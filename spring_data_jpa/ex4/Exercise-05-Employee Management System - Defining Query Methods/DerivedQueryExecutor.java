import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DerivedQueryExecutor.java
 *
 * Exercise 05 - Defining Query Methods
 * ---------------------------------------------
 * Reproduces the single most "magic" feature of Spring Data JPA: turning
 * a repository method's NAME into a working SQL query, with zero method
 * body written by the developer.
 *
 * Supported grammar (subset of the real Spring Data JPA keyword table):
 *
 *   [findBy|findAllBy|countBy|existsBy|deleteBy] Property [Keyword] [And|Or Property [Keyword]]* [OrderBy Property [Asc|Desc]]
 *
 * Keywords supported: GreaterThan, LessThan, Containing, Like, True, False
 *                      (no keyword => equality)
 *
 * Examples this class can parse straight from method names:
 *   findByActiveTrue()
 *   findByNameContaining(String keyword)
 *   findBySalaryGreaterThan(BigDecimal salary)
 *   findByActiveAndSalaryGreaterThan(boolean active, BigDecimal salary)
 *   countByActive(boolean active)
 *   findByOrderBySalaryDesc()
 *
 * Design pattern: Interpreter (the method name is parsed like a tiny
 * domain-specific language and "executed" against SQL).
 */
public class DerivedQueryExecutor<T> {

    private static final Pattern PREFIX_PATTERN =
            Pattern.compile("^(findAllBy|findBy|countBy|existsBy|deleteBy)(.*)$");

    private static final List<String> KEYWORDS = List.of(
            "GreaterThanEqual", "LessThanEqual", "GreaterThan", "LessThan",
            "Containing", "Like", "IsNotNull", "IsNull", "True", "False");

    private final Class<T> entityClass;
    private final String tableName;

    public DerivedQueryExecutor(Class<T> entityClass, String tableName) {
        this.entityClass = entityClass;
        this.tableName = tableName;
    }

    /** Returns true if this executor recognizes the given method as a derived query method. */
    public boolean supports(Method method) {
        return PREFIX_PATTERN.matcher(method.getName()).matches();
    }

    @SuppressWarnings("unchecked")
    public Object execute(Method method, Object[] args) throws Exception {
        Matcher m = PREFIX_PATTERN.matcher(method.getName());
        if (!m.matches()) {
            throw new UnsupportedOperationException("Not a derived query method: " + method.getName());
        }
        String prefix = m.group(1);
        String rest = m.group(2); // everything after "findBy" etc.

        // Split off an ORDER BY clause if present.
        String orderByClause = "";
        int orderByIdx = rest.indexOf("OrderBy");
        String conditionPart = rest;
        if (orderByIdx >= 0) {
            conditionPart = rest.substring(0, orderByIdx);
            orderByClause = buildOrderBy(rest.substring(orderByIdx + "OrderBy".length()));
        }

        StringBuilder whereClause = new StringBuilder();
        List<Object> params = new ArrayList<>();
        int argIndex = 0;

        if (!conditionPart.isEmpty()) {
            // Split on And / Or while keeping the connector, e.g. "ActiveAndSalaryGreaterThan"
            String[] tokens = conditionPart.split("(?=And|Or)|(?<=[a-z])(?=And|Or)");
            // Simpler & more reliable: manual split using regex with capture group
            List<String> parts = splitConditions(conditionPart);
            for (int i = 0; i < parts.size(); i++) {
                String token = parts.get(i);
                if (token.equals("And") || token.equals("Or")) {
                    whereClause.append(token.equals("And") ? " AND " : " OR ");
                    continue;
                }
                ConditionSpec spec = parseCondition(token);
                Field field = findField(spec.property);
                String column = EntityMetadataUtil.getColumnName(field) != null
                        ? EntityMetadataUtil.getColumnName(field) : field.getName();

                switch (spec.keyword) {
                    case "GreaterThan" -> { whereClause.append(column).append(" > ?"); params.add(args[argIndex++]); }
                    case "LessThan" -> { whereClause.append(column).append(" < ?"); params.add(args[argIndex++]); }
                    case "GreaterThanEqual" -> { whereClause.append(column).append(" >= ?"); params.add(args[argIndex++]); }
                    case "LessThanEqual" -> { whereClause.append(column).append(" <= ?"); params.add(args[argIndex++]); }
                    case "Containing", "Like" -> {
                        whereClause.append(column).append(" LIKE ?");
                        params.add("%" + args[argIndex++] + "%");
                    }
                    case "True" -> whereClause.append(column).append(" = TRUE");
                    case "False" -> whereClause.append(column).append(" = FALSE");
                    case "IsNull" -> whereClause.append(column).append(" IS NULL");
                    case "IsNotNull" -> whereClause.append(column).append(" IS NOT NULL");
                    default -> { whereClause.append(column).append(" = ?"); params.add(args[argIndex++]); }
                }
            }
        }

        String sql = switch (prefix) {
            case "findBy", "findAllBy" -> "SELECT * FROM " + tableName
                    + (whereClause.length() > 0 ? " WHERE " + whereClause : "") + orderByClause;
            case "countBy" -> "SELECT COUNT(*) FROM " + tableName
                    + (whereClause.length() > 0 ? " WHERE " + whereClause : "");
            case "existsBy" -> "SELECT COUNT(*) FROM " + tableName
                    + (whereClause.length() > 0 ? " WHERE " + whereClause : "") + " LIMIT 1";
            case "deleteBy" -> "DELETE FROM " + tableName
                    + (whereClause.length() > 0 ? " WHERE " + whereClause : "");
            default -> throw new UnsupportedOperationException("Unknown prefix: " + prefix);
        };

        return runQuery(prefix, sql, params);
    }

    // ---------------------------------------------------------------------

    private Object runQuery(String prefix, String sql, List<Object> params) throws Exception {
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            return switch (prefix) {
                case "findBy", "findAllBy" -> {
                    List<T> results = new ArrayList<>();
                    try (ResultSet rs = ps.executeQuery()) {
                        SimpleJpaRepository<T, Integer> mapper = new SimpleJpaRepository<>(entityClass);
                        while (rs.next()) {
                            results.add(mapper.mapRow(rs));
                        }
                    }
                    yield results;
                }
                case "countBy" -> {
                    try (ResultSet rs = ps.executeQuery()) {
                        yield rs.next() ? rs.getLong(1) : 0L;
                    }
                }
                case "existsBy" -> {
                    try (ResultSet rs = ps.executeQuery()) {
                        yield rs.next() && rs.getLong(1) > 0;
                    }
                }
                case "deleteBy" -> {
                    yield ps.executeUpdate();
                }
                default -> throw new UnsupportedOperationException(prefix);
            };
        }
    }

    private String buildOrderBy(String orderPart) {
        if (orderPart.isEmpty()) return "";
        boolean desc = orderPart.endsWith("Desc");
        boolean asc = orderPart.endsWith("Asc");
        String property = orderPart;
        if (desc) property = orderPart.substring(0, orderPart.length() - 4);
        if (asc) property = orderPart.substring(0, orderPart.length() - 3);
        Field field = findField(property);
        String column = EntityMetadataUtil.getColumnName(field) != null
                ? EntityMetadataUtil.getColumnName(field) : field.getName();
        return " ORDER BY " + column + (desc ? " DESC" : " ASC");
    }

    /** Splits "ActiveAndSalaryGreaterThan" into ["Active", "And", "SalaryGreaterThan"]. */
    private List<String> splitConditions(String conditionPart) {
        List<String> result = new ArrayList<>();
        Pattern p = Pattern.compile("(And|Or)");
        Matcher matcher = p.matcher(conditionPart);
        int last = 0;
        while (matcher.find()) {
            result.add(conditionPart.substring(last, matcher.start()));
            result.add(matcher.group(1));
            last = matcher.end();
        }
        result.add(conditionPart.substring(last));
        return result;
    }

    private ConditionSpec parseCondition(String token) {
        for (String kw : KEYWORDS) {
            if (token.endsWith(kw)) {
                return new ConditionSpec(token.substring(0, token.length() - kw.length()), kw);
            }
        }
        return new ConditionSpec(token, "Equals");
    }

    private Field findField(String propertyPascalCase) {
        String target = propertyPascalCase.substring(0, 1).toLowerCase(Locale.ROOT) + propertyPascalCase.substring(1);
        for (Field f : entityClass.getDeclaredFields()) {
            if (f.getName().equalsIgnoreCase(target)) {
                f.setAccessible(true);
                return f;
            }
        }
        throw new IllegalArgumentException("No field '" + target + "' found on " + entityClass.getSimpleName());
    }

    private static class ConditionSpec {
        final String property;
        final String keyword;
        ConditionSpec(String property, String keyword) {
            this.property = property;
            this.keyword = keyword;
        }
    }
}

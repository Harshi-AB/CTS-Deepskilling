import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * java.lang.reflect.Proxy InvocationHandler backing every repository
 * interface in this project. Provides the generic CRUD operations
 * (save / findById / findAll / deleteById / count) declared on
 * {@link Repository}, and derives SQL for Spring-Data-JPA style
 * "Query Methods" purely from the invoked method's name via
 * {@link QueryMethodParser}. This is what lets
 * "RepositoryFactory.create(XxxRepository.class, Xxx.class)" hand back
 * a fully working repository without any Spring/Hibernate JAR.
 */
public class RepositoryInvocationHandler implements InvocationHandler {

    private final Class<?> entityClass;

    public RepositoryInvocationHandler(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        switch (method.getName()) {
            case "toString":  return "RepositoryProxy(" + entityClass.getSimpleName() + ")";
            case "hashCode":  return System.identityHashCode(proxy);
            case "equals":    return proxy == (args != null ? args[0] : null);
            case "save":      return save(args[0]);
            case "findById":  return findById(args[0]);
            case "findAll":   return findAll();
            case "deleteById": deleteById(args[0]); return null;
            case "count":     return count();
            default:          return handleQueryMethod(method, args);
        }
    }

    // ---------------------------------------------------------------
    // generic CRUD
    // ---------------------------------------------------------------

    private Object save(Object entity) throws Exception {
        Field idField = EntityUtils.idField(entityClass);
        Object idValue = EntityUtils.get(idField, entity);
        boolean isNew = (idValue == null) || (idValue instanceof Number && ((Number) idValue).longValue() == 0);
        if (isNew) {
            insert(entity, idField);
        } else {
            update(entity, idField);
        }
        return entity;
    }

    private void insert(Object entity, Field idField) throws Exception {
        List<Field> columns = EntityUtils.columnFields(entityClass);
        List<Field> manyToOne = EntityUtils.manyToOneFields(entityClass);

        StringBuilder cols = new StringBuilder();
        StringBuilder marks = new StringBuilder();
        List<Object> values = new ArrayList<>();

        for (Field f : columns) {
            if (f.equals(idField)) {
                continue;
            }
            cols.append(EntityUtils.columnName(f)).append(",");
            marks.append("?,");
            values.add(EntityUtils.get(f, entity));
        }
        for (Field f : manyToOne) {
            JoinColumn jc = f.getAnnotation(JoinColumn.class);
            Object related = EntityUtils.get(f, entity);
            if (related != null) {
                Field relatedId = EntityUtils.idField(related.getClass());
                cols.append(jc.name()).append(",");
                marks.append("?,");
                values.add(EntityUtils.get(relatedId, related));
            }
        }
        if (cols.length() > 0) {
            cols.setLength(cols.length() - 1);
            marks.setLength(marks.length() - 1);
        }

        String sql = "INSERT INTO " + EntityUtils.tableName(entityClass) + " (" + cols + ") VALUES (" + marks + ")";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bindValues(ps, values);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    EntityUtils.set(idField, entity, castToFieldType(idField.getType(), keys.getLong(1)));
                }
            }
        }
    }

    private void update(Object entity, Field idField) throws Exception {
        List<Field> columns = EntityUtils.columnFields(entityClass);
        List<Field> manyToOne = EntityUtils.manyToOneFields(entityClass);

        StringBuilder set = new StringBuilder();
        List<Object> values = new ArrayList<>();
        for (Field f : columns) {
            if (f.equals(idField)) {
                continue;
            }
            set.append(EntityUtils.columnName(f)).append("=?,");
            values.add(EntityUtils.get(f, entity));
        }
        for (Field f : manyToOne) {
            JoinColumn jc = f.getAnnotation(JoinColumn.class);
            Object related = EntityUtils.get(f, entity);
            set.append(jc.name()).append("=?,");
            values.add(related == null ? null : EntityUtils.get(EntityUtils.idField(related.getClass()), related));
        }
        if (set.length() > 0) {
            set.setLength(set.length() - 1);
        }

        String sql = "UPDATE " + EntityUtils.tableName(entityClass) + " SET " + set + " WHERE "
                + EntityUtils.columnName(idField) + "=?";
        values.add(EntityUtils.get(idField, entity));

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            bindValues(ps, values);
            ps.executeUpdate();
        }
    }

    private Optional<Object> findById(Object id) throws Exception {
        Field idField = EntityUtils.idField(entityClass);
        String sql = "SELECT * FROM " + EntityUtils.tableName(entityClass) + " WHERE "
                + EntityUtils.columnName(idField) + "=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs, con));
                }
            }
        }
        return Optional.empty();
    }

    private List<Object> findAll() throws Exception {
        List<Object> result = new ArrayList<>();
        String sql = "SELECT * FROM " + EntityUtils.tableName(entityClass);
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(mapRow(rs, con));
            }
        }
        return result;
    }

    private void deleteById(Object id) throws Exception {
        Field idField = EntityUtils.idField(entityClass);
        String sql = "DELETE FROM " + EntityUtils.tableName(entityClass) + " WHERE "
                + EntityUtils.columnName(idField) + "=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        }
    }

    private long count() throws Exception {
        String sql = "SELECT COUNT(*) FROM " + EntityUtils.tableName(entityClass);
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getLong(1);
        }
    }

    // ---------------------------------------------------------------
    // query methods, e.g. findByCoNameContainingOrderByCoNameAsc(...)
    // ---------------------------------------------------------------

    private Object handleQueryMethod(Method method, Object[] args) throws Exception {
        QueryMethodParser query = QueryMethodParser.parse(method.getName());
        Map<String, Field> fieldsByName = new HashMap<>();
        for (Field f : EntityUtils.columnFields(entityClass)) {
            fieldsByName.put(f.getName(), f);
        }

        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(EntityUtils.tableName(entityClass));
        List<Object> values = new ArrayList<>();
        int argIndex = 0;

        if (!query.conditions.isEmpty()) {
            sql.append(" WHERE ");
            for (int i = 0; i < query.conditions.size(); i++) {
                QueryMethodParser.Condition c = query.conditions.get(i);
                Field f = fieldsByName.get(c.fieldName);
                if (f == null) {
                    throw new IllegalArgumentException("Unknown field '" + c.fieldName
                            + "' referenced by query method " + method.getName());
                }
                String column = EntityUtils.columnName(f);
                if (i > 0) {
                    sql.append(" AND ");
                }
                switch (c.operator) {
                    case "CONTAINS":
                        sql.append(column).append(" LIKE ?");
                        values.add("%" + args[argIndex++] + "%");
                        break;
                    case "STARTS_WITH":
                        sql.append(column).append(" LIKE ?");
                        values.add(args[argIndex++] + "%");
                        break;
                    case "ENDS_WITH":
                        sql.append(column).append(" LIKE ?");
                        values.add("%" + args[argIndex++]);
                        break;
                    case "GT":
                        sql.append(column).append(" > ?");
                        values.add(args[argIndex++]);
                        break;
                    case "LT":
                        sql.append(column).append(" < ?");
                        values.add(args[argIndex++]);
                        break;
                    case "GTE":
                        sql.append(column).append(" >= ?");
                        values.add(args[argIndex++]);
                        break;
                    case "LTE":
                        sql.append(column).append(" <= ?");
                        values.add(args[argIndex++]);
                        break;
                    case "BETWEEN":
                        sql.append(column).append(" BETWEEN ? AND ?");
                        values.add(args[argIndex++]);
                        values.add(args[argIndex++]);
                        break;
                    default:
                        sql.append(column).append(" = ?");
                        values.add(args[argIndex++]);
                }
            }
        }

        if (query.orderByField != null) {
            Field f = fieldsByName.get(query.orderByField);
            String column = (f != null) ? EntityUtils.columnName(f) : query.orderByField;
            sql.append(" ORDER BY ").append(column).append(query.orderDescending ? " DESC" : " ASC");
        }
        if (query.limit != null) {
            sql.append(" LIMIT ").append(query.limit);
        }

        List<Object> results = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            bindValues(ps, values);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs, con));
                }
            }
        }

        Class<?> returnType = method.getReturnType();
        if (List.class.isAssignableFrom(returnType)) {
            return results;
        }
        if (Optional.class.isAssignableFrom(returnType)) {
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        }
        return results.isEmpty() ? null : results.get(0);
    }

    // ---------------------------------------------------------------
    // row -> entity mapping (including relationship loading)
    // ---------------------------------------------------------------

    private Object mapRow(ResultSet rs, Connection con) throws Exception {
        Object entity = mapRowShallow(rs);

        // @ManyToOne defaults to FetchType.EAGER (per JPA spec) - load the parent eagerly.
        for (Field f : EntityUtils.manyToOneFields(entityClass)) {
            ManyToOne ann = f.getAnnotation(ManyToOne.class);
            JoinColumn jc = f.getAnnotation(JoinColumn.class);
            Object fkValue = rs.getObject(jc.name());
            if (fkValue != null && ann.fetch() == FetchType.EAGER) {
                EntityUtils.set(f, entity, loadById(f.getType(), fkValue, con));
            }
        }

        // @OneToMany is populated only when fetch = EAGER. When LAZY, the field is left
        // null, which makes the entity's own getter throw LazyInitializationException -
        // mirroring real Hibernate behaviour for this exact hands-on exercise.
        for (Field f : EntityUtils.oneToManyFields(entityClass)) {
            OneToMany ann = f.getAnnotation(OneToMany.class);
            if (ann.fetch() == FetchType.EAGER) {
                EntityUtils.set(f, entity, loadOneToMany(f, ann.mappedBy(), entity));
            }
        }

        // @ManyToMany - same LAZY/EAGER rule as above.
        for (Field f : EntityUtils.manyToManyFields(entityClass)) {
            ManyToMany ann = f.getAnnotation(ManyToMany.class);
            if (ann.fetch() == FetchType.EAGER) {
                EntityUtils.set(f, entity, loadManyToMany(f, entity));
            }
        }

        return entity;
    }

    /** Maps only the simple @Column fields - used to stop relationship-loading recursion. */
    private Object mapRowShallow(ResultSet rs) throws Exception {
        Object entity = entityClass.getDeclaredConstructor().newInstance();
        for (Field f : EntityUtils.columnFields(entityClass)) {
            Object value = rs.getObject(EntityUtils.columnName(f));
            EntityUtils.set(f, entity, convert(value, f.getType()));
        }
        return entity;
    }

    private Object loadById(Class<?> relatedClass, Object idValue, Connection con) throws Exception {
        Field relatedId = EntityUtils.idField(relatedClass);
        String sql = "SELECT * FROM " + EntityUtils.tableName(relatedClass) + " WHERE "
                + EntityUtils.columnName(relatedId) + "=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, idValue);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new RepositoryInvocationHandler(relatedClass).mapRow(rs, con);
                }
            }
        }
        return null;
    }

    private Set<Object> loadOneToMany(Field collectionField, String mappedBy, Object owner) throws Exception {
        Class<?> childClass = (Class<?>) ((ParameterizedType) collectionField.getGenericType())
                .getActualTypeArguments()[0];
        Field ownerFieldOnChild = childClass.getDeclaredField(mappedBy);
        JoinColumn jc = ownerFieldOnChild.getAnnotation(JoinColumn.class);
        Object ownerIdValue = EntityUtils.get(EntityUtils.idField(entityClass), owner);

        Set<Object> children = new LinkedHashSet<>();
        String sql = "SELECT * FROM " + EntityUtils.tableName(childClass) + " WHERE " + jc.name() + "=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, ownerIdValue);
            try (ResultSet rs = ps.executeQuery()) {
                RepositoryInvocationHandler childHandler = new RepositoryInvocationHandler(childClass);
                while (rs.next()) {
                    // shallow mapping to avoid recursively re-loading the owner side
                    Object child = childHandler.mapRowShallow(rs);
                    if (ownerFieldOnChild.getType().isAssignableFrom(entityClass)) {
                        EntityUtils.set(ownerFieldOnChild, child, owner);
                    }
                    children.add(child);
                }
            }
        }
        return children;
    }

    private Set<Object> loadManyToMany(Field collectionField, Object owner) throws Exception {
        Class<?> targetClass = (Class<?>) ((ParameterizedType) collectionField.getGenericType())
                .getActualTypeArguments()[0];
        ManyToMany ann = collectionField.getAnnotation(ManyToMany.class);

        String joinTableName;
        String ownerColumn;
        String targetColumn;

        if (!ann.mappedBy().isEmpty()) {
            // inverse side: read the @JoinTable declared on the owning field of targetClass
            Field owningField = null;
            for (Field f : targetClass.getDeclaredFields()) {
                if (f.isAnnotationPresent(ManyToMany.class) && f.isAnnotationPresent(JoinTable.class)) {
                    owningField = f;
                    break;
                }
            }
            JoinTable jt = owningField.getAnnotation(JoinTable.class);
            joinTableName = jt.name();
            ownerColumn = jt.inverseJoinColumns()[0].name();
            targetColumn = jt.joinColumns()[0].name();
        } else {
            JoinTable jt = collectionField.getAnnotation(JoinTable.class);
            joinTableName = jt.name();
            ownerColumn = jt.joinColumns()[0].name();
            targetColumn = jt.inverseJoinColumns()[0].name();
        }

        Object ownerIdValue = EntityUtils.get(EntityUtils.idField(entityClass), owner);
        Field targetIdField = EntityUtils.idField(targetClass);

        String sql = "SELECT t.* FROM " + EntityUtils.tableName(targetClass) + " t "
                + "JOIN " + joinTableName + " j ON t." + EntityUtils.columnName(targetIdField)
                + " = j." + targetColumn + " WHERE j." + ownerColumn + "=?";

        Set<Object> related = new LinkedHashSet<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, ownerIdValue);
            try (ResultSet rs = ps.executeQuery()) {
                RepositoryInvocationHandler targetHandler = new RepositoryInvocationHandler(targetClass);
                while (rs.next()) {
                    related.add(targetHandler.mapRowShallow(rs));
                }
            }
        }
        return related;
    }

    // ---------------------------------------------------------------
    // low level helpers
    // ---------------------------------------------------------------

    private void bindValues(PreparedStatement ps, List<Object> values) throws SQLException {
        for (int i = 0; i < values.size(); i++) {
            ps.setObject(i + 1, values.get(i));
        }
    }

    private Object convert(Object value, Class<?> targetType) {
        if (value == null) {
            return null;
        }
        if ((targetType == boolean.class || targetType == Boolean.class) && value instanceof Number) {
            return ((Number) value).intValue() != 0;
        }
        if ((targetType == double.class || targetType == Double.class) && value instanceof BigDecimal) {
            return ((BigDecimal) value).doubleValue();
        }
        if ((targetType == int.class || targetType == Integer.class) && value instanceof Number) {
            return ((Number) value).intValue();
        }
        if ((targetType == long.class || targetType == Long.class) && value instanceof Number) {
            return ((Number) value).longValue();
        }
        return value;
    }

    private Object castToFieldType(Class<?> type, long value) {
        if (type == long.class || type == Long.class) {
            return value;
        }
        return (int) value;
    }
}

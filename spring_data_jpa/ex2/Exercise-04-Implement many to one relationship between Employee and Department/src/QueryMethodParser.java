import java.util.ArrayList;
import java.util.List;

/**
 * Parses Spring-Data-JPA "Query Methods" style method names - such as
 * findByCoNameContainingOrderByCoNameAsc or findTop3ByOrderByStVolumeDesc -
 * into a structured description (conditions, ordering, row limit), exactly
 * following the "Query Creation from method names" rules documented at
 * https://docs.spring.io/spring-data/jpa/docs/2.2.0.RELEASE/reference/html/#jpa.query-methods.query-creation
 *
 * No external Spring dependency is used - this is a small, self-contained
 * reimplementation of that method-name-to-query mechanism.
 */
public class QueryMethodParser {

    /** A single WHERE condition derived from part of the method name. */
    public static class Condition {
        public final String fieldName;
        public final String operator;

        public Condition(String fieldName, String operator) {
            this.fieldName = fieldName;
            this.operator = operator;
        }
    }

    public final List<Condition> conditions = new ArrayList<>();
    public String orderByField;
    public boolean orderDescending;
    public Integer limit;

    // Longest keywords first so e.g. "GreaterThanEqual" is not mistaken for "GreaterThan".
    private static final String[] OPERATOR_KEYWORDS = {
        "GreaterThanEqual", "LessThanEqual", "StartingWith", "EndingWith",
        "Containing", "Between", "GreaterThan", "LessThan", "Like"
    };

    public static QueryMethodParser parse(String methodName) {
        QueryMethodParser query = new QueryMethodParser();
        String remainder = methodName;

        if (remainder.startsWith("findAll")) {
            remainder = remainder.substring("findAll".length());
        } else if (remainder.startsWith("find")) {
            remainder = remainder.substring("find".length());
        } else if (remainder.startsWith("get")) {
            remainder = remainder.substring("get".length());
        } else if (remainder.startsWith("read")) {
            remainder = remainder.substring("read".length());
        }

        // Top<N> / First<N> - e.g. Top3By...
        if (remainder.startsWith("Top") || remainder.startsWith("First")) {
            String prefix = remainder.startsWith("Top") ? "Top" : "First";
            int i = prefix.length();
            int start = i;
            while (i < remainder.length() && Character.isDigit(remainder.charAt(i))) {
                i++;
            }
            String num = remainder.substring(start, i);
            query.limit = num.isEmpty() ? 1 : Integer.parseInt(num);
            remainder = remainder.substring(i);
        }

        if (remainder.startsWith("By")) {
            remainder = remainder.substring(2);
        }

        String conditionsPart = remainder;
        String orderPart = null;
        int orderIdx = remainder.indexOf("OrderBy");
        if (orderIdx >= 0) {
            conditionsPart = remainder.substring(0, orderIdx);
            orderPart = remainder.substring(orderIdx + "OrderBy".length());
        }

        if (!conditionsPart.isEmpty()) {
            for (String part : conditionsPart.split("And")) {
                query.conditions.add(parseCondition(part));
            }
        }

        if (orderPart != null && !orderPart.isEmpty()) {
            boolean desc = orderPart.endsWith("Desc");
            boolean asc = orderPart.endsWith("Asc");
            String field = orderPart;
            if (desc) {
                field = orderPart.substring(0, orderPart.length() - "Desc".length());
            } else if (asc) {
                field = orderPart.substring(0, orderPart.length() - "Asc".length());
            }
            query.orderByField = decapitalize(field);
            query.orderDescending = desc;
        }

        return query;
    }

    private static Condition parseCondition(String part) {
        for (String keyword : OPERATOR_KEYWORDS) {
            if (part.endsWith(keyword) && part.length() > keyword.length()) {
                String field = part.substring(0, part.length() - keyword.length());
                return new Condition(decapitalize(field), toOperatorCode(keyword));
            }
        }
        return new Condition(decapitalize(part), "EQUALS");
    }

    private static String toOperatorCode(String keyword) {
        switch (keyword) {
            case "GreaterThanEqual": return "GTE";
            case "LessThanEqual":    return "LTE";
            case "StartingWith":     return "STARTS_WITH";
            case "EndingWith":       return "ENDS_WITH";
            case "Containing":       return "CONTAINS";
            case "Between":          return "BETWEEN";
            case "GreaterThan":      return "GT";
            case "LessThan":         return "LT";
            case "Like":             return "LIKE";
            default:                 return "EQUALS";
        }
    }

    private static String decapitalize(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }
}

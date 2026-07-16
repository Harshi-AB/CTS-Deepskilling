import java.util.LinkedHashMap;
import java.util.Map;

/**
 * JsonUtil.java
 *
 * Minimal, dependency-free JSON helper used across this project.
 * Hand-rolled on purpose, since the exercise forbids external
 * libraries such as Jackson/Gson and any build tool.
 */
public class JsonUtil {

    private JsonUtil() {
    }

    public static String toJson(Employee employee) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"id\":").append(employee.getId()).append(",");
        sb.append("\"name\":\"").append(escape(employee.getName())).append("\",");
        sb.append("\"department\":\"").append(escape(employee.getDepartment())).append("\",");
        sb.append("\"salary\":").append(employee.getSalary());
        sb.append("}");
        return sb.toString();
    }

    public static String toJsonArray(Iterable<Employee> employees) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Employee e : employees) {
            if (!first) {
                sb.append(",");
            }
            sb.append(toJson(e));
            first = false;
        }
        sb.append("]");
        return sb.toString();
    }

    public static Map<String, String> parseFlatObject(String json) {
        Map<String, String> result = new LinkedHashMap<>();
        if (json == null) {
            return result;
        }
        String trimmed = json.trim();
        if (trimmed.startsWith("{")) {
            trimmed = trimmed.substring(1);
        }
        if (trimmed.endsWith("}")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        if (trimmed.isBlank()) {
            return result;
        }
        String[] pairs = trimmed.split(",");
        for (String pair : pairs) {
            String[] kv = pair.split(":", 2);
            if (kv.length != 2) {
                continue;
            }
            String key = stripQuotes(kv[0].trim());
            String value = stripQuotes(kv[1].trim());
            result.put(key, value);
        }
        return result;
    }

    private static String stripQuotes(String s) {
        if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public static String errorJson(String message) {
        return "{\"error\":\"" + escape(message) + "\"}";
    }
}

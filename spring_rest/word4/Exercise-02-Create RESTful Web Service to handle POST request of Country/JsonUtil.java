import java.util.LinkedHashMap;
import java.util.Map;

/**
 * JsonUtil.java
 *
 * Minimal, dependency-free JSON helper used across the project.
 * Since the exercise forbids external libraries (no Jackson/Gson, no Maven/Gradle),
 * this class hand-rolls just enough JSON reading/writing for flat, simple objects,
 * which is all a Country resource needs.
 *
 * This is intentionally small in scope - it is NOT a general purpose JSON parser.
 */
public class JsonUtil {

    private JsonUtil() {
        // utility class - prevent instantiation
    }

    /** Converts a Country bean into a JSON object string. */
    public static String toJson(Country country) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"code\":\"").append(escape(country.getCode())).append("\",");
        sb.append("\"name\":\"").append(escape(country.getName())).append("\",");
        sb.append("\"capital\":\"").append(escape(country.getCapital())).append("\",");
        sb.append("\"region\":\"").append(escape(country.getRegion())).append("\",");
        sb.append("\"population\":").append(country.getPopulation());
        sb.append("}");
        return sb.toString();
    }

    /** Converts a list-like array of countries (as varargs) into a JSON array string. */
    public static String toJsonArray(Iterable<Country> countries) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (Country c : countries) {
            if (!first) {
                sb.append(",");
            }
            sb.append(toJson(c));
            first = false;
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Parses a single flat JSON object (no nested objects/arrays) into a
     * String-keyed map. Good enough for reading a posted Country payload.
     */
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

    /** Builds a simple JSON error object: {"error":"message"} */
    public static String errorJson(String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"error\":\"").append(escape(message)).append("\"}");
        return sb.toString();
    }
}

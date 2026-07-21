import java.util.LinkedHashMap;
import java.util.Map;

/**
 * JsonUtil is a minimal, dependency-free helper used to convert simple
 * (flat) key/value maps to JSON text and back again. Duplicated into
 * this project (rather than shared) so that every exercise folder
 * compiles completely independently with "javac *.java".
 */
public final class JsonUtil {

    private JsonUtil() {
        // Utility class
    }

    public static String toJson(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        int i = 0;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append("\"").append(escape(entry.getKey())).append("\":");
            Object value = entry.getValue();
            if (value == null) {
                sb.append("null");
            } else if (value instanceof Number || value instanceof Boolean) {
                sb.append(value.toString());
            } else {
                sb.append("\"").append(escape(value.toString())).append("\"");
            }
            i++;
        }
        sb.append("}");
        return sb.toString();
    }

    public static Map<String, String> fromJson(String json) {
        Map<String, String> result = new LinkedHashMap<>();
        if (json == null || json.trim().isEmpty()) {
            return result;
        }
        String trimmed = json.trim();
        if (trimmed.startsWith("{")) {
            trimmed = trimmed.substring(1);
        }
        if (trimmed.endsWith("}")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }

        boolean inQuotes = false;
        StringBuilder token = new StringBuilder();
        for (int idx = 0; idx < trimmed.length(); idx++) {
            char c = trimmed.charAt(idx);
            if (c == '"' && (idx == 0 || trimmed.charAt(idx - 1) != '\\')) {
                inQuotes = !inQuotes;
            }
            if (c == ',' && !inQuotes) {
                addPair(result, token.toString());
                token.setLength(0);
            } else {
                token.append(c);
            }
        }
        if (token.length() > 0) {
            addPair(result, token.toString());
        }
        return result;
    }

    private static void addPair(Map<String, String> result, String pairText) {
        int colonIndex = findColon(pairText);
        if (colonIndex < 0) {
            return;
        }
        String key = stripQuotes(pairText.substring(0, colonIndex).trim());
        String value = stripQuotes(pairText.substring(colonIndex + 1).trim());
        result.put(key, value);
    }

    private static int findColon(String text) {
        boolean inQuotes = false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '"' && (i == 0 || text.charAt(i - 1) != '\\')) {
                inQuotes = !inQuotes;
            }
            if (c == ':' && !inQuotes) {
                return i;
            }
        }
        return -1;
    }

    private static String stripQuotes(String value) {
        if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}

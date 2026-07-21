import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal JSON writer/reader sufficient for flat JWT header and payload
 * objects (string, number and boolean values only). This avoids pulling
 * in any external JSON library, keeping the project to Core Java only.
 */
public final class JsonUtil {

    private JsonUtil() {
        // utility class - no instances
    }

    /** Serializes a flat map into a compact JSON object string. */
    public static String toJson(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) {
                sb.append(",");
            }
            first = false;
            sb.append("\"").append(escape(entry.getKey())).append("\":");
            appendValue(sb, entry.getValue());
        }
        sb.append("}");
        return sb.toString();
    }

    private static void appendValue(StringBuilder sb, Object value) {
        if (value instanceof Number || value instanceof Boolean) {
            sb.append(value.toString());
        } else {
            sb.append("\"").append(escape(String.valueOf(value))).append("\"");
        }
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    /**
     * Parses a compact, flat JSON object (no nested objects/arrays) into
     * a String-keyed map. Numeric-looking values are parsed as Long,
     * "true"/"false" as Boolean, everything else stays a String.
     */
    public static Map<String, Object> parse(String json) {
        Map<String, Object> result = new LinkedHashMap<>();
        String trimmed = json.trim();
        if (trimmed.length() < 2) {
            return result;
        }
        trimmed = trimmed.substring(1, trimmed.length() - 1); // strip { }

        int i = 0;
        int len = trimmed.length();
        while (i < len) {
            // skip whitespace / commas
            while (i < len && (trimmed.charAt(i) == ',' || Character.isWhitespace(trimmed.charAt(i)))) {
                i++;
            }
            if (i >= len) break;

            // read key
            int keyStart = trimmed.indexOf('"', i) + 1;
            int keyEnd = trimmed.indexOf('"', keyStart);
            String key = trimmed.substring(keyStart, keyEnd);
            i = keyEnd + 1;

            // skip colon
            while (i < len && (trimmed.charAt(i) == ':' || Character.isWhitespace(trimmed.charAt(i)))) {
                i++;
            }

            // read value
            Object value;
            if (trimmed.charAt(i) == '"') {
                int valStart = i + 1;
                int valEnd = trimmed.indexOf('"', valStart);
                value = trimmed.substring(valStart, valEnd);
                i = valEnd + 1;
            } else {
                int valStart = i;
                while (i < len && trimmed.charAt(i) != ',') {
                    i++;
                }
                String raw = trimmed.substring(valStart, i).trim();
                if (raw.equals("true") || raw.equals("false")) {
                    value = Boolean.parseBoolean(raw);
                } else {
                    try {
                        value = Long.parseLong(raw);
                    } catch (NumberFormatException e) {
                        value = raw;
                    }
                }
            }
            result.put(key, value);
        }
        return result;
    }
}

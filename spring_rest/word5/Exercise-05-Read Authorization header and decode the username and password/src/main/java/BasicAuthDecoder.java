import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Utility class encapsulating the logic to manually decode an HTTP
 * "Authorization: Basic &lt;base64(username:password)&gt;" header.
 *
 * This is the exact algorithm the HTTP Basic specification (RFC 7617)
 * defines, and it is what Spring Security performs under the hood.
 */
public final class BasicAuthDecoder {

    private static final String BASIC_PREFIX = "Basic ";

    private BasicAuthDecoder() {
        // Utility class: prevent instantiation
    }

    /**
     * Immutable holder for the decoded username/password pair.
     */
    public static final class Credentials {
        private final String username;
        private final String password;

        public Credentials(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

    /**
     * Decodes a raw "Authorization" header value of the form
     * "Basic &lt;base64&gt;" into its username/password components.
     *
     * @param authorizationHeader the raw header value, e.g. "Basic dXNlcjpwYXNz"
     * @return the decoded Credentials
     * @throws IllegalArgumentException if the header is missing, malformed,
     *                                  or is not a "Basic" scheme header
     */
    public static Credentials decode(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new IllegalArgumentException("Authorization header is missing");
        }

        if (!authorizationHeader.startsWith(BASIC_PREFIX)) {
            throw new IllegalArgumentException("Authorization header does not use the Basic scheme");
        }

        // Strip the "Basic " prefix to get just the Base64 payload
        String base64Credentials = authorizationHeader.substring(BASIC_PREFIX.length()).trim();

        // Decode Base64 -> raw bytes -> UTF-8 string "username:password"
        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
        String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);

        // Split on the FIRST colon only, since passwords may contain colons
        int separatorIndex = decodedString.indexOf(':');
        if (separatorIndex < 0) {
            throw new IllegalArgumentException("Decoded credentials are missing the ':' separator");
        }

        String username = decodedString.substring(0, separatorIndex);
        String password = decodedString.substring(separatorIndex + 1);

        return new Credentials(username, password);
    }

    /**
     * Convenience helper: the inverse operation, useful for building test
     * requests. Encodes a username/password pair into a full
     * "Basic &lt;base64&gt;" header value.
     */
    public static String encode(String username, String password) {
        String raw = username + ":" + password;
        String base64 = Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
        return BASIC_PREFIX + base64;
    }
}

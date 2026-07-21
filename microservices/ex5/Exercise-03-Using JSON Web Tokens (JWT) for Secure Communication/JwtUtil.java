import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * JwtUtil
 * ----------------------------------------------------------------------
 * A minimal, dependency-free implementation of JSON Web Tokens (JWT),
 * built entirely with core Java (java.util.Base64 + javax.crypto -
 * both part of the standard JDK, so no external JWT library is needed).
 *
 * A JWT has three Base64Url-encoded parts, separated by dots:
 *      header.payload.signature
 *
 *  - header    : algorithm + token type, e.g. {"alg":"HS256","typ":"JWT"}
 *  - payload   : claims, e.g. subject (username), issued-at, expiry
 *  - signature : HMAC-SHA256(header + "." + payload, SECRET_KEY)
 *
 * The signature lets a server later verify the token was NOT tampered
 * with, and was issued by someone who knows SECRET_KEY.
 * ----------------------------------------------------------------------
 */
public class JwtUtil {

    // In a real system this would come from a secure config/secret store.
    private static final String SECRET_KEY = "CognizantDeepSkillingSecretKey123!";
    private static final String ALGORITHM = "HmacSHA256";
    private static final long EXPIRY_MILLIS = 5 * 60 * 1000; // token valid for 5 minutes

    /**
     * Builds and signs a new JWT for the given username.
     *
     * @param username the authenticated user's name, stored as the "sub" claim
     * @return a complete, signed JWT string
     */
    public static String generateToken(String username) {
        try {
            long issuedAt = System.currentTimeMillis();
            long expiresAt = issuedAt + EXPIRY_MILLIS;

            String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
            String payload = "{\"sub\":\"" + username + "\",\"iat\":" + issuedAt + ",\"exp\":" + expiresAt + "}";

            String encodedHeader = base64UrlEncode(header.getBytes(StandardCharsets.UTF_8));
            String encodedPayload = base64UrlEncode(payload.getBytes(StandardCharsets.UTF_8));

            String unsignedToken = encodedHeader + "." + encodedPayload;
            String signature = sign(unsignedToken);

            return unsignedToken + "." + signature;
        } catch (Exception e) {
            throw new RuntimeException("Error generating JWT", e);
        }
    }

    /**
     * Verifies a token's signature AND checks that it has not expired.
     *
     * @param token the JWT string received from a client
     * @return true if the token is authentic and still valid
     */
    public static boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return false; // malformed token
            }

            String unsignedToken = parts[0] + "." + parts[1];
            String expectedSignature = sign(unsignedToken);

            // Constant-time-ish comparison is best practice; equals() is fine for this exercise.
            if (!expectedSignature.equals(parts[2])) {
                return false; // signature mismatch -> token was tampered with or forged
            }

            String payloadJson = new String(base64UrlDecode(parts[1]), StandardCharsets.UTF_8);
            long expiresAt = extractLongField(payloadJson, "exp");

            return System.currentTimeMillis() < expiresAt;
        } catch (Exception e) {
            return false; // any parsing error -> treat as invalid
        }
    }

    /**
     * Extracts the username ("sub" claim) from a token.
     * Should only be called after validateToken() returns true.
     */
    public static String extractUsername(String token) {
        String[] parts = token.split("\\.");
        String payloadJson = new String(base64UrlDecode(parts[1]), StandardCharsets.UTF_8);
        return extractStringField(payloadJson, "sub");
    }

    // ---------------------------------------------------------------
    // Internal helpers
    // ---------------------------------------------------------------

    private static String sign(String data) throws Exception {
        Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM));
        byte[] signatureBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return base64UrlEncode(signatureBytes);
    }

    private static String base64UrlEncode(byte[] data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
    }

    private static byte[] base64UrlDecode(String data) {
        return Base64.getUrlDecoder().decode(data);
    }

    /** Extracts a numeric field, e.g. "exp":1719999999, from a flat JSON string. */
    private static long extractLongField(String json, String field) {
        String marker = "\"" + field + "\":";
        int idx = json.indexOf(marker);
        if (idx == -1) {
            throw new IllegalArgumentException("Field '" + field + "' not found in token payload.");
        }
        int start = idx + marker.length();
        int end = start;
        while (end < json.length() && Character.isDigit(json.charAt(end))) {
            end++;
        }
        return Long.parseLong(json.substring(start, end));
    }

    /** Extracts a string field, e.g. "sub":"alice", from a flat JSON string. */
    private static String extractStringField(String json, String field) {
        String marker = "\"" + field + "\":\"";
        int idx = json.indexOf(marker);
        if (idx == -1) {
            throw new IllegalArgumentException("Field '" + field + "' not found in token payload.");
        }
        int start = idx + marker.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }
}

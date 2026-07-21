import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Core JWT engine: creates (signs) and verifies JSON Web Tokens using
 * HMAC-SHA256 (the "HS256" algorithm), implemented with Core Java's
 * built-in javax.crypto.Mac - no external JWT library required.
 *
 * This class is what makes JWTs useful for "Secure Communication"
 * between services: any party holding the shared secret key can create
 * a token, and any party holding the same secret can verify that the
 * token's header+payload were not tampered with in transit.
 */
public final class JwtUtil {

    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private JwtUtil() {
        // utility class - no instances
    }

    /**
     * Creates and signs a new JWT from the given header and payload,
     * using the supplied secret key.
     */
    public static JwtToken create(JwtHeader header, JwtPayload payload, String secretKey) {
        String encodedHeader = Base64UrlCodec.encode(JsonUtil.toJson(header.toMap()));
        String encodedPayload = Base64UrlCodec.encode(JsonUtil.toJson(payload.toMap()));
        String signingInput = encodedHeader + "." + encodedPayload;

        String signature = sign(signingInput, secretKey);
        String compactForm = signingInput + "." + signature;

        return new JwtToken(header, payload, signature, compactForm);
    }

    /**
     * Parses and verifies a compact JWT string against the supplied
     * secret key. Throws SecurityException if the structure is invalid,
     * the signature does not match (i.e. the token was tampered with or
     * signed with a different key), or the token has expired.
     */
    public static JwtToken verifyAndParse(String compactForm, String secretKey) {
        String[] parts = compactForm.split("\\.");
        if (parts.length != 3) {
            throw new SecurityException("Malformed JWT: expected 3 segments, found " + parts.length);
        }
        String encodedHeader = parts[0];
        String encodedPayload = parts[1];
        String providedSignature = parts[2];

        String signingInput = encodedHeader + "." + encodedPayload;
        String expectedSignature = sign(signingInput, secretKey);

        if (!constantTimeEquals(expectedSignature, providedSignature)) {
            throw new SecurityException("JWT signature verification failed - token may have been tampered with");
        }

        Map<String, Object> headerMap = JsonUtil.parse(Base64UrlCodec.decodeToString(encodedHeader));
        Map<String, Object> payloadMap = JsonUtil.parse(Base64UrlCodec.decodeToString(encodedPayload));

        JwtHeader header = JwtHeader.fromMap(headerMap);
        JwtPayload payload = JwtPayload.fromMap(payloadMap);

        if (payload.isExpired()) {
            throw new SecurityException("JWT has expired");
        }

        return new JwtToken(header, payload, providedSignature, compactForm);
    }

    private static String sign(String data, String secretKey) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(
                    secretKey.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM);
            mac.init(keySpec);
            byte[] rawSignature = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64UrlCodec.encode(rawSignature);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to compute HMAC signature", e);
        }
    }

    /** Timing-safe comparison to avoid leaking signature validity via timing attacks. */
    private static boolean constantTimeEquals(String a, String b) {
        if (a.length() != b.length()) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }
}

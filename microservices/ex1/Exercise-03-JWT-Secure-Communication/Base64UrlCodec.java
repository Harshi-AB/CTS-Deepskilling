import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Small helper around Base64URL (unpadded) encoding/decoding, the
 * encoding scheme used for every segment of a JWT (RFC 7519 / RFC 4648
 * section 5).
 */
public final class Base64UrlCodec {

    private Base64UrlCodec() {
        // utility class - no instances
    }

    public static String encode(byte[] input) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(input);
    }

    public static String encode(String input) {
        return encode(input.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] decode(String input) {
        return Base64.getUrlDecoder().decode(input);
    }

    public static String decodeToString(String input) {
        return new String(decode(input), StandardCharsets.UTF_8);
    }
}

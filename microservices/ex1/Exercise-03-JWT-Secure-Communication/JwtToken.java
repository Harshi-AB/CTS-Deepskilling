/**
 * Represents a complete, encoded JSON Web Token in its standard
 * dot-separated compact serialization form:
 *
 *      base64url(header) . base64url(payload) . base64url(signature)
 *
 * This class is just an immutable holder for the three parts plus the
 * final compact string; the actual signing/verification cryptography
 * lives in JwtUtil.
 */
public class JwtToken {

    private final JwtHeader header;
    private final JwtPayload payload;
    private final String signature;      // base64url-encoded signature
    private final String compactForm;    // header.payload.signature

    public JwtToken(JwtHeader header, JwtPayload payload, String signature, String compactForm) {
        this.header = header;
        this.payload = payload;
        this.signature = signature;
        this.compactForm = compactForm;
    }

    public JwtHeader getHeader() {
        return header;
    }

    public JwtPayload getPayload() {
        return payload;
    }

    public String getSignature() {
        return signature;
    }

    public String getCompactForm() {
        return compactForm;
    }

    @Override
    public String toString() {
        return compactForm;
    }
}

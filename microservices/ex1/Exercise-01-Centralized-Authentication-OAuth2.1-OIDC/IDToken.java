import java.util.Map;

/**
 * Represents an OpenID Connect (OIDC) ID Token.
 *
 * The ID Token is the core artifact that distinguishes OIDC from plain
 * OAuth 2.1: it proves the *identity* of the authenticated user to the
 * client (whereas the Access Token only grants API access). It carries
 * standard claims such as iss (issuer), sub (subject/user id), aud
 * (audience/client id), iat, exp, plus any user profile claims.
 */
public class IDToken {

    private final String issuer;
    private final String subject;
    private final String audience;
    private final long issuedAtEpochSeconds;
    private final long expiryEpochSeconds;
    private final Map<String, Object> claims;

    public IDToken(String issuer, String subject, String audience,
                    long validForSeconds, Map<String, Object> claims) {
        this.issuer = issuer;
        this.subject = subject;
        this.audience = audience;
        this.issuedAtEpochSeconds = System.currentTimeMillis() / 1000;
        this.expiryEpochSeconds = this.issuedAtEpochSeconds + validForSeconds;
        this.claims = claims;
    }

    /**
     * Renders the ID Token as a readable "id_token" summary.
     * (A production OIDC provider would encode this as a signed JWT -
     * see Exercise 3 for the JWT signing mechanics.)
     */
    public String render() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"iss\": \"").append(issuer).append("\",\n");
        sb.append("  \"sub\": \"").append(subject).append("\",\n");
        sb.append("  \"aud\": \"").append(audience).append("\",\n");
        sb.append("  \"iat\": ").append(issuedAtEpochSeconds).append(",\n");
        sb.append("  \"exp\": ").append(expiryEpochSeconds).append(",\n");
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            sb.append("  \"").append(entry.getKey()).append("\": \"")
              .append(entry.getValue()).append("\",\n");
        }
        sb.append("}");
        return sb.toString();
    }

    public String getSubject() {
        return subject;
    }
}

/**
 * A short-lived Authorization Code issued after the Resource Owner
 * authenticates and grants consent. It is exchanged for tokens at the
 * token endpoint and can only be used once (single-use, as required by
 * OAuth 2.1).
 */
public class AuthorizationCode {

    private final String code;
    private final String username;
    private final String clientId;
    private final String codeChallenge;   // PKCE: SHA-256(code_verifier), base64url encoded
    private final long expiryEpochMillis;
    private boolean used;

    public AuthorizationCode(String code, String username, String clientId,
                              String codeChallenge, long validForMillis) {
        this.code = code;
        this.username = username;
        this.clientId = clientId;
        this.codeChallenge = codeChallenge;
        this.expiryEpochMillis = System.currentTimeMillis() + validForMillis;
        this.used = false;
    }

    public String getCode() {
        return code;
    }

    public String getUsername() {
        return username;
    }

    public String getClientId() {
        return clientId;
    }

    public String getCodeChallenge() {
        return codeChallenge;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryEpochMillis;
    }

    public boolean isUsed() {
        return used;
    }

    public void markUsed() {
        this.used = true;
    }
}

/**
 * Represents an OAuth 2.1 Access Token used by the client to call
 * protected APIs on behalf of the Resource Owner.
 */
public class AccessToken {

    private final String value;
    private final String username;
    private final String clientId;
    private final String scope;
    private final long expiryEpochMillis;

    public AccessToken(String value, String username, String clientId,
                        String scope, long validForMillis) {
        this.value = value;
        this.username = username;
        this.clientId = clientId;
        this.scope = scope;
        this.expiryEpochMillis = System.currentTimeMillis() + validForMillis;
    }

    public String getValue() {
        return value;
    }

    public String getUsername() {
        return username;
    }

    public String getScope() {
        return scope;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryEpochMillis;
    }
}

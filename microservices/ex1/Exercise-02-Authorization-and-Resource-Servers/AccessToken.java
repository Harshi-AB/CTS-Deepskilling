import java.util.Collections;
import java.util.Set;

/**
 * An opaque Access Token issued by the Authorization Server and later
 * presented to, and validated by, a Resource Server.
 */
public class AccessToken {

    private final String value;
    private final String clientId;
    private final Set<Scope> scopes;
    private final long expiryEpochMillis;
    private boolean revoked;

    public AccessToken(String value, String clientId, Set<Scope> scopes, long validForMillis) {
        this.value = value;
        this.clientId = clientId;
        this.scopes = scopes;
        this.expiryEpochMillis = System.currentTimeMillis() + validForMillis;
        this.revoked = false;
    }

    public String getValue() {
        return value;
    }

    public String getClientId() {
        return clientId;
    }

    public Set<Scope> getScopes() {
        return Collections.unmodifiableSet(scopes);
    }

    public boolean hasScope(Scope required) {
        return scopes.contains(required);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryEpochMillis;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void revoke() {
        this.revoked = true;
    }
}

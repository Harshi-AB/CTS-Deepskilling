import java.util.EnumSet;
import java.util.Set;

/**
 * A client application registered with the Authorization Server,
 * along with the maximum set of scopes it is permitted to request
 * (its "allowed scopes"). This models the configuration step of
 * "Configuring an Authorization Server": deciding, per client,
 * which permissions it may ever be granted.
 */
public class RegisteredClient {

    private final String clientId;
    private final String clientSecret;
    private final Set<Scope> allowedScopes;

    public RegisteredClient(String clientId, String clientSecret, Set<Scope> allowedScopes) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.allowedScopes = EnumSet.copyOf(allowedScopes);
    }

    public String getClientId() {
        return clientId;
    }

    public boolean checkSecret(String candidateSecret) {
        return this.clientSecret.equals(candidateSecret);
    }

    public boolean isScopeAllowed(Scope scope) {
        return allowedScopes.contains(scope);
    }

    public Set<Scope> getAllowedScopes() {
        return allowedScopes;
    }
}

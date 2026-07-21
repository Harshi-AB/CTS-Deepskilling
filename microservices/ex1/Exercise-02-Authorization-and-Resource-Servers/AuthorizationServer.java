import java.security.SecureRandom;
import java.util.Base64;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The Authorization Server (AS).
 *
 * Configuration responsibilities demonstrated here:
 *  - Registering clients together with the scopes each is allowed to request.
 *  - Implementing the OAuth 2.1 Client Credentials grant (service-to-service
 *    auth, no end user involved) to issue scoped Access Tokens.
 *  - Publishing tokens to a shared TokenStore so a separate Resource
 *    Server can validate them (simulating a token-introspection endpoint).
 */
public class AuthorizationServer {

    private static final long ACCESS_TOKEN_TTL_MILLIS = 5 * 60_000; // 5 minutes

    private final Map<String, RegisteredClient> clients = new HashMap<>();
    private final TokenStore tokenStore;
    private final SecureRandom random = new SecureRandom();

    public AuthorizationServer(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    /** Configuration step: register a client with its allowed scopes. */
    public void registerClient(RegisteredClient client) {
        clients.put(client.getClientId(), client);
        System.out.println("[AuthorizationServer] Registered client '" + client.getClientId()
                + "' with allowed scopes " + client.getAllowedScopes());
    }

    /**
     * Client Credentials grant: the client authenticates with its own
     * clientId/clientSecret (no end user) and requests a set of scopes.
     * The server only grants the intersection of requested scopes and
     * the client's configured allowed scopes.
     */
    public AccessToken issueToken(String clientId, String clientSecret, Set<Scope> requestedScopes) {
        RegisteredClient client = clients.get(clientId);
        if (client == null || !client.checkSecret(clientSecret)) {
            throw new SecurityException("Client authentication failed for '" + clientId + "'");
        }

        Set<Scope> grantedScopes = EnumSet.noneOf(Scope.class);
        for (Scope requested : requestedScopes) {
            if (client.isScopeAllowed(requested)) {
                grantedScopes.add(requested);
            } else {
                System.out.println("[AuthorizationServer] Denied scope '" + requested
                        + "' for client '" + clientId + "' (not in allowed scopes)");
            }
        }

        if (grantedScopes.isEmpty()) {
            throw new SecurityException("No requested scopes were granted for client '" + clientId + "'");
        }

        String value = generateRandomToken();
        AccessToken token = new AccessToken(value, clientId, grantedScopes, ACCESS_TOKEN_TTL_MILLIS);
        tokenStore.save(token);

        System.out.println("[AuthorizationServer] Issued token to '" + clientId
                + "' with granted scopes " + grantedScopes);
        return token;
    }

    private String generateRandomToken() {
        byte[] bytes = new byte[24];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}

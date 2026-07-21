import java.util.ArrayList;
import java.util.List;

/**
 * The Resource Server (RS).
 *
 * Responsibilities demonstrated here:
 *  - Hosting protected resources, each guarded by a required scope.
 *  - Validating incoming Access Tokens against the shared TokenStore
 *    (simulating token introspection against the Authorization Server)
 *    before serving any resource: checks the token exists, is not
 *    expired, is not revoked, and carries the scope the resource needs.
 *
 * This is the separation of concerns at the heart of the exercise:
 * the Authorization Server decides WHO gets WHAT scopes; the Resource
 * Server independently ENFORCES those scopes on each request, without
 * ever handling user credentials itself.
 */
public class ResourceServer {

    private final TokenStore tokenStore;
    private final List<ProtectedResource> resources = new ArrayList<>();

    public ResourceServer(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    public void addResource(ProtectedResource resource) {
        resources.add(resource);
    }

    /**
     * Simulates an incoming HTTP request carrying a Bearer token in the
     * Authorization header, targeting a given resource path.
     */
    public String handleRequest(String path, String bearerToken) {
        AccessToken token = tokenStore.find(bearerToken);

        if (token == null) {
            return "401 Unauthorized - token not recognized";
        }
        if (token.isRevoked()) {
            return "401 Unauthorized - token has been revoked";
        }
        if (token.isExpired()) {
            return "401 Unauthorized - token has expired";
        }

        ProtectedResource resource = findResource(path);
        if (resource == null) {
            return "404 Not Found - no such resource: " + path;
        }

        if (!token.hasScope(resource.getRequiredScope())) {
            return "403 Forbidden - token lacks required scope '" + resource.getRequiredScope() + "'";
        }

        return "200 OK - " + resource.getPayload();
    }

    private ProtectedResource findResource(String path) {
        for (ProtectedResource r : resources) {
            if (r.getPath().equals(path)) {
                return r;
            }
        }
        return null;
    }
}

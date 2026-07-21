/**
 * A protected resource exposed by the Resource Server, guarded by a
 * required scope.
 */
public class ProtectedResource {

    private final String path;
    private final Scope requiredScope;
    private final String payload;

    public ProtectedResource(String path, Scope requiredScope, String payload) {
        this.path = path;
        this.requiredScope = requiredScope;
        this.payload = payload;
    }

    public String getPath() {
        return path;
    }

    public Scope getRequiredScope() {
        return requiredScope;
    }

    public String getPayload() {
        return payload;
    }
}

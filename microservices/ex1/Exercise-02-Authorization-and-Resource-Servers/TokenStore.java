import java.util.HashMap;
import java.util.Map;

/**
 * Shared token store used to hand tokens off between the
 * Authorization Server (writer) and the Resource Server (reader).
 * In a real deployment this hand-off happens implicitly - the client
 * carries the token in an HTTP header - but the two servers still
 * need a common source of truth (or a way to validate the token,
 * e.g. via introspection or a shared signing key). This class models
 * that shared trust boundary explicitly.
 */
public class TokenStore {

    private final Map<String, AccessToken> tokens = new HashMap<>();

    public void save(AccessToken token) {
        tokens.put(token.getValue(), token);
    }

    public AccessToken find(String tokenValue) {
        return tokens.get(tokenValue);
    }
}

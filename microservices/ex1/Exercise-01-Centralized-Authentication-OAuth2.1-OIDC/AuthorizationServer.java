import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * The Centralized Authorization Server (also acting as the OpenID
 * Provider).
 *
 * Responsibilities:
 *  1. Register / hold OAuth clients and users (centralized identity store).
 *  2. Authenticate the Resource Owner (username/password).
 *  3. Issue a single-use Authorization Code (with PKCE code_challenge
 *     bound to it), implementing the OAuth 2.1 Authorization Code flow.
 *  4. Exchange a valid Authorization Code (+ matching PKCE code_verifier)
 *     for an Access Token and an OIDC ID Token.
 *
 * Centralizing these responsibilities in one server is exactly what makes
 * this "Centralized Authentication": every relying-party application
 * delegates login to this single trusted authority instead of each
 * maintaining its own username/password store.
 */
public class AuthorizationServer {

    private static final String ISSUER = "https://central-auth.example.com";
    private static final long AUTH_CODE_TTL_MILLIS = 60_000;      // 1 minute
    private static final long ACCESS_TOKEN_TTL_MILLIS = 3_600_000; // 1 hour
    private static final long ID_TOKEN_TTL_SECONDS = 3600;         // 1 hour

    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Client> clients = new HashMap<>();
    private final Map<String, AuthorizationCode> authCodes = new HashMap<>();
    private final SecureRandom random = new SecureRandom();

    public void registerUser(User user) {
        users.put(user.getUsername(), user);
    }

    public void registerClient(Client client) {
        clients.put(client.getClientId(), client);
    }

    /**
     * Step 1: Authentication + Authorization request.
     * The Resource Owner logs in centrally and consents; the server then
     * issues a one-time Authorization Code bound to the PKCE
     * code_challenge supplied by the client.
     */
    public String authorize(String username, String password, String clientId,
                             String redirectUri, String codeChallenge) {
        User user = users.get(username);
        Client client = clients.get(clientId);

        if (user == null || !user.checkPassword(password)) {
            throw new SecurityException("Authentication failed for user: " + username);
        }
        if (client == null) {
            throw new SecurityException("Unknown client: " + clientId);
        }
        if (!client.isRedirectUriValid(redirectUri)) {
            throw new SecurityException("redirect_uri mismatch for client: " + clientId);
        }

        String code = generateRandomToken();
        AuthorizationCode authCode = new AuthorizationCode(
                code, username, clientId, codeChallenge, AUTH_CODE_TTL_MILLIS);
        authCodes.put(code, authCode);

        System.out.println("[AuthorizationServer] Issued authorization code for user '"
                + username + "' -> client '" + clientId + "'");
        return code;
    }

    /**
     * Step 2: Token exchange.
     * The client presents the Authorization Code plus the PKCE
     * code_verifier. The server recomputes SHA-256(code_verifier) and
     * checks it matches the code_challenge stored with the code -
     * this is what prevents authorization-code-interception attacks,
     * mandated by OAuth 2.1.
     */
    public TokenResponse exchangeCodeForTokens(String code, String clientId,
                                                String clientSecret, String codeVerifier) {
        AuthorizationCode authCode = authCodes.get(code);
        Client client = clients.get(clientId);

        if (authCode == null || authCode.isUsed() || authCode.isExpired()) {
            throw new SecurityException("Invalid, expired or already-used authorization code");
        }
        if (client == null || !client.checkSecret(clientSecret)) {
            throw new SecurityException("Client authentication failed");
        }
        if (!authCode.getClientId().equals(clientId)) {
            throw new SecurityException("Authorization code was not issued to this client");
        }
        if (!verifyPkce(codeVerifier, authCode.getCodeChallenge())) {
            throw new SecurityException("PKCE verification failed");
        }

        authCode.markUsed(); // single-use enforcement

        User user = users.get(authCode.getUsername());

        AccessToken accessToken = new AccessToken(
                generateRandomToken(), user.getUsername(), clientId,
                "openid profile", ACCESS_TOKEN_TTL_MILLIS);

        IDToken idToken = new IDToken(
                ISSUER, user.getUsername(), clientId, ID_TOKEN_TTL_SECONDS, user.getClaims());

        System.out.println("[AuthorizationServer] Tokens issued to client '" + clientId + "'");
        return new TokenResponse(accessToken, idToken);
    }

    private boolean verifyPkce(String codeVerifier, String expectedChallenge) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(codeVerifier.getBytes("UTF-8"));
            String computedChallenge = Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
            return computedChallenge.equals(expectedChallenge);
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify PKCE challenge", e);
        }
    }

    private String generateRandomToken() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /**
     * Simple holder for the pair of tokens returned by the token endpoint.
     */
    public static class TokenResponse {
        private final AccessToken accessToken;
        private final IDToken idToken;

        public TokenResponse(AccessToken accessToken, IDToken idToken) {
            this.accessToken = accessToken;
            this.idToken = idToken;
        }

        public AccessToken getAccessToken() {
            return accessToken;
        }

        public IDToken getIdToken() {
            return idToken;
        }
    }
}

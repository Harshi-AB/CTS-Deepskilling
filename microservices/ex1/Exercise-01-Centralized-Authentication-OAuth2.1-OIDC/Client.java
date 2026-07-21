/**
 * Represents an OAuth 2.1 Relying Party / Client application that has been
 * registered with the Authorization Server (Centralized Authentication
 * Server).
 *
 * OAuth 2.1 mandates PKCE for public clients, so every client is expected
 * to send a code_challenge during the authorization request and the
 * matching code_verifier during the token request.
 */
public class Client {

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    public Client(String clientId, String clientSecret, String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    public String getClientId() {
        return clientId;
    }

    public boolean checkSecret(String candidateSecret) {
        return this.clientSecret.equals(candidateSecret);
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public boolean isRedirectUriValid(String candidateUri) {
        return this.redirectUri.equals(candidateUri);
    }
}

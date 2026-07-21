import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Demo entry point: simulates the full OAuth 2.1 Authorization Code
 * flow with PKCE, layered with OpenID Connect, against a single
 * Centralized Authentication Server.
 *
 * Flow demonstrated:
 *   1. Two independent client applications ("HR-Portal", "Payroll-App")
 *      and one user are registered with the central server.
 *   2. The client generates a PKCE code_verifier / code_challenge pair.
 *   3. The user authenticates ONCE at the central server and an
 *      authorization code is issued.
 *   4. The client exchanges the code (+ code_verifier) for an
 *      Access Token and an OIDC ID Token.
 *   5. A tampering attempt (wrong code_verifier) is shown being rejected,
 *      proving PKCE protects the flow.
 */
public class CentralizedAuthenticationDemo {

    public static void main(String[] args) throws Exception {
        AuthorizationServer authServer = new AuthorizationServer();

        // --- Central identity store: one user shared across all apps ---
        User alice = new User("alice", "P@ssw0rd!");
        alice.addClaim("name", "Alice Johnson");
        alice.addClaim("email", "alice.johnson@example.com");
        authServer.registerUser(alice);

        // --- Two independent relying-party applications ---
        Client hrPortal = new Client("hr-portal", "hr-secret", "https://hr.example.com/callback");
        Client payrollApp = new Client("payroll-app", "payroll-secret", "https://payroll.example.com/callback");
        authServer.registerClient(hrPortal);
        authServer.registerClient(payrollApp);

        System.out.println("========================================================");
        System.out.println(" Centralized Authentication with OAuth 2.1 / OIDC - Demo");
        System.out.println("========================================================\n");

        // ---------- Successful login to HR-Portal ----------
        System.out.println(">>> Scenario 1: Alice logs into HR-Portal via Central Auth\n");
        String verifier = generateCodeVerifier();
        String challenge = deriveCodeChallenge(verifier);

        String code = authServer.authorize(
                "alice", "P@ssw0rd!", "hr-portal",
                "https://hr.example.com/callback", challenge);

        AuthorizationServer.TokenResponse tokens = authServer.exchangeCodeForTokens(
                code, "hr-portal", "hr-secret", verifier);

        System.out.println("Access Token : " + tokens.getAccessToken().getValue());
        System.out.println("ID Token (OIDC claims):");
        System.out.println(tokens.getIdToken().render());

        // ---------- Same user, second independent app, single sign-on style login ----------
        System.out.println("\n>>> Scenario 2: Alice logs into Payroll-App via the SAME Central Auth\n");
        String verifier2 = generateCodeVerifier();
        String challenge2 = deriveCodeChallenge(verifier2);

        String code2 = authServer.authorize(
                "alice", "P@ssw0rd!", "payroll-app",
                "https://payroll.example.com/callback", challenge2);

        AuthorizationServer.TokenResponse tokens2 = authServer.exchangeCodeForTokens(
                code2, "payroll-app", "payroll-secret", verifier2);

        System.out.println("Access Token : " + tokens2.getAccessToken().getValue());
        System.out.println("ID Token (OIDC claims):");
        System.out.println(tokens2.getIdToken().render());

        // ---------- Security demonstration: PKCE rejects a mismatched verifier ----------
        System.out.println("\n>>> Scenario 3: Attacker intercepts the code but does not know the real code_verifier\n");
        String verifier3 = generateCodeVerifier();
        String challenge3 = deriveCodeChallenge(verifier3);
        String code3 = authServer.authorize(
                "alice", "P@ssw0rd!", "hr-portal",
                "https://hr.example.com/callback", challenge3);
        try {
            authServer.exchangeCodeForTokens(code3, "hr-portal", "hr-secret", "wrong-verifier-guessed-by-attacker");
        } catch (SecurityException e) {
            System.out.println("Rejected as expected -> " + e.getMessage());
        }

        // ---------- Security demonstration: authorization code is single-use ----------
        System.out.println("\n>>> Scenario 4: Replaying an already-used authorization code\n");
        try {
            authServer.exchangeCodeForTokens(code, "hr-portal", "hr-secret", verifier);
        } catch (SecurityException e) {
            System.out.println("Rejected as expected -> " + e.getMessage());
        }
    }

    /** Generates a cryptographically random PKCE code_verifier. */
    private static String generateCodeVerifier() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /** Derives the PKCE code_challenge (S256 method) from the code_verifier. */
    private static String deriveCodeChallenge(String codeVerifier) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    }
}

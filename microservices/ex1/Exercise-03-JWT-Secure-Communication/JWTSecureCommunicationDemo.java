/**
 * Demo entry point: shows two microservices ("AuthService" and
 * "OrderService") communicating securely using a shared-secret JWT.
 *
 * Flow demonstrated:
 *   1. AuthService issues a signed JWT after "authenticating" a user.
 *   2. The JWT is handed to OrderService (simulating it crossing the
 *      network in an Authorization: Bearer header).
 *   3. OrderService verifies the JWT using the same shared secret and
 *      trusts the claims inside it without another network call back
 *      to AuthService - this is the core value of JWTs for secure,
 *      stateless inter-service communication.
 *   4. Tampering and wrong-secret scenarios show verification failing
 *      safely.
 *   5. An expired-token scenario shows expiry enforcement.
 */
public class JWTSecureCommunicationDemo {

    // Shared secret known only to trusted internal services.
    private static final String SHARED_SECRET = "s3cr3t-key-shared-between-microservices-only";

    public static void main(String[] args) throws Exception {
        System.out.println("=========================================================");
        System.out.println(" Using JSON Web Tokens (JWT) for Secure Communication");
        System.out.println("=========================================================\n");

        // ---------- Scenario 1: AuthService issues a JWT ----------
        System.out.println(">>> Scenario 1: AuthService issues a signed JWT for user 'bob'\n");

        long now = System.currentTimeMillis() / 1000;
        JwtHeader header = new JwtHeader("HS256", "JWT");
        JwtPayload payload = new JwtPayload()
                .setIssuer("auth-service")
                .setSubject("bob")
                .setAudience("order-service")
                .setIssuedAt(now)
                .setExpiration(now + 300) // valid for 5 minutes
                .addClaim("role", "CUSTOMER");

        JwtToken token = JwtUtil.create(header, payload, SHARED_SECRET);
        System.out.println("Signed JWT sent over the network:");
        System.out.println(token.getCompactForm());

        // ---------- Scenario 2: OrderService verifies and trusts the JWT ----------
        System.out.println("\n>>> Scenario 2: OrderService receives the JWT and verifies it\n");
        JwtToken verified = JwtUtil.verifyAndParse(token.getCompactForm(), SHARED_SECRET);
        System.out.println("Signature valid. Trusted claims extracted by OrderService:");
        System.out.println("  subject (user) : " + verified.getPayload().getClaim("sub"));
        System.out.println("  role           : " + verified.getPayload().getClaim("role"));
        System.out.println("  issuer         : " + verified.getPayload().getClaim("iss"));

        // ---------- Scenario 3: Tampered payload is rejected ----------
        System.out.println("\n>>> Scenario 3: An attacker tampers with the payload (e.g. role -> ADMIN)\n");
        String[] segments = token.getCompactForm().split("\\.");
        String tamperedPayload = Base64UrlCodec.encode(
                "{\"iss\":\"auth-service\",\"sub\":\"bob\",\"aud\":\"order-service\",\"role\":\"ADMIN\"}");
        String tamperedToken = segments[0] + "." + tamperedPayload + "." + segments[2];
        try {
            JwtUtil.verifyAndParse(tamperedToken, SHARED_SECRET);
        } catch (SecurityException e) {
            System.out.println("Rejected as expected -> " + e.getMessage());
        }

        // ---------- Scenario 4: Token signed with the wrong secret is rejected ----------
        System.out.println("\n>>> Scenario 4: A service without the correct shared secret tries to verify\n");
        try {
            JwtUtil.verifyAndParse(token.getCompactForm(), "a-completely-different-secret");
        } catch (SecurityException e) {
            System.out.println("Rejected as expected -> " + e.getMessage());
        }

        // ---------- Scenario 5: Expired token is rejected ----------
        System.out.println("\n>>> Scenario 5: An already-expired token is presented\n");
        JwtPayload expiredPayload = new JwtPayload()
                .setIssuer("auth-service")
                .setSubject("bob")
                .setIssuedAt(now - 1000)
                .setExpiration(now - 500); // expired 500 seconds ago
        JwtToken expiredToken = JwtUtil.create(header, expiredPayload, SHARED_SECRET);
        try {
            JwtUtil.verifyAndParse(expiredToken.getCompactForm(), SHARED_SECRET);
        } catch (SecurityException e) {
            System.out.println("Rejected as expected -> " + e.getMessage());
        }
    }
}

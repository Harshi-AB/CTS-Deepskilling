import java.util.EnumSet;
import java.util.Set;

/**
 * Demo entry point: configures an Authorization Server and a separate
 * Resource Server that share a TokenStore, then walks through several
 * client-credentials requests to show scope-based access control in
 * action.
 */
public class AuthorizationResourceServerDemo {

    public static void main(String[] args) {
        TokenStore tokenStore = new TokenStore();

        // ---- Configure the Authorization Server ----
        AuthorizationServer authServer = new AuthorizationServer(tokenStore);

        RegisteredClient orderServiceClient = new RegisteredClient(
                "order-service", "order-secret",
                EnumSet.of(Scope.READ_ORDERS, Scope.WRITE_ORDERS, Scope.READ_PROFILE));

        RegisteredClient reportingServiceClient = new RegisteredClient(
                "reporting-service", "reporting-secret",
                EnumSet.of(Scope.READ_ORDERS)); // read-only, no write/admin

        authServer.registerClient(orderServiceClient);
        authServer.registerClient(reportingServiceClient);

        // ---- Configure the Resource Server ----
        ResourceServer resourceServer = new ResourceServer(tokenStore);
        resourceServer.addResource(new ProtectedResource(
                "/orders", Scope.READ_ORDERS, "[ {\"orderId\":101}, {\"orderId\":102} ]"));
        resourceServer.addResource(new ProtectedResource(
                "/orders/create", Scope.WRITE_ORDERS, "{\"status\":\"order created\"}"));
        resourceServer.addResource(new ProtectedResource(
                "/admin/settings", Scope.ADMIN, "{\"maintenanceMode\":false}"));

        System.out.println("=========================================================");
        System.out.println(" Configuring Authorization Servers and Resource Servers");
        System.out.println("=========================================================\n");

        // ---- Scenario 1: order-service requests full access, gets it ----
        System.out.println(">>> Scenario 1: order-service requests READ_ORDERS + WRITE_ORDERS\n");
        AccessToken orderToken = authServer.issueToken(
                "order-service", "order-secret",
                EnumSet.of(Scope.READ_ORDERS, Scope.WRITE_ORDERS));

        System.out.println("GET  /orders         -> " + resourceServer.handleRequest("/orders", orderToken.getValue()));
        System.out.println("POST /orders/create   -> " + resourceServer.handleRequest("/orders/create", orderToken.getValue()));
        System.out.println("GET  /admin/settings  -> " + resourceServer.handleRequest("/admin/settings", orderToken.getValue()));

        // ---- Scenario 2: reporting-service is read-only; write & admin must be denied ----
        System.out.println("\n>>> Scenario 2: reporting-service requests READ_ORDERS + ADMIN (ADMIN not allowed)\n");
        AccessToken reportingToken = authServer.issueToken(
                "reporting-service", "reporting-secret",
                EnumSet.of(Scope.READ_ORDERS, Scope.ADMIN));

        System.out.println("GET  /orders          -> " + resourceServer.handleRequest("/orders", reportingToken.getValue()));
        System.out.println("POST /orders/create    -> " + resourceServer.handleRequest("/orders/create", reportingToken.getValue()));

        // ---- Scenario 3: Resource Server rejects an unrecognized token ----
        System.out.println("\n>>> Scenario 3: Resource Server receives a forged/unknown token\n");
        System.out.println("GET  /orders (forged) -> " + resourceServer.handleRequest("/orders", "forged-token-value"));

        // ---- Scenario 4: Revoked token is rejected even though it "exists" ----
        System.out.println("\n>>> Scenario 4: Authorization Server revokes order-service's token\n");
        orderToken.revoke();
        System.out.println("GET  /orders (revoked) -> " + resourceServer.handleRequest("/orders", orderToken.getValue()));

        // ---- Scenario 5: Wrong client secret is rejected at the Authorization Server ----
        System.out.println("\n>>> Scenario 5: Client presents the wrong secret\n");
        try {
            authServer.issueToken("order-service", "wrong-secret", EnumSet.of(Scope.READ_ORDERS));
        } catch (SecurityException e) {
            System.out.println("Rejected as expected -> " + e.getMessage());
        }
    }
}

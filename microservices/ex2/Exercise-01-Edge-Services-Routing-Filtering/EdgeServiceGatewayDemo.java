/**
 * EdgeServiceGatewayDemo
 * -----------------------
 * Plain-Java re-creation of "Exercise 1: Implementing Edge Services for
 * Routing and Filtering" (originally built with Spring Boot 3 +
 * Spring Cloud Gateway).
 *
 * What used to live in application.properties:
 *
 *   spring.cloud.gateway.routes[0].id=example_route
 *   spring.cloud.gateway.routes[0].uri=http://example.org
 *   spring.cloud.gateway.routes[0].predicates[0]=Path=/example/**
 *
 * is now configured programmatically on the Router below, and the
 * @Component LoggingFilter (a GlobalFilter) is registered on the same
 * Router as a GatewayFilter.
 *
 * Run this class to see requests being routed and logged.
 */
public class EdgeServiceGatewayDemo {

    public static void main(String[] args) {

        // 1. Create the router (our in-memory API Gateway).
        Router router = new Router();

        // 2. Configure routing, equivalent to application.properties.
        router.addRoute(new Route("example_route", "http://example.org", "/example/**"));

        // 3. Register the global logging filter (equivalent to @Component + GlobalFilter).
        router.addFilter(new LoggingFilter());

        System.out.println("===== Edge Service Gateway started =====");
        System.out.println();

        // 4. Simulate a few incoming requests to test routing + filtering.
        String[] incomingRequests = {
                "/example/products",
                "/example/orders/42",
                "/unknown/path"
        };

        for (String uri : incomingRequests) {
            System.out.println("---- Incoming request: " + uri + " ----");
            RequestContext context = new RequestContext(uri);
            router.route(context);
            System.out.println();
        }

        System.out.println("===== Edge Service Gateway finished handling requests =====");
    }
}

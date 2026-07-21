/**
 * ApiGatewayRoute is the plain-Java equivalent of the routing entry:
 *
 *   spring.cloud.gateway.routes[0].id=load_balanced_route
 *   spring.cloud.gateway.routes[0].uri=lb://example-service
 *   spring.cloud.gateway.routes[0].predicates[0]=Path=/loadbalanced/**
 *
 * The "lb://" scheme tells the gateway that the destination host/port
 * must be resolved dynamically through a LoadBalancer rather than being
 * a fixed address.
 */
public class ApiGatewayRoute {

    private static final String LB_SCHEME_PREFIX = "lb://";

    private final String id;
    private final String uri;          // e.g. "lb://example-service"
    private final String pathPredicate; // e.g. "/loadbalanced/**"

    public ApiGatewayRoute(String id, String uri, String pathPredicate) {
        this.id = id;
        this.uri = uri;
        this.pathPredicate = pathPredicate;
    }

    public String getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public boolean isLoadBalanced() {
        return uri.startsWith(LB_SCHEME_PREFIX);
    }

    /**
     * Extracts the logical service id from a "lb://service-name" uri.
     */
    public String getServiceId() {
        if (!isLoadBalanced()) {
            throw new IllegalStateException("Route '" + id + "' is not a load-balanced (lb://) route.");
        }
        return uri.substring(LB_SCHEME_PREFIX.length());
    }

    public boolean matches(String requestUri) {
        if (pathPredicate.endsWith("/**")) {
            String prefix = pathPredicate.substring(0, pathPredicate.length() - 3);
            return requestUri.startsWith(prefix);
        }
        return pathPredicate.equals(requestUri);
    }

    @Override
    public String toString() {
        return "ApiGatewayRoute{id='" + id + "', uri='" + uri + "', predicate='" + pathPredicate + "'}";
    }
}

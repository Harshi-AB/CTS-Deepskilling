/**
 * Route represents a single routing rule, equivalent to one entry under
 * spring.cloud.gateway.routes[n] in application.properties:
 *
 *   spring.cloud.gateway.routes[0].id=example_route
 *   spring.cloud.gateway.routes[0].uri=http://example.org
 *   spring.cloud.gateway.routes[0].predicates[0]=Path=/example/**
 *
 * It stores an id, a destination URI and a simple Ant-style path
 * predicate (supporting a trailing "/**" wildcard, just like the
 * Spring Cloud Gateway "Path" predicate).
 */
public class Route {

    private final String id;
    private final String uri;
    private final String pathPredicate;

    public Route(String id, String uri, String pathPredicate) {
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

    public String getPathPredicate() {
        return pathPredicate;
    }

    /**
     * Very small re-implementation of Spring Cloud Gateway's "Path" predicate.
     * Supports:
     *   "/example/**" -> matches "/example/" followed by anything
     *   "/example"    -> matches exactly "/example"
     */
    public boolean matches(String requestUri) {
        if (pathPredicate.endsWith("/**")) {
            String prefix = pathPredicate.substring(0, pathPredicate.length() - 3);
            return requestUri.startsWith(prefix);
        }
        return pathPredicate.equals(requestUri);
    }

    @Override
    public String toString() {
        return "Route{id='" + id + "', uri='" + uri + "', predicate='" + pathPredicate + "'}";
    }
}

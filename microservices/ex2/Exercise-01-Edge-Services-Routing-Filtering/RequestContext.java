import java.util.HashMap;
import java.util.Map;

/**
 * RequestContext simulates an incoming HTTP request as it travels
 * through the Edge Service (this plays the role of Spring's
 * ServerWebExchange in a plain, dependency-free way).
 *
 * It carries the request URI, an arbitrary attribute bag (so filters
 * can pass data along the chain) and, once a route has been matched,
 * a reference to that Route.
 */
public class RequestContext {

    private final String requestUri;
    private final Map<String, Object> attributes;
    private Route matchedRoute;

    public RequestContext(String requestUri) {
        this.requestUri = requestUri;
        this.attributes = new HashMap<>();
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public Route getMatchedRoute() {
        return matchedRoute;
    }

    public void setMatchedRoute(Route matchedRoute) {
        this.matchedRoute = matchedRoute;
    }
}

import java.util.ArrayList;
import java.util.List;

/**
 * Router is the plain-Java equivalent of Spring Cloud Gateway's routing
 * engine. It keeps a list of configured Route objects (normally read from
 * application.properties) and a list of global filters (normally
 * discovered via @Component scanning).
 *
 * For every incoming request it:
 *   1. Finds the first Route whose predicate matches the request path.
 *   2. Builds a filter chain out of the registered GatewayFilters.
 *   3. Runs the chain; the final link in the chain "forwards" the
 *      request to the matched route's destination URI.
 */
public class Router {

    private final List<Route> routes = new ArrayList<>();
    private final List<GatewayFilter> filters = new ArrayList<>();

    public void addRoute(Route route) {
        routes.add(route);
    }

    public void addFilter(GatewayFilter filter) {
        filters.add(filter);
    }

    /**
     * Routes and filters a single request, mimicking what Spring Cloud
     * Gateway's DispatcherHandler + GatewayFilterChain do internally.
     */
    public void route(RequestContext context) {
        Route match = findMatchingRoute(context.getRequestUri());

        if (match == null) {
            System.out.println("[Router] 404 - No route matched for: " + context.getRequestUri());
            return;
        }

        context.setMatchedRoute(match);

        // Build the chain recursively, last filter -> forwarding handler.
        FilterChain chain = buildChain(0, this::forwardToDestination);
        chain.doFilter(context);
    }

    private Route findMatchingRoute(String requestUri) {
        for (Route route : routes) {
            if (route.matches(requestUri)) {
                return route;
            }
        }
        return null;
    }

    /**
     * Recursively builds a FilterChain so that filters execute in
     * registration order, each one able to run logic both before and
     * after calling chain.doFilter(context).
     */
    private FilterChain buildChain(int index, FilterChain finalHandler) {
        if (index >= filters.size()) {
            return finalHandler;
        }
        GatewayFilter currentFilter = filters.get(index);
        FilterChain nextChain = buildChain(index + 1, finalHandler);
        return context -> currentFilter.filter(context, nextChain);
    }

    /**
     * The terminal step of the chain: simulates forwarding the request
     * to the destination service configured on the matched route.
     */
    private void forwardToDestination(RequestContext context) {
        Route route = context.getMatchedRoute();
        System.out.println("[Router] Forwarding \"" + context.getRequestUri()
                + "\" -> " + route.getUri() + "  (matched route id='" + route.getId() + "')");
    }
}

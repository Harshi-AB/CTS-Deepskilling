/**
 * FilterChain is the plain-Java equivalent of Spring Cloud Gateway's
 * GatewayFilterChain. Calling doFilter(context) passes control to the
 * next filter in the chain (or to the final routing handler once all
 * filters have run).
 */
public interface FilterChain {
    void doFilter(RequestContext context);
}

/**
 * GatewayFilter is the plain-Java equivalent of Spring Cloud Gateway's
 * GlobalFilter interface. Any cross-cutting concern (logging, security,
 * header manipulation, etc.) is implemented by creating a class that
 * implements this interface.
 */
public interface GatewayFilter {

    /**
     * @param context the current request context
     * @param chain   the remaining chain; implementations MUST call
     *                chain.doFilter(context) to continue processing,
     *                unless they intentionally want to short-circuit
     *                the request (e.g. reject it).
     */
    void filter(RequestContext context, FilterChain chain);
}

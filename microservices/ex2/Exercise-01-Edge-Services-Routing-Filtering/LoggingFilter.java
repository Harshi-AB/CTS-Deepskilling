/**
 * LoggingFilter is the plain-Java equivalent of the Spring @Component:
 *
 *   @Component
 *   public class LoggingFilter implements GlobalFilter {
 *       @Override
 *       public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
 *           System.out.println("Request: " + exchange.getRequest().getURI());
 *           return chain.filter(exchange);
 *       }
 *   }
 *
 * It logs every incoming request URI before allowing the chain to continue.
 */
public class LoggingFilter implements GatewayFilter {

    @Override
    public void filter(RequestContext context, FilterChain chain) {
        System.out.println("[LoggingFilter] Request: " + context.getRequestUri());
        // Continue the chain, just like calling chain.filter(exchange) in Spring.
        chain.doFilter(context);
    }
}

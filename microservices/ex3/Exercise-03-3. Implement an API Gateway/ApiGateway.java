import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * ApiGateway is the single entry point for all client traffic, playing
 * the same role as Spring Cloud Gateway in a real deployment. It
 * performs three cross-cutting concerns before/while routing a request
 * to the correct backend microservice:
 *
 *   1. RATE LIMITING - each client gets a token bucket (see
 *      RateLimiter); once exhausted, requests receive HTTP 429.
 *   2. CACHING - GET responses are cached for a short TTL (see
 *      ResponseCache) so repeated requests for the same resource don't
 *      have to hit the backend service again.
 *   3. PATH REWRITING - external, gateway-facing paths such as
 *      "/gateway/customers/1" are rewritten to the internal path the
 *      backend service actually expects, "/api/customers/1", and
 *      dispatched to the correct backend base URL.
 *
 * Routing table (path prefix -> backend base URL):
 *   /gateway/customers/**  -> CustomerService  (rewritten to /api/customers/**)
 *   /gateway/billing/**    -> BillingService   (rewritten to /api/billing/**)
 */
public class ApiGateway {

    private final int port;
    private final String customerServiceBaseUrl;
    private final String billingServiceBaseUrl;
    private final RateLimiter rateLimiter;
    private final ResponseCache responseCache;
    private HttpServer server;

    public ApiGateway(int port, String customerServiceBaseUrl, String billingServiceBaseUrl) {
        this.port = port;
        this.customerServiceBaseUrl = customerServiceBaseUrl;
        this.billingServiceBaseUrl = billingServiceBaseUrl;
        // Allow bursts of 3 requests, refilling at 1 token/second per client
        this.rateLimiter = new RateLimiter(3, 1.0);
        // Cache GET responses for 5 seconds
        this.responseCache = new ResponseCache(5000);
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/gateway", new GatewayHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("[ApiGateway] Started on http://localhost:" + port + "/gateway");
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    private class GatewayHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String incomingPath = exchange.getRequestURI().getPath();
            String clientKey = exchange.getRemoteAddress().getAddress().getHostAddress();

            // ---- 1. RATE LIMITING ----
            if (!rateLimiter.tryConsume(clientKey)) {
                System.out.println("[ApiGateway] Rate limit exceeded for client " + clientKey
                        + " on " + incomingPath);
                sendResponse(exchange, 429,
                        "{\"error\":\"Too Many Requests - rate limit exceeded\"}");
                return;
            }

            // ---- 2. CACHING (GET requests only) ----
            boolean isGet = "GET".equalsIgnoreCase(exchange.getRequestMethod());
            String cacheKey = clientAgnosticCacheKey(incomingPath);
            if (isGet) {
                String cached = responseCache.get(cacheKey);
                if (cached != null) {
                    System.out.println("[ApiGateway] Cache HIT for " + incomingPath);
                    sendResponse(exchange, 200, cached);
                    return;
                }
            }

            // ---- 3. PATH REWRITING + ROUTING ----
            RouteResult route = rewriteAndRoute(incomingPath);
            if (route == null) {
                sendResponse(exchange, 404, "{\"error\":\"No route matches " + incomingPath + "\"}");
                return;
            }

            System.out.println("[ApiGateway] Routing " + incomingPath + " -> " + route.targetUrl
                    + " (path rewritten)");
            String backendResponse = HttpClientHelper.get(route.targetUrl);

            if (backendResponse == null) {
                sendResponse(exchange, 502, "{\"error\":\"Bad Gateway - backend service unreachable\"}");
                return;
            }

            if (isGet) {
                responseCache.put(cacheKey, backendResponse);
            }
            sendResponse(exchange, 200, backendResponse);
        }

        /** Cache key that ignores the client, so all clients share cached GET results. */
        private String clientAgnosticCacheKey(String path) {
            return "GET:" + path;
        }

        /**
         * Rewrites an external gateway path into the internal backend path and
         * resolves which backend service base URL it should be sent to.
         */
        private RouteResult rewriteAndRoute(String incomingPath) {
            if (incomingPath.startsWith("/gateway/customers")) {
                String rewrittenPath = incomingPath.replaceFirst("/gateway/customers", "/api/customers");
                return new RouteResult(customerServiceBaseUrl + rewrittenPath);
            } else if (incomingPath.startsWith("/gateway/billing")) {
                String rewrittenPath = incomingPath.replaceFirst("/gateway/billing", "/api/billing");
                return new RouteResult(billingServiceBaseUrl + rewrittenPath);
            }
            return null;
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String body) throws IOException {
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(statusCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    /** Simple holder for the fully-resolved backend URL after rewriting/routing. */
    private static class RouteResult {
        final String targetUrl;

        RouteResult(String targetUrl) {
            this.targetUrl = targetUrl;
        }
    }
}

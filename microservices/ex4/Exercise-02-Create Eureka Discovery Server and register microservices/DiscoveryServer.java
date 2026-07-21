import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * DiscoveryServer
 * ----------------------------------------------------------------------
 * A core-Java simulation of a Eureka-style Service Discovery Server.
 *
 * Since the real Spring Cloud Netflix Eureka Server requires Maven and
 * Spring dependencies (which are not allowed here), this class re-creates
 * its essential behaviour using only core Java (com.sun.net.httpserver):
 *
 *   - Maintains an in-memory REGISTRY of service-name -> list of instance URLs
 *   - Lets a microservice REGISTER itself     : POST /eureka/apps/register
 *   - Lets a microservice DE-REGISTER itself  : POST /eureka/apps/deregister
 *   - Lets anyone VIEW the registry           : GET  /eureka/apps
 *
 * Run this FIRST (it plays the role of the Eureka server on port 8761,
 * matching the port used in the exercise's application.properties).
 * ----------------------------------------------------------------------
 */
public class DiscoveryServer {

    private static final int PORT = 8761;

    // Service registry: serviceName (e.g. "account-service") -> list of base URLs.
    // ConcurrentHashMap / CopyOnWriteArrayList are used because multiple
    // microservices may register concurrently.
    private static final Map<String, List<String>> REGISTRY = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/eureka/apps/register", new RegisterHandler());
        server.createContext("/eureka/apps/deregister", new DeregisterHandler());
        server.createContext("/eureka/apps", new ListHandler());
        server.setExecutor(null);
        server.start();

        System.out.println("========================================================");
        System.out.println(" Discovery Server (Eureka simulation) started on port " + PORT);
        System.out.println(" Registry view : http://localhost:" + PORT + "/eureka/apps");
        System.out.println("========================================================");
    }

    /**
     * POST /eureka/apps/register?service=account-service&url=http://localhost:8080
     * Registers a service instance URL under a service name.
     */
    static class RegisterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "{\"error\":\"Use POST to register.\"}");
                return;
            }

            Map<String, String> params = parseQuery(exchange.getRequestURI().getQuery());
            String service = params.get("service");
            String url = params.get("url");

            if (service == null || url == null) {
                sendResponse(exchange, 400, "{\"error\":\"'service' and 'url' query params are required.\"}");
                return;
            }

            REGISTRY.computeIfAbsent(service.toUpperCase(), k -> new CopyOnWriteArrayList<>());
            List<String> instances = REGISTRY.get(service.toUpperCase());
            if (!instances.contains(url)) {
                instances.add(url);
            }

            System.out.println("[Discovery] Registered " + service + " -> " + url);
            sendResponse(exchange, 200, "{\"status\":\"REGISTERED\",\"service\":\"" + service + "\",\"url\":\"" + url + "\"}");
        }
    }

    /**
     * POST /eureka/apps/deregister?service=account-service&url=http://localhost:8080
     * Removes a service instance from the registry.
     */
    static class DeregisterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "{\"error\":\"Use POST to deregister.\"}");
                return;
            }

            Map<String, String> params = parseQuery(exchange.getRequestURI().getQuery());
            String service = params.get("service");
            String url = params.get("url");

            if (service != null && url != null) {
                List<String> instances = REGISTRY.get(service.toUpperCase());
                if (instances != null) {
                    instances.remove(url);
                }
            }

            System.out.println("[Discovery] Deregistered " + service + " -> " + url);
            sendResponse(exchange, 200, "{\"status\":\"DEREGISTERED\"}");
        }
    }

    /**
     * GET /eureka/apps
     * Returns the full registry as JSON, similar to the "Instances currently
     * registered with Eureka" table on the real Eureka dashboard.
     */
    static class ListHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "{\"error\":\"Use GET to list the registry.\"}");
                return;
            }

            StringBuilder json = new StringBuilder("{\"applications\":[");
            boolean firstApp = true;
            for (Map.Entry<String, List<String>> entry : REGISTRY.entrySet()) {
                if (entry.getValue().isEmpty()) continue;
                if (!firstApp) json.append(",");
                firstApp = false;

                json.append("{\"name\":\"").append(entry.getKey()).append("\",\"instances\":[");
                boolean firstUrl = true;
                for (String url : entry.getValue()) {
                    if (!firstUrl) json.append(",");
                    firstUrl = false;
                    json.append("\"").append(url).append("\"");
                }
                json.append("]}");
            }
            json.append("]}");

            sendResponse(exchange, 200, json.toString());
        }
    }

    /** Parses a query string like "service=account-service&url=http://localhost:8080". */
    private static Map<String, String> parseQuery(String query) {
        Map<String, String> params = new ConcurrentHashMap<>();
        if (query == null || query.isEmpty()) return params;

        for (String pair : query.split("&")) {
            int idx = pair.indexOf('=');
            if (idx > 0) {
                String key = java.net.URLDecoder.decode(pair.substring(0, idx), java.nio.charset.StandardCharsets.UTF_8);
                String value = java.net.URLDecoder.decode(pair.substring(idx + 1), java.nio.charset.StandardCharsets.UTF_8);
                params.put(key, value);
            }
        }
        return params;
    }

    private static void sendResponse(HttpExchange exchange, int statusCode, String body) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        byte[] bytes = body.getBytes("UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}

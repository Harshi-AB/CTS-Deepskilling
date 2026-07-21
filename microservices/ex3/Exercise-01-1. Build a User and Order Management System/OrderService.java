import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * OrderService is a standalone REST microservice responsible for managing
 * Order resources. It exposes:
 *
 *   GET  /orders          -> list all orders
 *   GET  /orders/{id}      -> fetch a single order, enriched with the
 *                            placing user's details
 *   POST /orders          -> place a new order (JSON body: userId, product, quantity)
 *
 * To fulfil a request for a single order, this service calls the remote
 * User Service over HTTP (via HttpClientHelper) to fetch the user who
 * placed the order - this is the equivalent of using Spring WebFlux's
 * WebClient or an OpenFeign client to talk to another microservice.
 */
public class OrderService {

    private final Map<Integer, Order> repository = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(0);
    private final int port;
    private final String userServiceBaseUrl;
    private HttpServer server;

    public OrderService(int port, String userServiceBaseUrl) {
        this.port = port;
        this.userServiceBaseUrl = userServiceBaseUrl;
    }

    /** Seeds a couple of sample orders so the demo has data to work with. */
    public void seedData() {
        placeOrder(1, "Wireless Mouse", 2);
        placeOrder(2, "Mechanical Keyboard", 1);
    }

    /** Places (creates) a new order for the given user. */
    public Order placeOrder(int userId, String product, int quantity) {
        int id = idGenerator.incrementAndGet();
        Order order = new Order(id, userId, product, quantity, "PLACED");
        repository.put(id, order);
        return order;
    }

    public Order findById(int id) {
        return repository.get(id);
    }

    public Collection<Order> findAll() {
        return repository.values();
    }

    /**
     * Calls the remote User Service to fetch full user details for the
     * given userId. Returns null if the user cannot be found or the
     * service is unreachable.
     */
    private User fetchUser(int userId) {
        String json = HttpClientHelper.get(userServiceBaseUrl + "/users/" + userId);
        if (json == null || json.isEmpty() || json.contains("error")) {
            return null;
        }
        Map<String, String> fields = JsonUtil.fromJson(json);
        try {
            int id = Integer.parseInt(fields.get("id"));
            return new User(id, fields.get("name"), fields.get("email"));
        } catch (Exception e) {
            return null;
        }
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/orders", new OrdersHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("[OrderService] Started on http://localhost:" + port + "/orders");
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    private class OrdersHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            try {
                if ("GET".equalsIgnoreCase(method)) {
                    handleGet(exchange, path);
                } else if ("POST".equalsIgnoreCase(method)) {
                    handlePost(exchange);
                } else {
                    sendResponse(exchange, 405, "{\"error\":\"Method Not Allowed\"}");
                }
            } catch (Exception e) {
                sendResponse(exchange, 500, "{\"error\":\"" + e.getMessage() + "\"}");
            }
        }

        private void handleGet(HttpExchange exchange, String path) throws IOException {
            String[] segments = path.split("/");
            if (segments.length <= 2) {
                // GET /orders -> list all (without user enrichment, for speed)
                StringBuilder sb = new StringBuilder("[");
                boolean first = true;
                for (Order order : findAll()) {
                    if (!first) {
                        sb.append(",");
                    }
                    sb.append(order.toJson());
                    first = false;
                }
                sb.append("]");
                sendResponse(exchange, 200, sb.toString());
            } else {
                // GET /orders/{id} -> single order enriched with user details
                int id = Integer.parseInt(segments[2]);
                Order order = findById(id);
                if (order == null) {
                    sendResponse(exchange, 404, "{\"error\":\"Order not found\"}");
                    return;
                }
                User user = fetchUser(order.getUserId());
                sendResponse(exchange, 200, order.toJsonWithUser(user));
            }
        }

        private void handlePost(HttpExchange exchange) throws IOException {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Map<String, String> fields = JsonUtil.fromJson(body);
            int userId = Integer.parseInt(fields.getOrDefault("userId", "0"));
            String product = fields.getOrDefault("product", "Unknown Product");
            int quantity = Integer.parseInt(fields.getOrDefault("quantity", "1"));
            Order order = placeOrder(userId, product, quantity);
            sendResponse(exchange, 201, order.toJson());
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
}

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * InventoryService is a standalone REST microservice that tracks stock
 * levels for every product. Rather than storing its own copy of the
 * product data, it DISCOVERS the ProductService's current address
 * through the ServiceRegistry (the Eureka stand-in) on every call, and
 * reads its business thresholds (e.g. what counts as "low stock") from
 * the ConfigServer (the Spring Cloud Config Server stand-in) instead of
 * hardcoding them.
 *
 * Endpoints:
 *   GET  /inventory              -> list all products with a low-stock flag
 *   GET  /inventory/{id}/check    -> check whether one product is low on stock
 *   POST /inventory/{id}/restock  -> restock a product using the centrally
 *                                    configured default restock quantity
 */
public class InventoryService {

    public static final String SERVICE_NAME = "INVENTORY-SERVICE";

    private final int port;
    private HttpServer server;

    public InventoryService(int port) {
        this.port = port;
    }

    /** Resolves the ProductService's current address via service discovery. */
    private String discoverProductServiceUrl() {
        return ServiceRegistry.getInstance().discover(ProductService.SERVICE_NAME);
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/inventory", new InventoryHandler());
        server.setExecutor(null);
        server.start();

        String baseUrl = "http://localhost:" + port;
        ServiceRegistry.getInstance().register(SERVICE_NAME, baseUrl);
        System.out.println("[InventoryService] Started on " + baseUrl + "/inventory");
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
        ServiceRegistry.getInstance().deregister(SERVICE_NAME);
    }

    private class InventoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            try {
                String productServiceUrl = discoverProductServiceUrl();
                if (productServiceUrl == null) {
                    sendResponse(exchange, 503,
                            "{\"error\":\"PRODUCT-SERVICE is not available in ServiceRegistry\"}");
                    return;
                }

                if ("GET".equalsIgnoreCase(method) && path.endsWith("/check")) {
                    handleLowStockCheck(exchange, path, productServiceUrl);
                } else if ("GET".equalsIgnoreCase(method)) {
                    handleListInventory(exchange, productServiceUrl);
                } else if ("POST".equalsIgnoreCase(method) && path.endsWith("/restock")) {
                    handleRestock(exchange, path, productServiceUrl);
                } else {
                    sendResponse(exchange, 405, "{\"error\":\"Method Not Allowed\"}");
                }
            } catch (Exception e) {
                sendResponse(exchange, 500, "{\"error\":\"" + e.getMessage() + "\"}");
            }
        }

        private void handleListInventory(HttpExchange exchange, String productServiceUrl) throws IOException {
            String json = HttpClientHelper.get(productServiceUrl + "/products");
            if (json == null) {
                sendResponse(exchange, 502, "{\"error\":\"Could not reach PRODUCT-SERVICE\"}");
                return;
            }
            int threshold = ConfigServer.getInstance().getInt("inventory.low-stock-threshold", 5);
            // Annotate the raw product list with a "lowStock" summary line
            String annotated = "{\"lowStockThreshold\":" + threshold + ",\"products\":" + json + "}";
            sendResponse(exchange, 200, annotated);
        }

        private void handleLowStockCheck(HttpExchange exchange, String path, String productServiceUrl)
                throws IOException {
            String[] segments = path.split("/");
            int id = Integer.parseInt(segments[2]);

            String json = HttpClientHelper.get(productServiceUrl + "/products/" + id);
            if (json == null || json.contains("error")) {
                sendResponse(exchange, 404, "{\"error\":\"Product not found\"}");
                return;
            }
            Map<String, String> fields = JsonUtil.fromJson(json);
            int stock = Integer.parseInt(fields.get("stock"));
            int threshold = ConfigServer.getInstance().getInt("inventory.low-stock-threshold", 5);
            boolean lowStock = stock <= threshold;

            String result = "{\"productId\":" + id + ",\"stock\":" + stock
                    + ",\"threshold\":" + threshold + ",\"lowStock\":" + lowStock + "}";
            sendResponse(exchange, 200, result);
        }

        private void handleRestock(HttpExchange exchange, String path, String productServiceUrl) throws IOException {
            String[] segments = path.split("/");
            int id = Integer.parseInt(segments[2]);

            int restockQuantity = ConfigServer.getInstance()
                    .getInt("inventory.default-restock-quantity", 20);

            String requestBody = "{\"delta\":" + restockQuantity + "}";
            String json = HttpClientHelper.post(productServiceUrl + "/products/" + id + "/stock", requestBody);

            if (json == null || json.contains("error")) {
                sendResponse(exchange, 404, "{\"error\":\"Product not found or restock failed\"}");
                return;
            }
            sendResponse(exchange, 200, json);
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

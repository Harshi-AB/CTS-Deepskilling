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
 * ProductService is a standalone REST microservice that manages the
 * product catalogue and each product's current stock count.
 *
 * On startup it REGISTERS itself with the ServiceRegistry under the
 * logical name "PRODUCT-SERVICE", exactly as a real service would
 * register with a Eureka server, so that other services (such as
 * InventoryService) can find it without a hardcoded URL.
 *
 * Endpoints:
 *   GET  /products                  -> list all products
 *   GET  /products/{id}              -> fetch a single product
 *   POST /products/{id}/stock       -> adjust stock (JSON body: {"delta": n})
 */
public class ProductService {

    public static final String SERVICE_NAME = "PRODUCT-SERVICE";

    private final Map<Integer, Product> repository = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(0);
    private final int port;
    private HttpServer server;

    public ProductService(int port) {
        this.port = port;
    }

    public void seedData() {
        createProduct("Wireless Mouse", 19.99, 50);
        createProduct("Mechanical Keyboard", 59.99, 30);
        createProduct("4K Monitor", 249.99, 3); // intentionally low stock for demo
    }

    public Product createProduct(String name, double price, int stock) {
        int id = idGenerator.incrementAndGet();
        Product product = new Product(id, name, price, stock);
        repository.put(id, product);
        return product;
    }

    public Product findById(int id) {
        return repository.get(id);
    }

    public Collection<Product> findAll() {
        return repository.values();
    }

    /** Adjusts a product's stock by the given delta (can be negative). */
    public Product adjustStock(int id, int delta) {
        Product product = repository.get(id);
        if (product == null) {
            return null;
        }
        int newStock = Math.max(0, product.getStock() + delta);
        product.setStock(newStock);
        return product;
    }

    /** Starts the HTTP server and registers this service with the registry. */
    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/products", new ProductsHandler());
        server.setExecutor(null);
        server.start();

        String baseUrl = "http://localhost:" + port;
        ServiceRegistry.getInstance().register(SERVICE_NAME, baseUrl);
        System.out.println("[ProductService] Started on " + baseUrl + "/products");
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
        ServiceRegistry.getInstance().deregister(SERVICE_NAME);
    }

    private class ProductsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            try {
                if ("GET".equalsIgnoreCase(method)) {
                    handleGet(exchange, path);
                } else if ("POST".equalsIgnoreCase(method) && path.endsWith("/stock")) {
                    handleStockAdjustment(exchange, path);
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
                StringBuilder sb = new StringBuilder("[");
                boolean first = true;
                for (Product product : findAll()) {
                    if (!first) {
                        sb.append(",");
                    }
                    sb.append(product.toJson());
                    first = false;
                }
                sb.append("]");
                sendResponse(exchange, 200, sb.toString());
            } else {
                int id = Integer.parseInt(segments[2]);
                Product product = findById(id);
                if (product == null) {
                    sendResponse(exchange, 404, "{\"error\":\"Product not found\"}");
                } else {
                    sendResponse(exchange, 200, product.toJson());
                }
            }
        }

        private void handleStockAdjustment(HttpExchange exchange, String path) throws IOException {
            // Path looks like /products/{id}/stock
            String[] segments = path.split("/");
            int id = Integer.parseInt(segments[2]);

            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Map<String, String> fields = JsonUtil.fromJson(body);
            int delta = Integer.parseInt(fields.getOrDefault("delta", "0"));

            Product updated = adjustStock(id, delta);
            if (updated == null) {
                sendResponse(exchange, 404, "{\"error\":\"Product not found\"}");
            } else {
                sendResponse(exchange, 200, updated.toJson());
            }
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

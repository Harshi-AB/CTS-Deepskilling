import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CustomerService is a backend microservice that the API Gateway routes
 * requests to. It exposes:
 *
 *   GET /api/customers        -> list all customers
 *   GET /api/customers/{id}    -> fetch a single customer
 *
 * Client applications never call this service directly in a real
 * deployment - they go through the API Gateway, which is what
 * ApiGatewaySystem (the Main class) demonstrates.
 */
public class CustomerService {

    private final Map<Integer, Customer> repository = new ConcurrentHashMap<>();
    private final int port;
    private HttpServer server;

    public CustomerService(int port) {
        this.port = port;
    }

    public void seedData() {
        repository.put(1, new Customer(1, "Alice Johnson", "Chennai"));
        repository.put(2, new Customer(2, "Bob Smith", "Bengaluru"));
        repository.put(3, new Customer(3, "Charlie Davis", "Hyderabad"));
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/customers", new CustomersHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("[CustomerService] Started on http://localhost:" + port + "/api/customers");
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    private class CustomersHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            try {
                String[] segments = path.split("/");
                // /api/customers            -> segments = ["", "api", "customers"]
                // /api/customers/{id}         -> segments length 4
                if (segments.length <= 3) {
                    StringBuilder sb = new StringBuilder("[");
                    boolean first = true;
                    for (Customer c : repository.values()) {
                        if (!first) {
                            sb.append(",");
                        }
                        sb.append(c.toJson());
                        first = false;
                    }
                    sb.append("]");
                    sendResponse(exchange, 200, sb.toString());
                } else {
                    int id = Integer.parseInt(segments[3]);
                    Customer customer = repository.get(id);
                    if (customer == null) {
                        sendResponse(exchange, 404, "{\"error\":\"Customer not found\"}");
                    } else {
                        sendResponse(exchange, 200, customer.toJson());
                    }
                }
            } catch (Exception e) {
                sendResponse(exchange, 500, "{\"error\":\"" + e.getMessage() + "\"}");
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

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
 * BillingService is a backend microservice that the API Gateway routes
 * requests to. It exposes:
 *
 *   GET /api/billing        -> list all bills
 *   GET /api/billing/{id}    -> fetch a single bill
 */
public class BillingService {

    private final Map<Integer, Bill> repository = new ConcurrentHashMap<>();
    private final int port;
    private HttpServer server;

    public BillingService(int port) {
        this.port = port;
    }

    public void seedData() {
        repository.put(1, new Bill(1, 1, 149.50, "PAID"));
        repository.put(2, new Bill(2, 2, 89.00, "PENDING"));
        repository.put(3, new Bill(3, 3, 320.75, "OVERDUE"));
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/billing", new BillingHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("[BillingService] Started on http://localhost:" + port + "/api/billing");
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    private class BillingHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            try {
                String[] segments = path.split("/");
                if (segments.length <= 3) {
                    StringBuilder sb = new StringBuilder("[");
                    boolean first = true;
                    for (Bill b : repository.values()) {
                        if (!first) {
                            sb.append(",");
                        }
                        sb.append(b.toJson());
                        first = false;
                    }
                    sb.append("]");
                    sendResponse(exchange, 200, sb.toString());
                } else {
                    int id = Integer.parseInt(segments[3]);
                    Bill bill = repository.get(id);
                    if (bill == null) {
                        sendResponse(exchange, 404, "{\"error\":\"Bill not found\"}");
                    } else {
                        sendResponse(exchange, 200, bill.toJson());
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

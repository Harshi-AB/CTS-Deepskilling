import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * LoanService
 * ----------------------------------------------------------------------
 * The "loan-service" microservice, extended (compared to Exercise 1) so
 * that it REGISTERS itself with the DiscoveryServer on startup.
 *
 * IMPORTANT: Start DiscoveryServer.java FIRST, then start this class.
 *
 * Endpoint : GET /loans/{number}
 * ----------------------------------------------------------------------
 */
public class LoanService {

    private static final int PORT = 8081;
    private static final String SERVICE_NAME = "loan-service";
    private static final String SELF_URL = "http://localhost:" + PORT;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/loans", new LoanHandler());
        server.setExecutor(null);
        server.start();

        System.out.println("Loan Microservice started on port " + PORT);

        RegistryClient.register(SERVICE_NAME, SELF_URL);

        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                RegistryClient.deregister(SERVICE_NAME, SELF_URL)));

        System.out.println("Check the registry at: http://localhost:8761/eureka/apps");
    }

    static class LoanHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "{\"error\":\"Method Not Allowed. Use GET.\"}");
                return;
            }

            String[] segments = exchange.getRequestURI().getPath().split("/");
            if (segments.length < 3 || segments[2].trim().isEmpty()) {
                sendResponse(exchange, 400, "{\"error\":\"Loan account number is required in the URL.\"}");
                return;
            }

            String loanNumber = segments[2];
            String json = "{"
                    + "\"number\":\"" + loanNumber + "\","
                    + "\"type\":\"car\","
                    + "\"loan\":400000,"
                    + "\"emi\":3258,"
                    + "\"tenure\":18"
                    + "}";

            sendResponse(exchange, 200, json);
        }
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

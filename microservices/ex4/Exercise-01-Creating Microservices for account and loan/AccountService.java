import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * AccountService
 * ----------------------------------------------------------------------
 * A standalone "Account" microservice.
 *
 * This simulates a Spring Boot RESTful microservice using ONLY core Java
 * (com.sun.net.httpserver, part of the JDK) since Maven/Spring dependencies
 * are not allowed for this exercise.
 *
 * Endpoint : GET /accounts/{number}
 * Response : { "number": "...", "type": "savings", "balance": 234343 }
 *
 * This is a simple, in-memory dummy service - it has no backend/database
 * connectivity, exactly as specified in the exercise.
 * ----------------------------------------------------------------------
 */
public class AccountService {

    // Runs on its own port so it does not clash with the Loan microservice.
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/accounts", new AccountHandler());
        server.setExecutor(null); // use the default single-threaded executor
        server.start();

        System.out.println("========================================================");
        System.out.println(" Account Microservice started on port " + PORT);
        System.out.println(" Try it in a browser or curl:");
        System.out.println("   http://localhost:" + PORT + "/accounts/00987987973432");
        System.out.println("========================================================");
    }

    /**
     * Handles all requests under the /accounts context.
     * Expected path pattern: /accounts/{number}
     */
    static class AccountHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath(); // e.g. /accounts/12345

            if (!"GET".equalsIgnoreCase(method)) {
                sendResponse(exchange, 405, "{\"error\":\"Method Not Allowed. Use GET.\"}");
                return;
            }

            // path = "/accounts/{number}" -> split on "/" gives ["", "accounts", "{number}"]
            String[] segments = path.split("/");
            if (segments.length < 3 || segments[2].trim().isEmpty()) {
                sendResponse(exchange, 400, "{\"error\":\"Account number is required in the URL.\"}");
                return;
            }

            String accountNumber = segments[2];

            // Dummy JSON response - no backend connectivity, as per the exercise spec.
            String json = "{"
                    + "\"number\":\"" + accountNumber + "\","
                    + "\"type\":\"savings\","
                    + "\"balance\":234343"
                    + "}";

            sendResponse(exchange, 200, json);
        }
    }

    /** Small helper to write a JSON response with the correct headers. */
    private static void sendResponse(HttpExchange exchange, int statusCode, String body) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        byte[] bytes = body.getBytes("UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}

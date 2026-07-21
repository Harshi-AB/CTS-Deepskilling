import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * LoanService
 * ----------------------------------------------------------------------
 * A standalone "Loan" microservice.
 *
 * This simulates a Spring Boot RESTful microservice using ONLY core Java
 * (com.sun.net.httpserver, part of the JDK) since Maven/Spring dependencies
 * are not allowed for this exercise.
 *
 * Endpoint : GET /loans/{number}
 * Response : { "number": "...", "type": "car", "loan": 400000,
 *              "emi": 3258, "tenure": 18 }
 *
 * Runs on a DIFFERENT port (8081) than AccountService (8080), exactly
 * as called out in the exercise ("the reason is that each service is
 * launched with default port 8080... include server.port=8081").
 * ----------------------------------------------------------------------
 */
public class LoanService {

    private static final int PORT = 8081;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/loans", new LoanHandler());
        server.setExecutor(null);
        server.start();

        System.out.println("========================================================");
        System.out.println(" Loan Microservice started on port " + PORT);
        System.out.println(" Try it in a browser or curl:");
        System.out.println("   http://localhost:" + PORT + "/loans/H00987987972342");
        System.out.println("========================================================");
    }

    /**
     * Handles all requests under the /loans context.
     * Expected path pattern: /loans/{number}
     */
    static class LoanHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath(); // e.g. /loans/H0098...

            if (!"GET".equalsIgnoreCase(method)) {
                sendResponse(exchange, 405, "{\"error\":\"Method Not Allowed. Use GET.\"}");
                return;
            }

            String[] segments = path.split("/");
            if (segments.length < 3 || segments[2].trim().isEmpty()) {
                sendResponse(exchange, 400, "{\"error\":\"Loan account number is required in the URL.\"}");
                return;
            }

            String loanNumber = segments[2];

            // Dummy JSON response - no backend connectivity, as per the exercise spec.
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

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * AuthServer
 * ----------------------------------------------------------------------
 * A small microservice demonstrating "Using JSON Web Tokens (JWT) for
 * Secure Communication" using only core Java.
 *
 * Two endpoints:
 *
 *   1) POST /login?username=alice&password=alice123
 *      - Verifies credentials against an in-memory user store.
 *      - On success, issues a signed JWT (see JwtUtil).
 *
 *   2) GET /secure/profile
 *      - A PROTECTED resource. Requires header:
 *            Authorization: Bearer <token>
 *      - The server validates the token's signature and expiry before
 *        returning any data - this is the "secure communication" part:
 *        the client authenticates once, then presents the token on
 *        every subsequent request instead of resending credentials.
 * ----------------------------------------------------------------------
 */
public class AuthServer {

    private static final int PORT = 9000;

    // Dummy in-memory user store: username -> password (plain text only for
    // demo purposes; a real system would store a salted hash).
    private static final Map<String, String> USERS = new HashMap<>();
    static {
        USERS.put("alice", "alice123");
        USERS.put("bob", "bobSecure!45");
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/login", new LoginHandler());
        server.createContext("/secure/profile", new SecureProfileHandler());
        server.setExecutor(null);
        server.start();

        System.out.println("========================================================");
        System.out.println(" Auth Server started on port " + PORT);
        System.out.println(" 1) Log in   : POST http://localhost:" + PORT + "/login?username=alice&password=alice123");
        System.out.println(" 2) Use the returned token as: Authorization: Bearer <token>");
        System.out.println("    Then call: GET http://localhost:" + PORT + "/secure/profile");
        System.out.println(" (or simply run JwtDemoClient.java, which does both steps automatically)");
        System.out.println("========================================================");
    }

    /** Handles login and issues a JWT on successful authentication. */
    static class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "{\"error\":\"Use POST to log in.\"}");
                return;
            }

            Map<String, String> params = parseQuery(exchange.getRequestURI().getQuery());
            String username = params.get("username");
            String password = params.get("password");

            if (username == null || password == null) {
                sendResponse(exchange, 400, "{\"error\":\"'username' and 'password' query params are required.\"}");
                return;
            }

            String storedPassword = USERS.get(username);
            if (storedPassword == null || !storedPassword.equals(password)) {
                sendResponse(exchange, 401, "{\"error\":\"Invalid username or password.\"}");
                return;
            }

            String token = JwtUtil.generateToken(username);
            sendResponse(exchange, 200, "{\"token\":\"" + token + "\"}");
        }
    }

    /** A protected resource that can only be accessed with a valid JWT. */
    static class SecureProfileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "{\"error\":\"Use GET.\"}");
                return;
            }

            String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                sendResponse(exchange, 401, "{\"error\":\"Missing 'Authorization: Bearer <token>' header.\"}");
                return;
            }

            String token = authHeader.substring("Bearer ".length()).trim();

            if (!JwtUtil.validateToken(token)) {
                sendResponse(exchange, 401, "{\"error\":\"Invalid or expired token.\"}");
                return;
            }

            String username = JwtUtil.extractUsername(token);
            String json = "{\"message\":\"Access granted.\",\"username\":\"" + username + "\"}";
            sendResponse(exchange, 200, json);
        }
    }

    private static Map<String, String> parseQuery(String query) {
        Map<String, String> params = new HashMap<>();
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

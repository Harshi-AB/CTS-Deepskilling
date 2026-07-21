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
 * UserService is a standalone REST microservice responsible for managing
 * User resources. It exposes:
 *
 *   GET  /users        -> list all users
 *   GET  /users/{id}    -> fetch a single user
 *   POST /users        -> create a new user (JSON body: name, email)
 *
 * Data is stored in-memory using a ConcurrentHashMap so the service is
 * safe to use concurrently (mirrors a real microservice's repository
 * layer without requiring an actual MySQL/PostgreSQL database).
 */
public class UserService {

    private final Map<Integer, User> repository = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(0);
    private final int port;
    private HttpServer server;

    public UserService(int port) {
        this.port = port;
    }

    /** Seeds a couple of sample users so the demo has data to work with. */
    public void seedData() {
        createUser("Alice Johnson", "alice@example.com");
        createUser("Bob Smith", "bob@example.com");
    }

    /** Creates and stores a new user, returning the generated User object. */
    public User createUser(String name, String email) {
        int id = idGenerator.incrementAndGet();
        User user = new User(id, name, email);
        repository.put(id, user);
        return user;
    }

    /** Looks up a user by id; returns null if not found. */
    public User findById(int id) {
        return repository.get(id);
    }

    /** Returns every user currently stored. */
    public Collection<User> findAll() {
        return repository.values();
    }

    /** Starts the embedded HTTP server and registers REST routes. */
    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/users", new UsersHandler());
        server.setExecutor(null); // default executor
        server.start();
        System.out.println("[UserService] Started on http://localhost:" + port + "/users");
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    /** Handles all HTTP requests under the /users path. */
    private class UsersHandler implements HttpHandler {
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
                // GET /users -> list all
                StringBuilder sb = new StringBuilder("[");
                boolean first = true;
                for (User user : findAll()) {
                    if (!first) {
                        sb.append(",");
                    }
                    sb.append(user.toJson());
                    first = false;
                }
                sb.append("]");
                sendResponse(exchange, 200, sb.toString());
            } else {
                // GET /users/{id} -> single user
                int id = Integer.parseInt(segments[2]);
                User user = findById(id);
                if (user == null) {
                    sendResponse(exchange, 404, "{\"error\":\"User not found\"}");
                } else {
                    sendResponse(exchange, 200, user.toJson());
                }
            }
        }

        private void handlePost(HttpExchange exchange) throws IOException {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Map<String, String> fields = JsonUtil.fromJson(body);
            String name = fields.getOrDefault("name", "Unknown");
            String email = fields.getOrDefault("email", "unknown@example.com");
            User user = createUser(name, email);
            sendResponse(exchange, 201, user.toJson());
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

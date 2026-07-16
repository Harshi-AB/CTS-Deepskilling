import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * EmployeeDeleteHandler.java
 *
 * Implements a REST DELETE service for the Employee resource.
 *
 *   GET    /employees        -> list all employees (to verify deletions)
 *   GET    /employees/{id}   -> fetch one employee
 *   DELETE /employees/{id}   -> remove an employee; returns 204 No Content
 *                               on success, matching REST convention that
 *                               a successful DELETE has no response body.
 */
public class EmployeeDeleteHandler implements HttpHandler {

    private final EmployeeRepository repository;

    public EmployeeDeleteHandler(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] segments = path.replaceFirst("^/", "").split("/");

        if ("GET".equalsIgnoreCase(method) && segments.length == 1 && segments[0].equals("employees")) {
            sendJson(exchange, 200, JsonUtil.toJsonArray(repository.findAll()));
        } else if ("GET".equalsIgnoreCase(method) && segments.length == 2 && segments[0].equals("employees")) {
            handleGetOne(exchange, segments[1]);
        } else if ("DELETE".equalsIgnoreCase(method) && segments.length == 2 && segments[0].equals("employees")) {
            handleDelete(exchange, segments[1]);
        } else {
            sendJson(exchange, 404, JsonUtil.errorJson("Resource not found: " + path));
        }
    }

    private void handleGetOne(HttpExchange exchange, String idSegment) throws IOException {
        Optional<Integer> id = parseId(idSegment);
        if (id.isEmpty()) {
            sendJson(exchange, 400, JsonUtil.errorJson("Employee id must be numeric."));
            return;
        }
        Optional<Employee> found = repository.findById(id.get());
        if (found.isPresent()) {
            sendJson(exchange, 200, JsonUtil.toJson(found.get()));
        } else {
            sendJson(exchange, 404, JsonUtil.errorJson("Employee not found for id: " + id.get()));
        }
    }

    private void handleDelete(HttpExchange exchange, String idSegment) throws IOException {
        Optional<Integer> id = parseId(idSegment);
        if (id.isEmpty()) {
            sendJson(exchange, 400, JsonUtil.errorJson("Employee id must be numeric."));
            return;
        }
        if (!repository.existsById(id.get())) {
            sendJson(exchange, 404, JsonUtil.errorJson("Cannot delete. Employee not found for id: " + id.get()));
            return;
        }
        repository.deleteById(id.get());
        // 204 No Content: successful DELETE, nothing to return in the body
        exchange.sendResponseHeaders(204, -1);
        exchange.close();
    }

    private Optional<Integer> parseId(String segment) {
        try {
            return Optional.of(Integer.parseInt(segment));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private void sendJson(HttpExchange exchange, int statusCode, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}

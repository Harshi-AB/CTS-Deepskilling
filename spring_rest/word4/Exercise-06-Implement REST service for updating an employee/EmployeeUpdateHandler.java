import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

/**
 * EmployeeUpdateHandler.java
 *
 * Implements a REST service for updating an existing Employee, using the
 * PUT method, which is the correct HTTP verb for a full resource update
 * (idempotent - repeating the same PUT has the same effect as doing it once).
 *
 *   GET /employees/{id}       -> fetch one employee (for verification)
 *   GET /employees            -> list all employees
 *   PUT /employees/{id}       -> update an existing employee's fields
 */
public class EmployeeUpdateHandler implements HttpHandler {

    private final EmployeeRepository repository;

    public EmployeeUpdateHandler(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] segments = path.replaceFirst("^/", "").split("/");

        if ("GET".equalsIgnoreCase(method) && segments.length == 1 && segments[0].equals("employees")) {
            sendResponse(exchange, 200, JsonUtil.toJsonArray(repository.findAll()));
        } else if ("GET".equalsIgnoreCase(method) && segments.length == 2 && segments[0].equals("employees")) {
            handleGetOne(exchange, segments[1]);
        } else if ("PUT".equalsIgnoreCase(method) && segments.length == 2 && segments[0].equals("employees")) {
            handlePut(exchange, segments[1]);
        } else {
            sendResponse(exchange, 404, JsonUtil.errorJson("Resource not found: " + path));
        }
    }

    private void handleGetOne(HttpExchange exchange, String idSegment) throws IOException {
        Optional<Integer> id = parseId(idSegment);
        if (id.isEmpty()) {
            sendResponse(exchange, 400, JsonUtil.errorJson("Employee id must be numeric."));
            return;
        }
        Optional<Employee> found = repository.findById(id.get());
        if (found.isPresent()) {
            sendResponse(exchange, 200, JsonUtil.toJson(found.get()));
        } else {
            sendResponse(exchange, 404, JsonUtil.errorJson("Employee not found for id: " + id.get()));
        }
    }

    private void handlePut(HttpExchange exchange, String idSegment) throws IOException {
        Optional<Integer> idOpt = parseId(idSegment);
        if (idOpt.isEmpty()) {
            sendResponse(exchange, 400, JsonUtil.errorJson("Employee id must be numeric."));
            return;
        }
        int id = idOpt.get();

        if (!repository.existsById(id)) {
            sendResponse(exchange, 404, JsonUtil.errorJson("Cannot update. Employee not found for id: " + id));
            return;
        }

        String body = readRequestBody(exchange.getRequestBody());
        Map<String, String> fields = JsonUtil.parseFlatObject(body);

        Employee existing = repository.findById(id).get();

        // Apply only the fields present in the request, updating the existing bean
        if (fields.containsKey("name")) {
            existing.setName(fields.get("name"));
        }
        if (fields.containsKey("department")) {
            existing.setDepartment(fields.get("department"));
        }
        if (fields.containsKey("salary")) {
            try {
                existing.setSalary(Double.parseDouble(fields.get("salary")));
            } catch (NumberFormatException e) {
                sendResponse(exchange, 400, JsonUtil.errorJson("Field 'salary' must be numeric."));
                return;
            }
        }

        Employee updated = repository.save(existing);
        sendResponse(exchange, 200, JsonUtil.toJson(updated));
    }

    private Optional<Integer> parseId(String segment) {
        try {
            return Optional.of(Integer.parseInt(segment));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private String readRequestBody(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(data)) != -1) {
            buffer.write(data, 0, bytesRead);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String responseBody) throws IOException {
        byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}

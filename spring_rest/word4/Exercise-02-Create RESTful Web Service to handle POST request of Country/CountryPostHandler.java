import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * CountryPostHandler.java
 *
 * Handles both:
 *   GET  /countries        -> list all countries (so the POST can be verified)
 *   POST /countries        -> create a new Country from the JSON request body
 *
 * Demonstrates reading a request body, converting it into a Country bean,
 * persisting it via the repository, and returning 201 Created with a
 * Location header, per REST conventions.
 */
public class CountryPostHandler implements HttpHandler {

    private final CountryRepository repository;

    public CountryPostHandler(CountryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if ("GET".equalsIgnoreCase(method)) {
            handleGetAll(exchange);
        } else if ("POST".equalsIgnoreCase(method)) {
            handlePost(exchange);
        } else {
            sendResponse(exchange, 405, JsonUtil.errorJson("Method Not Allowed. Use GET or POST."));
        }
    }

    private void handleGetAll(HttpExchange exchange) throws IOException {
        List<Country> all = repository.findAll();
        sendResponse(exchange, 200, JsonUtil.toJsonArray(all));
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange.getRequestBody());

        Map<String, String> fields = JsonUtil.parseFlatObject(requestBody);

        if (!fields.containsKey("code") || !fields.containsKey("name")) {
            sendResponse(exchange, 400, JsonUtil.errorJson("Fields 'code' and 'name' are required."));
            return;
        }

        Country country = new Country();
        country.setCode(fields.get("code"));
        country.setName(fields.get("name"));
        country.setCapital(fields.getOrDefault("capital", ""));
        country.setRegion(fields.getOrDefault("region", ""));

        String populationStr = fields.getOrDefault("population", "0");
        try {
            country.setPopulation(Long.parseLong(populationStr));
        } catch (NumberFormatException e) {
            sendResponse(exchange, 400, JsonUtil.errorJson("Field 'population' must be numeric."));
            return;
        }

        Country saved = repository.save(country);

        exchange.getResponseHeaders().set("Location", "/countries/" + saved.getCode());
        sendResponse(exchange, 201, JsonUtil.toJson(saved));
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

    private void sendResponse(HttpExchange exchange, int statusCode, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}

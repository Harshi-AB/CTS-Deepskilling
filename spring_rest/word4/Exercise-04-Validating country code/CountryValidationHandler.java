import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * CountryValidationHandler.java
 *
 * Handles POST /countries, validating the country code (and name) before
 * the resource is persisted. Invalid input results in HTTP 400 with a
 * descriptive JSON error body instead of a server crash or silent failure.
 */
public class CountryValidationHandler implements HttpHandler {

    private final CountryRepository repository;
    private final CountryValidator validator;

    public CountryValidationHandler(CountryRepository repository, CountryValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if ("POST".equalsIgnoreCase(method)) {
            handlePost(exchange);
        } else if ("GET".equalsIgnoreCase(method)) {
            sendResponse(exchange, 200, JsonUtil.toJsonArray(repository.findAll()));
        } else {
            sendResponse(exchange, 405, JsonUtil.errorJson("Method Not Allowed."));
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange.getRequestBody());
        Map<String, String> fields = JsonUtil.parseFlatObject(body);

        Country country = new Country();
        country.setCode(fields.getOrDefault("code", ""));
        country.setName(fields.getOrDefault("name", ""));
        country.setCapital(fields.getOrDefault("capital", ""));
        country.setRegion(fields.getOrDefault("region", ""));

        try {
            country.setPopulation(Long.parseLong(fields.getOrDefault("population", "0")));
        } catch (NumberFormatException e) {
            sendResponse(exchange, 400, JsonUtil.errorJson("Field 'population' must be numeric."));
            return;
        }

        try {
            validator.validate(country);
        } catch (IllegalArgumentException e) {
            sendResponse(exchange, 400, JsonUtil.errorJson(e.getMessage()));
            return;
        }

        repository.save(country);
        exchange.getResponseHeaders().set("Location", "/countries/" + country.getCode());
        sendResponse(exchange, 201, JsonUtil.toJson(country));
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

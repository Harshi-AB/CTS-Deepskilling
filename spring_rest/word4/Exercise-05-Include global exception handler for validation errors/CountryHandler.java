import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * CountryHandler.java
 *
 * The "business logic" handler. Notice that it does NOT contain any
 * try/catch around validation - it simply throws a ValidationException
 * when something is wrong, and trusts that the GlobalExceptionHandler
 * wrapping this handler will translate that into the correct HTTP response.
 * This keeps the business logic clean and focused.
 */
public class CountryHandler implements HttpHandler {

    private final CountryRepository repository;
    private final CountryValidator validator;

    public CountryHandler(CountryRepository repository, CountryValidator validator) {
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
            // Deliberately thrown, not handled here - the global handler owns formatting
            throw new ValidationException("Method Not Allowed: " + method, 405);
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

        String populationStr = fields.getOrDefault("population", "0");
        try {
            country.setPopulation(Long.parseLong(populationStr));
        } catch (NumberFormatException e) {
            throw new ValidationException("Field 'population' must be numeric.");
        }

        // No try/catch here - if this throws, GlobalExceptionHandler catches it
        validator.validate(country);

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

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * CountryResourceHandler.java
 *
 * Demonstrates correct RESTful resource naming guidelines:
 *   1. Use plural NOUNS for collections:      GET /countries
 *   2. Identify a single resource by its id:  GET /countries/{code}
 *   3. Never put verbs in the URI - actions come from the HTTP method,
 *      not from the path (e.g. no "/getCountry" or "/fetchAllCountries").
 *   4. Use query parameters for filtering a collection, not new endpoints:
 *      GET /countries?region=Asia   (instead of /countries/byRegion/Asia)
 *   5. Keep the URI hierarchy consistent and lower-case.
 *
 * This handler only supports GET, since the goal of this exercise is naming,
 * not full CRUD (that is covered in later exercises).
 */
public class CountryResourceHandler implements HttpHandler {

    private final CountryRepository repository;

    public CountryResourceHandler(CountryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath(); // e.g. /countries or /countries/IN
        String query = exchange.getRequestURI().getQuery(); // e.g. region=Asia

        if (!"GET".equalsIgnoreCase(method)) {
            sendResponse(exchange, 405, JsonUtil.errorJson("Method Not Allowed. Use GET on this resource."));
            return;
        }

        // Split the path into segments: ["countries"] or ["countries", "IN"]
        String[] segments = path.replaceFirst("^/", "").split("/");

        if (segments.length == 1 && segments[0].equals("countries")) {
            handleCollection(exchange, query);
        } else if (segments.length == 2 && segments[0].equals("countries")) {
            handleSingleResource(exchange, segments[1]);
        } else {
            sendResponse(exchange, 404, JsonUtil.errorJson("Resource not found: " + path));
        }
    }

    /** GET /countries  or  GET /countries?region=Asia */
    private void handleCollection(HttpExchange exchange, String query) throws IOException {
        List<Country> result;
        String region = extractQueryParam(query, "region");
        if (region != null) {
            result = repository.findByRegion(region);
        } else {
            result = repository.findAll();
        }
        sendResponse(exchange, 200, JsonUtil.toJsonArray(result));
    }

    /** GET /countries/{code} */
    private void handleSingleResource(HttpExchange exchange, String code) throws IOException {
        Optional<Country> found = repository.findByCode(code);
        if (found.isPresent()) {
            sendResponse(exchange, 200, JsonUtil.toJson(found.get()));
        } else {
            sendResponse(exchange, 404, JsonUtil.errorJson("Country not found for code: " + code));
        }
    }

    private String extractQueryParam(String query, String key) {
        if (query == null) {
            return null;
        }
        for (String pair : query.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2 && kv[0].equals(key)) {
                return kv[1];
            }
        }
        return null;
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

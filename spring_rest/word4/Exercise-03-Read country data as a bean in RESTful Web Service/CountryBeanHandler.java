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
 * CountryBeanHandler.java
 *
 * Demonstrates reading country data submitted by a client and binding it
 * as a Java bean (Country), then handing that bean to the repository and
 * echoing it back as the response - proving that the incoming JSON payload
 * was correctly read into a strongly-typed object rather than handled as
 * raw text.
 *
 *   GET  /countries/{code}  -> fetch a Country and return it as a bean (JSON)
 *   POST /countries         -> read posted JSON as a Country bean, save it,
 *                              and return the bean back to the caller
 */
public class CountryBeanHandler implements HttpHandler {

    private final CountryRepository repository;
    private final CountryBeanMapper mapper;

    public CountryBeanHandler(CountryRepository repository, CountryBeanMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if ("POST".equalsIgnoreCase(method) && path.equals("/countries")) {
            handlePost(exchange);
        } else if ("GET".equalsIgnoreCase(method)) {
            handleGet(exchange, path);
        } else {
            sendResponse(exchange, 405, JsonUtil.errorJson("Method Not Allowed."));
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange.getRequestBody());
        Map<String, String> rawFields = JsonUtil.parseFlatObject(body);

        // Read the raw request data as a proper Java bean
        Country bean = mapper.toBean(rawFields);

        if (bean.getCode() == null || bean.getCode().isBlank()) {
            sendResponse(exchange, 400, JsonUtil.errorJson("Field 'code' is required."));
            return;
        }

        repository.save(bean);

        // Echo the bean back out, proving it was read and bound correctly
        sendResponse(exchange, 201, mapper.toJson(bean));
    }

    private void handleGet(HttpExchange exchange, String path) throws IOException {
        String[] segments = path.replaceFirst("^/", "").split("/");
        if (segments.length == 2 && segments[0].equals("countries")) {
            Optional<Country> found = repository.findByCode(segments[1]);
            if (found.isPresent()) {
                sendResponse(exchange, 200, mapper.toJson(found.get()));
            } else {
                sendResponse(exchange, 404, JsonUtil.errorJson("Country not found: " + segments[1]));
            }
        } else {
            sendResponse(exchange, 404, JsonUtil.errorJson("Resource not found: " + path));
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

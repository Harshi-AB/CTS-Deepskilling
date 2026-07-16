import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * GlobalExceptionHandler.java
 *
 * A "decorator" HttpHandler that wraps any other HttpHandler and centrally
 * catches exceptions thrown while processing a request, converting them
 * into consistent JSON error responses with the right HTTP status code.
 *
 * This means individual business handlers (like CountryHandler) can simply
 * throw a ValidationException when something is wrong, without worrying
 * about how to format the HTTP error response themselves - that concern is
 * handled in exactly one place, which is the point of a *global* exception
 * handler.
 */
public class GlobalExceptionHandler implements HttpHandler {

    private final HttpHandler delegate;

    public GlobalExceptionHandler(HttpHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            delegate.handle(exchange);
        } catch (ValidationException e) {
            sendError(exchange, e.getStatusCode(), e.getMessage());
        } catch (IllegalArgumentException e) {
            sendError(exchange, 400, e.getMessage());
        } catch (Exception e) {
            // Catch-all so a bug never surfaces as a raw stack trace to the caller
            sendError(exchange, 500, "Internal Server Error: " + e.getMessage());
        }
    }

    private void sendError(HttpExchange exchange, int statusCode, String message) throws IOException {
        String body = JsonUtil.errorJson(message);
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * GlobalExceptionHandlerService.java
 *
 * Main class - wires CountryHandler through the GlobalExceptionHandler
 * decorator, so every validation error (and any other unexpected
 * exception) is caught and translated in exactly one place.
 *
 * Run this class, then try:
 *   POST http://localhost:8080/countries
 *        invalid: {"code":"","name":""}   -> 400, JSON error body, no stack trace
 *   DELETE http://localhost:8080/countries -> 405, JSON error body
 */
public class GlobalExceptionHandlerService {

    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        CountryRepository repository = new CountryRepository();
        CountryValidator validator = new CountryValidator();

        CountryHandler businessHandler = new CountryHandler(repository, validator);
        GlobalExceptionHandler wrappedHandler = new GlobalExceptionHandler(businessHandler);

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/countries", wrappedHandler);
        server.setExecutor(null);
        server.start();

        System.out.println("Server started on http://localhost:" + PORT);
        System.out.println("Every validation error is now handled centrally by GlobalExceptionHandler.");
    }
}

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * CountryCodeValidationService.java
 *
 * Main class - starts the embedded server that validates country codes
 * before creating a Country resource.
 *
 * Run this class, then try:
 *   POST http://localhost:8080/countries
 *        valid:   {"code":"IN","name":"India"}          -> 201 Created
 *        invalid: {"code":"india","name":"India"}       -> 400 Bad Request
 *        invalid: {"code":"","name":"India"}             -> 400 Bad Request
 */
public class CountryCodeValidationService {

    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        CountryRepository repository = new CountryRepository();
        CountryValidator validator = new CountryValidator();

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/countries", new CountryValidationHandler(repository, validator));
        server.setExecutor(null);
        server.start();

        System.out.println("Server started on http://localhost:" + PORT);
        System.out.println("Try posting a Country with an invalid code to see validation in action.");
    }
}

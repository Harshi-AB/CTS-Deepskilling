import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * ResourceNamingGuidelinesServer.java
 *
 * Main class - starts a lightweight embedded HTTP server (from the JDK's
 * built-in com.sun.net.httpserver package - no Spring, no Maven/Gradle,
 * no third-party JARs) and wires the CountryResourceHandler to the
 * "/countries" resource path.
 *
 * Run this class, then try:
 *   GET http://localhost:8080/countries
 *   GET http://localhost:8080/countries/IN
 *   GET http://localhost:8080/countries?region=Asia
 */
public class ResourceNamingGuidelinesServer {

    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        CountryRepository repository = new CountryRepository();

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/countries", new CountryResourceHandler(repository));
        server.setExecutor(null); // default single-threaded executor
        server.start();

        System.out.println("Server started on http://localhost:" + PORT);
        System.out.println("Try: GET /countries , GET /countries/IN , GET /countries?region=Asia");
    }
}

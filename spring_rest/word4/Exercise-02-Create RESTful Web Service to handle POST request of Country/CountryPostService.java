import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * CountryPostService.java
 *
 * Main class - starts an embedded HTTP server exposing the "/countries"
 * resource with GET (list) and POST (create) support.
 *
 * Run this class, then try:
 *   GET  http://localhost:8080/countries
 *   POST http://localhost:8080/countries
 *        body: {"code":"DE","name":"Germany","capital":"Berlin","region":"Europe","population":83000000}
 */
public class CountryPostService {

    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        CountryRepository repository = new CountryRepository();

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/countries", new CountryPostHandler(repository));
        server.setExecutor(null);
        server.start();

        System.out.println("Server started on http://localhost:" + PORT);
        System.out.println("Try: GET /countries , POST /countries with a JSON body");
    }
}

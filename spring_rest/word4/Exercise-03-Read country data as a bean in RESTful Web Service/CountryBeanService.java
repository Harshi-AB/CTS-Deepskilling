import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * CountryBeanService.java
 *
 * Main class - starts the embedded server for the country-as-bean exercise.
 *
 * Run this class, then try:
 *   POST http://localhost:8080/countries
 *        body: {"code":"JP","name":"Japan","capital":"Tokyo","region":"Asia","population":123000000}
 *   GET  http://localhost:8080/countries/JP
 */
public class CountryBeanService {

    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        CountryRepository repository = new CountryRepository();
        CountryBeanMapper mapper = new CountryBeanMapper();

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/countries", new CountryBeanHandler(repository, mapper));
        server.setExecutor(null);
        server.start();

        System.out.println("Server started on http://localhost:" + PORT);
        System.out.println("Try: POST /countries with a JSON body, then GET /countries/{code}");
    }
}

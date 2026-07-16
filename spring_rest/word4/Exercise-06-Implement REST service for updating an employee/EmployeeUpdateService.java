import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * EmployeeUpdateService.java
 *
 * Main class - starts the embedded server exposing the "/employees"
 * resource with GET (read) and PUT (update) support.
 *
 * Run this class, then try:
 *   GET http://localhost:8080/employees/1
 *   PUT http://localhost:8080/employees/1
 *       body: {"name":"Aditi Sharma","department":"Engineering","salary":72000}
 */
public class EmployeeUpdateService {

    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        EmployeeRepository repository = new EmployeeRepository();

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/employees", new EmployeeUpdateHandler(repository));
        server.setExecutor(null);
        server.start();

        System.out.println("Server started on http://localhost:" + PORT);
        System.out.println("Try: GET /employees/1 , PUT /employees/1 with a JSON body");
    }
}

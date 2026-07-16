import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * EmployeeDeleteService.java
 *
 * Main class - starts the embedded server exposing the "/employees"
 * resource with GET (read) and DELETE (remove) support.
 *
 * Run this class, then try:
 *   GET    http://localhost:8080/employees
 *   DELETE http://localhost:8080/employees/2
 *   GET    http://localhost:8080/employees        (id 2 should be gone)
 */
public class EmployeeDeleteService {

    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        EmployeeRepository repository = new EmployeeRepository();

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/employees", new EmployeeDeleteHandler(repository));
        server.setExecutor(null);
        server.start();

        System.out.println("Server started on http://localhost:" + PORT);
        System.out.println("Try: GET /employees , DELETE /employees/{id}");
    }
}

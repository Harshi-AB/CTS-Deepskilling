import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * EmployeeController.java
 *
 * REST Controller layer.
 *
 * Requirements covered:
 *  - New GET method getAllEmployees() that returns the employee list
 *  - Mapped with @GetMapping to the URL '/employees'
 *  - Invokes employeeService.getAllEmployees() and returns the result
 *
 * Since this project uses Core Java only (no Spring Web / Maven), the
 * @GetMapping-annotated method is wired to a REAL HTTP endpoint using
 * com.sun.net.httpserver.HttpServer (bundled with the JDK), so the
 * service can genuinely be tested with Postman, exactly as required
 * by the hands-on ("Test the service using postman").
 */
public class EmployeeController implements HttpHandler {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * getAllEmployees() - GET /employees
     * Invokes employeeService.getAllEmployees() and returns the employee list.
     */
    @GetMapping("/employees")
    public ArrayList<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    /**
     * Handles the raw HTTP request coming into the embedded server and
     * delegates to getAllEmployees(), writing back a JSON HTTP response -
     * this is the Core Java equivalent of what Spring's DispatcherServlet
     * would do automatically for a @RestController.
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        System.out.println("[LOG] " + method + " " + exchange.getRequestURI());

        if (!"GET".equalsIgnoreCase(method)) {
            String response = "{\"error\":\"Method Not Allowed\"}";
            sendResponse(exchange, 405, response);
            return;
        }

        ArrayList<Employee> employees = getAllEmployees();

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < employees.size(); i++) {
            json.append(employees.get(i).toJson());
            if (i < employees.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");

        sendResponse(exchange, 200, json.toString());
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(bytes);
        }
    }
}

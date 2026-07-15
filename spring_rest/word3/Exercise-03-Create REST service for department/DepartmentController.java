import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * DepartmentController.java
 *
 * REST Controller layer for departments.
 *
 * Requirements covered:
 *  - getAllDepartments() with URL "/departments", returning an array
 *    of departments
 *
 * As with EmployeeController in Exercise 2, this is wired to a real
 * embedded HTTP endpoint (com.sun.net.httpserver.HttpServer, bundled
 * with the JDK) so the service can be tested with Postman, and every
 * incoming request is logged to the console so the log-verification
 * step in the hands-on can be completed.
 */
public class DepartmentController implements HttpHandler {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * getAllDepartments() - GET /departments
     * Returns the array of departments.
     */
    @GetMapping("/departments")
    public ArrayList<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        // Log line so the "verify the service is called by looking into
        // the logs" step from the hands-on can be observed on the console.
        System.out.println("[LOG] Department REST service called -> "
                + method + " " + exchange.getRequestURI());

        if (!"GET".equalsIgnoreCase(method)) {
            sendResponse(exchange, 405, "{\"error\":\"Method Not Allowed\"}");
            return;
        }

        ArrayList<Department> departments = getAllDepartments();

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < departments.size(); i++) {
            json.append(departments.get(i).toJson());
            if (i < departments.size() - 1) {
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

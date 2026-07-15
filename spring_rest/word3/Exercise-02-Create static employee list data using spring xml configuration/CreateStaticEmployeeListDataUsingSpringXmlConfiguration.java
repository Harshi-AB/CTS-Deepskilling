import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;

/**
 * CreateStaticEmployeeListDataUsingSpringXmlConfiguration.java
 *
 * Main / driver class for Exercise 2.
 *
 * Wires together the layers (equivalent to what a Spring IoC container
 * would do automatically):
 *   EmployeeDao        (reads employee.xml, exposes getAllEmployees())
 *   EmployeeService     (@Service, @Transactional getAllEmployees())
 *   EmployeeController  (@GetMapping("/employees"))
 *
 * It also starts a small embedded HTTP server (built into the JDK via
 * com.sun.net.httpserver.HttpServer) so that GET /employees can be
 * tested for real using Postman, as required by the hands-on.
 *
 * Compile: javac *.java
 * Run:     java CreateStaticEmployeeListDataUsingSpringXmlConfiguration
 *
 * Once running, open Postman (or a browser) and call:
 *   GET http://localhost:8080/employees
 */
public class CreateStaticEmployeeListDataUsingSpringXmlConfiguration {

    public static void main(String[] args) throws Exception {

        // 1. DAO - reads static employee list data from employee.xml
        EmployeeDao employeeDao = new EmployeeDao("employee.xml");

        // 2. Service - depends on the DAO
        EmployeeService employeeService = new EmployeeService(employeeDao);

        // 3. Controller - depends on the Service, exposes GET /employees
        EmployeeController employeeController = new EmployeeController(employeeService);

        // --- Print the employee list to the console first ---
        ArrayList<Employee> employees = employeeDao.getAllEmployees();
        System.out.println("================= EMPLOYEE_LIST (from employee.xml) =================");
        for (Employee employee : employees) {
            System.out.println(employee);
        }
        System.out.println("=======================================================================");

        // --- Start the embedded REST server ---
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/employees", employeeController);
        server.setExecutor(null); // default single-threaded executor
        server.start();

        System.out.println("REST service started. Test it with Postman:");
        System.out.println("   GET http://localhost:" + port + "/employees");
        System.out.println("Press Ctrl+C to stop the server.");
    }
}

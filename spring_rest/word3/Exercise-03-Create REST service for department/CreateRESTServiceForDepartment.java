import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;

/**
 * CreateRESTServiceForDepartment.java
 *
 * Main / driver class for Exercise 3.
 *
 * Wires together:
 *   DepartmentDao        (reads department.xml, exposes getAllDepartments())
 *   DepartmentService     (@Service getAllDepartments())
 *   DepartmentController  (@GetMapping("/departments"))
 *
 * Starts an embedded HTTP server (com.sun.net.httpserver.HttpServer,
 * bundled with the JDK - no external dependency) so GET /departments
 * can be tested with Postman, exactly as required by the hands-on.
 *
 * Compile: javac *.java
 * Run:     java CreateRESTServiceForDepartment
 *
 * Once running, open Postman (or a browser) and call:
 *   GET http://localhost:8081/departments
 */
public class CreateRESTServiceForDepartment {

    public static void main(String[] args) throws Exception {

        // 1. DAO - reads department list from department.xml
        DepartmentDao departmentDao = new DepartmentDao("department.xml");

        // 2. Service - depends on the DAO
        DepartmentService departmentService = new DepartmentService(departmentDao);

        // 3. Controller - depends on the Service, exposes GET /departments
        DepartmentController departmentController = new DepartmentController(departmentService);

        // --- Print the department list to the console first ---
        ArrayList<Department> departments = departmentDao.getAllDepartments();
        System.out.println("================= DEPARTMENT_LIST (from department.xml) =================");
        for (Department department : departments) {
            System.out.println(department);
        }
        System.out.println("===========================================================================");

        // --- Start the embedded REST server ---
        int port = 8081; // different port than the Employee service (Exercise 2)
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/departments", departmentController);
        server.setExecutor(null); // default single-threaded executor
        server.start();

        System.out.println("REST service started. Test it with Postman:");
        System.out.println("   GET http://localhost:" + port + "/departments");
        System.out.println("Press Ctrl+C to stop the server.");
    }
}

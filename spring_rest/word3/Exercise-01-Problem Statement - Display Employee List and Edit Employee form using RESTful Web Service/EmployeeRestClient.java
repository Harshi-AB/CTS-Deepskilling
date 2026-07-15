import java.util.ArrayList;
import java.util.List;

/**
 * EmployeeRestClient.java
 *
 * In the real hands-on, the Angular application calls a Spring RESTful
 * Web Service (GET /employees, GET /employees/{id}) to fetch employee
 * data. Since this project uses Core Java only (no Spring, no HTTP
 * client libraries), this class SIMULATES that RESTful Web Service
 * client: it behaves as the "remote service" that the UI layer talks
 * to, keeping the same layered thinking (UI -> REST client -> data).
 *
 * This mirrors the problem statement:
 * "the angular application has to be changed to get the data from a
 *  RESTful Web Service developed in Spring".
 */
public class EmployeeRestClient {

    // Simulated server-side data store (equivalent of EMPLOYEE_LIST on the server)
    private static final List<Employee> EMPLOYEE_LIST = new ArrayList<>();

    static {
        EMPLOYEE_LIST.add(new Employee(101, "John Smith", "john.smith@cognizant.com", "IT"));
        EMPLOYEE_LIST.add(new Employee(102, "Emma Watson", "emma.watson@cognizant.com", "HR"));
        EMPLOYEE_LIST.add(new Employee(103, "Michael Brown", "michael.brown@cognizant.com", "Finance"));
        EMPLOYEE_LIST.add(new Employee(104, "Olivia Davis", "olivia.davis@cognizant.com", "IT"));
    }

    /**
     * Simulates: GET /employees
     * Returns the full list of employees, as the RESTful Web Service would.
     */
    public List<Employee> getAllEmployees() {
        System.out.println("[REST CLIENT] GET /employees  -> 200 OK");
        return new ArrayList<>(EMPLOYEE_LIST);
    }

    /**
     * Simulates: GET /employees/{id}
     * Returns a single employee by id, as the RESTful Web Service would
     * when populating the "Edit Employee" form.
     */
    public Employee getEmployeeById(int id) {
        System.out.println("[REST CLIENT] GET /employees/" + id + "  -> 200 OK");
        for (Employee employee : EMPLOYEE_LIST) {
            if (employee.getEmployeeId() == id) {
                return employee;
            }
        }
        return null;
    }

    /**
     * Simulates: PUT /employees/{id}
     * Updates an employee, as would happen when the "Edit Employee" form
     * is saved and posted back to the RESTful Web Service.
     */
    public boolean updateEmployee(Employee updatedEmployee) {
        for (int i = 0; i < EMPLOYEE_LIST.size(); i++) {
            if (EMPLOYEE_LIST.get(i).getEmployeeId() == updatedEmployee.getEmployeeId()) {
                EMPLOYEE_LIST.set(i, updatedEmployee);
                System.out.println("[REST CLIENT] PUT /employees/"
                        + updatedEmployee.getEmployeeId() + "  -> 200 OK (updated)");
                return true;
            }
        }
        System.out.println("[REST CLIENT] PUT /employees/"
                + updatedEmployee.getEmployeeId() + "  -> 404 NOT FOUND");
        return false;
    }
}

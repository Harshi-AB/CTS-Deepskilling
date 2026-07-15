import java.util.List;

/**
 * EmployeeListView.java
 *
 * Represents the "Employee List" screen described in the problem
 * statement. It consumes the (simulated) RESTful Web Service through
 * EmployeeRestClient, instead of using hard coded values as the
 * original Angular screen used to do.
 */
public class EmployeeListView {

    private final EmployeeRestClient restClient;

    public EmployeeListView(EmployeeRestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Renders the employee list screen to the console.
     */
    public void render() {
        List<Employee> employees = restClient.getAllEmployees();

        System.out.println();
        System.out.println("================= EMPLOYEE LIST =================");
        System.out.printf("%-5s %-20s %-30s %-15s%n", "ID", "NAME", "EMAIL", "DEPARTMENT");
        System.out.println("--------------------------------------------------------------------");
        for (Employee employee : employees) {
            System.out.println(employee + "     [ Edit ]");
        }
        System.out.println("======================================================================");
    }
}

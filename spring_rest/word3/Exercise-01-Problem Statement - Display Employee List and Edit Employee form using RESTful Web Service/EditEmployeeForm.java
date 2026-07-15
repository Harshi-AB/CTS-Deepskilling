/**
 * EditEmployeeForm.java
 *
 * Represents the "Edit Employee" form described in the problem
 * statement: "clicking on the Edit button against each employee
 * should display Edit Employee form with values retrieved from
 * RESTful Web Service".
 */
public class EditEmployeeForm {

    private final EmployeeRestClient restClient;

    public EditEmployeeForm(EmployeeRestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Loads an employee (via the simulated REST GET call) and displays
     * the Edit Employee form pre-populated with its values.
     */
    public Employee load(int employeeId) {
        Employee employee = restClient.getEmployeeById(employeeId);

        System.out.println();
        System.out.println("================ EDIT EMPLOYEE FORM =================");
        if (employee == null) {
            System.out.println("No employee found with id " + employeeId);
        } else {
            System.out.println("Employee Id : " + employee.getEmployeeId());
            System.out.println("Name        : " + employee.getEmployeeName());
            System.out.println("Email       : " + employee.getEmail());
            System.out.println("Department  : " + employee.getDepartment());
        }
        System.out.println("=======================================================");
        return employee;
    }

    /**
     * Simulates the user editing the department field and clicking
     * "Save", which posts the update back through the REST client.
     */
    public void save(Employee employee, String newDepartment) {
        employee.setDepartment(newDepartment);
        boolean success = restClient.updateEmployee(employee);
        System.out.println(success
                ? "Employee updated successfully."
                : "Failed to update employee.");
    }
}

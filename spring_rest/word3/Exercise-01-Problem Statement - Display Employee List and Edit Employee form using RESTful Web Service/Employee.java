/**
 * Employee.java
 *
 * Plain Old Java Object (POJO) that represents an Employee.
 * This class models the data that, in the real Angular + Spring
 * application, would be fetched from the RESTful Web Service and
 * displayed on the "Employee List" screen / "Edit Employee" form.
 *
 * No package declaration is used, as required (Core Java only).
 */
public class Employee {

    private int employeeId;
    private String employeeName;
    private String email;
    private String department;

    // No-arg constructor (required for simple object creation)
    public Employee() {
    }

    // Parameterised constructor
    public Employee(int employeeId, String employeeName, String email, String department) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.email = email;
        this.department = department;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Returns a nicely formatted single-line representation of this
     * Employee, used when rendering the "Employee List" screen.
     */
    @Override
    public String toString() {
        return String.format("%-5d %-20s %-30s %-15s",
                employeeId, employeeName, email, department);
    }
}

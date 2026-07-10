/*
 * EmployeeService.java
 * --------------------
 * Service layer delegating to EmployeeRepository.
 */
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public double getAverageSalary() {
        return employeeRepository.getAverageSalary();
    }

    public double getAverageSalary(int departmentId) {
        return employeeRepository.getAverageSalary(departmentId);
    }
}

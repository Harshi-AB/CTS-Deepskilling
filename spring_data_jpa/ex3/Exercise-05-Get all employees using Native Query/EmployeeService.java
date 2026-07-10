/*
 * EmployeeService.java
 * --------------------
 * Service layer delegating to EmployeeRepository.
 */
import java.util.List;

public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployeesNative() {
        return employeeRepository.getAllEmployeesNative();
    }
}

/*
 * EmployeeService.java
 * --------------------
 * Service layer delegating to EmployeeRepository (kept as a distinct class
 * to mirror the layered Repository -> Service -> Application architecture
 * used by Spring Data JPA projects).
 */
import java.util.List;

public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllPermanentEmployees() {
        return employeeRepository.getAllPermanentEmployees();
    }
}

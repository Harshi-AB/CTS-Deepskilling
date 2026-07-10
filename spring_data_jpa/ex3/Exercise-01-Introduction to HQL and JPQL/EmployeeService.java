/*
 * EmployeeService.java
 * --------------------
 * Service layer (Service pattern) sitting between the application entry
 * point (OrmLearnApplication) and the repository. In a real Spring Data JPA
 * project this class would be annotated @Service and the repository would
 * be auto-injected; here we perform that wiring manually with plain
 * constructor injection, keeping the same layered design.
 */
import java.util.List;

public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployeesJPQL() {
        return employeeRepository.getAllEmployeesJPQL();
    }

    public List<Employee> getAllEmployeesHQL() {
        return employeeRepository.getAllEmployeesHQL();
    }

    public int copyEmployeeUsingHQLInsert(int sourceEmployeeId, String newName) {
        return employeeRepository.copyEmployeeUsingHQLInsert(sourceEmployeeId, newName);
    }
}

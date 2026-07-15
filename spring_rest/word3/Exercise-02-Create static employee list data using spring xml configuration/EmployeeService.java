import java.util.ArrayList;

/**
 * EmployeeService.java
 *
 * Service layer.
 *
 * Requirements covered:
 *  - Annotated with @Service (changed from @Component, as instructed)
 *  - getAllEmployees() invokes employeeDao.getAllEmployees() and
 *    returns the employee list
 *  - getAllEmployees() is annotated with @Transactional
 */
@Service
public class EmployeeService {

    private final EmployeeDao employeeDao;

    public EmployeeService(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Transactional
    public ArrayList<Employee> getAllEmployees() {
        return employeeDao.getAllEmployees();
    }
}

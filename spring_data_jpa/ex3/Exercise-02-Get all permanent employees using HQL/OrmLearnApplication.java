/*
 * OrmLearnApplication.java
 * -------------------------
 * Application entry point for Hands-on 2. Invokes
 * EmployeeService.getAllPermanentEmployees() and logs each employee plus
 * their skill list, exactly as the sample test method in the document does.
 */
import java.util.List;

public class OrmLearnApplication {

    private static final AppLogger LOGGER = AppLogger.getLogger(OrmLearnApplication.class);

    private static final EmployeeRepository employeeRepository = new EmployeeRepository();
    private static final EmployeeService employeeService = new EmployeeService(employeeRepository);

    public static void main(String[] args) {
        try {
            testGetAllPermanentEmployees();
        } finally {
            JPAUtil.shutdown();
        }
    }

    public static void testGetAllPermanentEmployees() {
        LOGGER.info("Start");
        List<Employee> employees = employeeService.getAllPermanentEmployees();
        LOGGER.debug("Permanent Employees:{}", employees);
        employees.forEach(e -> LOGGER.debug("Skills:{}", e.getSkillList()));
        LOGGER.info("End");
    }
}

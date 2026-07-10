/*
 * OrmLearnApplication.java
 * -------------------------
 * Application entry point for Hands-on 5 - tests the native-query based
 * repository method.
 */
import java.util.List;

public class OrmLearnApplication {

    private static final AppLogger LOGGER = AppLogger.getLogger(OrmLearnApplication.class);

    private static final EmployeeRepository employeeRepository = new EmployeeRepository();
    private static final EmployeeService employeeService = new EmployeeService(employeeRepository);

    public static void main(String[] args) {
        try {
            testGetAllEmployeesNative();
        } finally {
            JPAUtil.shutdown();
        }
    }

    private static void testGetAllEmployeesNative() {
        LOGGER.info("Start - testGetAllEmployeesNative");
        List<Employee> employees = employeeService.getAllEmployeesNative();
        employees.forEach(e -> LOGGER.debug("Employee (native):{}", e));
        LOGGER.info("End - testGetAllEmployeesNative");
    }
}

/*
 * OrmLearnApplication.java
 * -------------------------
 * Application entry point (mirrors the OrmLearnApplication class referred
 * to throughout the hands-on document). Wires together the repository and
 * service objects and runs each test method in turn.
 */
import java.util.List;

public class OrmLearnApplication {

    private static final AppLogger LOGGER = AppLogger.getLogger(OrmLearnApplication.class);

    // Manual dependency wiring (no Spring container available)
    private static final EmployeeRepository employeeRepository = new EmployeeRepository();
    private static final EmployeeService employeeService = new EmployeeService(employeeRepository);

    public static void main(String[] args) {
        try {
            testJPQLQuery();
            testHQLQuery();
            testHQLOnlyInsert();
        } finally {
            // Release the EntityManagerFactory before the JVM exits
            JPAUtil.shutdown();
        }
    }

    /**
     * Demonstrates the strict JPQL form: "SELECT e FROM Employee e"
     */
    private static void testJPQLQuery() {
        LOGGER.info("Start - testJPQLQuery (JPQL: SELECT e FROM Employee e)");
        List<Employee> employees = employeeService.getAllEmployeesJPQL();
        employees.forEach(e -> LOGGER.debug("JPQL result -> {}", e));
        LOGGER.info("End - testJPQLQuery");
    }

    /**
     * Demonstrates the HQL-only shorthand: "FROM Employee" (no SELECT, no alias)
     */
    private static void testHQLQuery() {
        LOGGER.info("Start - testHQLQuery (HQL shorthand: FROM Employee)");
        List<Employee> employees = employeeService.getAllEmployeesHQL();
        employees.forEach(e -> LOGGER.debug("HQL result -> {}", e));
        LOGGER.info("End - testHQLQuery");
    }

    /**
     * Demonstrates the HQL-only bulk INSERT ... SELECT statement, which has
     * no JPQL equivalent at all.
     */
    private static void testHQLOnlyInsert() {
        LOGGER.info("Start - testHQLOnlyInsert (HQL: INSERT INTO Employee ... SELECT ...)");
        int rows = employeeService.copyEmployeeUsingHQLInsert(1, "Arun Kumar (Copy)");
        LOGGER.debug("Rows inserted -> {}", rows);
        LOGGER.info("End - testHQLOnlyInsert");
    }
}

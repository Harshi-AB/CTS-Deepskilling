/*
 * OrmLearnApplication.java
 * -------------------------
 * Application entry point for Hands-on 4 - tests both the unfiltered and
 * department-filtered average salary queries.
 */
public class OrmLearnApplication {

    private static final AppLogger LOGGER = AppLogger.getLogger(OrmLearnApplication.class);

    private static final EmployeeRepository employeeRepository = new EmployeeRepository();
    private static final EmployeeService employeeService = new EmployeeService(employeeRepository);

    public static void main(String[] args) {
        try {
            testGetAverageSalary();
            testGetAverageSalaryByDepartment();
        } finally {
            JPAUtil.shutdown();
        }
    }

    private static void testGetAverageSalary() {
        LOGGER.info("Start - testGetAverageSalary");
        double avg = employeeService.getAverageSalary();
        LOGGER.debug("Average salary (all employees):{}", avg);
        LOGGER.info("End - testGetAverageSalary");
    }

    private static void testGetAverageSalaryByDepartment() {
        LOGGER.info("Start - testGetAverageSalaryByDepartment");
        int departmentId = 1; // Engineering
        double avg = employeeService.getAverageSalary(departmentId);
        LOGGER.debug("Average salary (department id=1):{}", avg);
        LOGGER.info("End - testGetAverageSalaryByDepartment");
    }
}

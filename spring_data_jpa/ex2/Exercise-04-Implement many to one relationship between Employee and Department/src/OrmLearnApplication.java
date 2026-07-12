import java.util.Date;

/**
 * Entry point for Exercise 04. Follows the hands-on exactly:
 * static EmployeeService / DepartmentService references, wired in main(),
 * and testGetEmployee() / testAddEmployee() / testUpdateEmployee() as
 * described in the document (no Spring container is used, so the
 * services are constructed directly instead of being pulled "from the
 * context").
 */
public class OrmLearnApplication {

    private static final Logger LOGGER = Logger.getLogger(OrmLearnApplication.class);

    private static EmployeeService employeeService;
    private static DepartmentService departmentService;

    public static void main(String[] args) {
        employeeService = new EmployeeService();
        departmentService = new DepartmentService();

        testGetEmployee();
        testAddEmployee();
        testUpdateEmployee();
    }

    /** Gets employee id 1 along with its department (ManyToOne is EAGER by default). */
    private static void testGetEmployee() {
        LOGGER.info("Start");
        Employee employee = employeeService.get(1);
        LOGGER.debug("Employee:{}", employee);
        LOGGER.debug("Department:{}", employee.getDepartment());
        LOGGER.info("End");
    }

    /** Creates a new employee assigned to department id 1 and saves it. */
    private static void testAddEmployee() {
        LOGGER.info("Start");
        Employee employee = new Employee();
        employee.setName("Emma Wilson");
        employee.setSalary(68000.00);
        employee.setPermanent(true);
        employee.setDateOfBirth(new Date());

        Department department = departmentService.get(1);
        employee.setDepartment(department);

        employeeService.save(employee);
        LOGGER.debug("Employee:{}", employee);
        LOGGER.info("End");
    }

    /** Loads employee id 2, moves it to a different department, and saves the update. */
    private static void testUpdateEmployee() {
        LOGGER.info("Start");
        Employee employee = employeeService.get(2);

        Department department = departmentService.get(3);
        employee.setDepartment(department);

        employeeService.save(employee);
        LOGGER.debug("Employee:{}", employee);
        LOGGER.info("End");
    }
}

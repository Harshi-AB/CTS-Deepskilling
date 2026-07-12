/**
 * Entry point for Exercise 05. Follows the hands-on's two-step procedure:
 *
 *  Step 1 (testGetDepartment): Department.employeeList is declared
 *  @OneToMany(mappedBy = "department") with no explicit fetch - which
 *  defaults to FetchType.LAZY per the JPA spec. Calling
 *  department.getEmployeeList() after the department has already been
 *  loaded (and, with real Hibernate, after the session/EntityManager is
 *  closed) throws LazyInitializationException.
 *
 *  Step 2 (testGetDepartmentEager): after editing Department.employeeList
 *  to add fetch = FetchType.EAGER (represented here by the separate
 *  DepartmentEager class, so both steps are visible in one run without
 *  hand-editing the file), employeeList is populated up front and no
 *  exception occurs.
 */
public class OrmLearnApplication {

    private static final Logger LOGGER = Logger.getLogger(OrmLearnApplication.class);

    private static DepartmentService departmentService;
    private static DepartmentEagerService departmentEagerService;

    public static void main(String[] args) {
        departmentService = new DepartmentService();
        departmentEagerService = new DepartmentEagerService();

        testGetDepartment();
        testGetDepartmentEager();
    }

    /** Step 1: @OneToMany default fetch = LAZY -> getEmployeeList() throws LazyInitializationException. */
    private static void testGetDepartment() {
        LOGGER.info("Start");
        // department id 1 (Engineering) has more than one employee
        Department department = departmentService.get(1);
        LOGGER.debug("Department:{}", department);
        try {
            LOGGER.debug("Employees:{}", department.getEmployeeList());
        } catch (LazyInitializationException e) {
            System.out.println("Caught expected LazyInitializationException: " + e.getMessage());
        }
        LOGGER.info("End");
    }

    /** Step 2: fetch = FetchType.EAGER -> employeeList is already populated, no exception. */
    private static void testGetDepartmentEager() {
        LOGGER.info("Start");
        DepartmentEager department = departmentEagerService.get(1);
        LOGGER.debug("Department:{}", department);
        LOGGER.debug("Employees:{}", department.getEmployeeList());
        LOGGER.info("End");
    }
}

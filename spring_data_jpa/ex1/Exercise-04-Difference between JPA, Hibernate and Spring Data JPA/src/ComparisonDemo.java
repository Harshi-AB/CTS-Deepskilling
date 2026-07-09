import java.util.List;

/**
 * ComparisonDemo
 * --------------
 * Demonstrates Hands-on 4's core point - JPA is a specification (JSR 338)
 * with no implementation of its own; Hibernate is an ORM tool that
 * implements JPA; Spring Data JPA is a further abstraction over an
 * implementation like Hibernate that removes boilerplate.
 *
 * Both code paths below end up doing the same thing - persisting an
 * Employee row - but the line count and manual bookkeeping differ hugely.
 */
public class ComparisonDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComparisonDemo.class);

    public static void main(String[] args) throws Exception {

        LOGGER.info("===== JPA is a specification; Hibernate is an implementation of it =====");

        // ---- 1) The plain Hibernate way (manual Session/Transaction handling) ----
        AnnotationConfiguration configuration = new AnnotationConfiguration();
        configuration.configure();
        configuration.addAnnotatedClass(Employee.class);
        SessionFactory factory = configuration.buildSessionFactory();

        HibernateEmployeeDao hibernateDao = new HibernateEmployeeDao(factory);
        LOGGER.info("--- Hibernate way: HibernateEmployeeDao.addEmployee() ---");
        Integer hibernateEmpId = hibernateDao.addEmployee(new Employee("Zara", "Ali", 1000));
        LOGGER.info("Employee persisted via plain Hibernate, id={}", hibernateEmpId);

        // ---- 2) The Spring Data JPA way (repository + service, no manual session code) ----
        LOGGER.info("--- Spring Data JPA way: EmployeeService.addEmployee() ---");
        ApplicationContext context = SpringApplication.run(ComparisonDemo.class, args);
        EmployeeRepository employeeRepository = context.registerRepository(EmployeeRepository.class);
        EmployeeService employeeService = context.registerBean(new EmployeeService());

        Employee springEmployee = new Employee("Daisy", "Das", 5000);
        employeeService.addEmployee(springEmployee);
        LOGGER.info("Employee persisted via Spring Data JPA, id={}", springEmployee.getId());

        // ---- Verify both rows landed in the same simulated EMPLOYEE table ----
        List<Employee> all = employeeRepository.findAll();
        LOGGER.debug("All employees in table: {}", all);

        factory.close();

        LOGGER.info("===== Summary =====");
        LOGGER.info("Hibernate way   : {} lines of session/transaction bookkeeping per method", 9);
        LOGGER.info("Spring Data JPA : {} line - employeeRepository.save(employee)", 1);
    }
}

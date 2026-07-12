/**
 * Service layer for Employee, as specified in the hands-on:
 * annotated with @Service, wraps EmployeeRepository, and exposes
 * @Transactional get()/save() methods that log entry/exit.
 */
@Service
public class EmployeeService {

    private static final Logger LOGGER = Logger.getLogger(EmployeeService.class);

    // "autowired" - built once via RepositoryFactory since no Spring container is used
    private final EmployeeRepository employeeRepository =
            RepositoryFactory.create(EmployeeRepository.class, Employee.class);

    @Transactional
    public Employee get(int id) {
        LOGGER.info("Start");
        Employee employee = employeeRepository.findById(id).get();
        LOGGER.info("End");
        return employee;
    }

    @Transactional
    public void save(Employee employee) {
        LOGGER.info("Start");
        employeeRepository.save(employee);
        LOGGER.info("End");
    }
}

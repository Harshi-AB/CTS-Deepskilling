/** Service layer for Employee - @Service, wraps EmployeeRepository. */
@Service
public class EmployeeService {

    private static final Logger LOGGER = Logger.getLogger(EmployeeService.class);

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

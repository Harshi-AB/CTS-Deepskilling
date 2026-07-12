/**
 * Service layer for EmployeeEager - represents the "fixed" EmployeeService
 * after switching @ManyToMany's fetch to FetchType.EAGER, as instructed
 * by the hands-on.
 */
@Service
public class EmployeeEagerService {

    private static final Logger LOGGER = Logger.getLogger(EmployeeEagerService.class);

    private final EmployeeEagerRepository employeeEagerRepository =
            RepositoryFactory.create(EmployeeEagerRepository.class, EmployeeEager.class);

    @Transactional
    public EmployeeEager get(int id) {
        LOGGER.info("Start");
        EmployeeEager employee = employeeEagerRepository.findById(id).get();
        LOGGER.info("End");
        return employee;
    }
}

/**
 * Service layer for Department, following the same @Service /
 * @Transactional pattern shown for EmployeeService in the hands-on.
 */
@Service
public class DepartmentService {

    private static final Logger LOGGER = Logger.getLogger(DepartmentService.class);

    private final DepartmentRepository departmentRepository =
            RepositoryFactory.create(DepartmentRepository.class, Department.class);

    @Transactional
    public Department get(int id) {
        LOGGER.info("Start");
        Department department = departmentRepository.findById(id).get();
        LOGGER.info("End");
        return department;
    }

    @Transactional
    public void save(Department department) {
        LOGGER.info("Start");
        departmentRepository.save(department);
        LOGGER.info("End");
    }
}

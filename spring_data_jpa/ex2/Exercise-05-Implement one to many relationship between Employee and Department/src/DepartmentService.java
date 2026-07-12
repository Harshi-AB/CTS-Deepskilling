/** Service layer for Department (LAZY employeeList) - @Service, wraps DepartmentRepository. */
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

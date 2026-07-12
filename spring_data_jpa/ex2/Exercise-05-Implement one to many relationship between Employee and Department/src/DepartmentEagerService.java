/**
 * Service layer for DepartmentEager (EAGER employeeList) - represents the
 * "fixed" version of DepartmentService after switching @OneToMany's fetch
 * to FetchType.EAGER, as instructed by the hands-on.
 */
@Service
public class DepartmentEagerService {

    private static final Logger LOGGER = Logger.getLogger(DepartmentEagerService.class);

    private final DepartmentEagerRepository departmentEagerRepository =
            RepositoryFactory.create(DepartmentEagerRepository.class, DepartmentEager.class);

    @Transactional
    public DepartmentEager get(int id) {
        LOGGER.info("Start");
        DepartmentEager department = departmentEagerRepository.findById(id).get();
        LOGGER.info("End");
        return department;
    }
}

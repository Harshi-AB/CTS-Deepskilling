import java.util.ArrayList;

/**
 * DepartmentService.java
 *
 * Service layer for departments.
 *
 * Requirements covered:
 *  - Annotated with @Service
 *  - getAllDepartments() invokes departmentDao.getAllDepartments()
 */
@Service
public class DepartmentService {

    private final DepartmentDao departmentDao;

    public DepartmentService(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }

    @Transactional
    public ArrayList<Department> getAllDepartments() {
        return departmentDao.getAllDepartments();
    }
}

/**
 * EmployeeService
 * ---------------
 * The exact Spring Data JPA snippet from Hands-on 4:
 *
 *   @Autowired
 *   private EmployeeRepository employeeRepository;
 *
 *   @Transactional
 *   public void addEmployee(Employee employee) {
 *     employeeRepository.save(employee);
 *   }
 *
 * Compare the size of this method to HibernateEmployeeDao.addEmployee()
 * below - that difference IS the boilerplate Spring Data JPA removes.
 */
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
    }
}

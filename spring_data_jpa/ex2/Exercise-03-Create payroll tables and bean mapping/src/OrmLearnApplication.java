import java.util.List;

/**
 * Entry point for Exercise 03. Wires up EmployeeRepository,
 * DepartmentRepository and SkillRepository and verifies the bean mapping
 * by loading and printing every row of each table.
 */
public class OrmLearnApplication {

    private static final Logger LOGGER = Logger.getLogger(OrmLearnApplication.class);

    private static EmployeeRepository employeeRepository;
    private static DepartmentRepository departmentRepository;
    private static SkillRepository skillRepository;

    public static void main(String[] args) throws Exception {
        employeeRepository = RepositoryFactory.create(EmployeeRepository.class, Employee.class);
        departmentRepository = RepositoryFactory.create(DepartmentRepository.class, Department.class);
        skillRepository = RepositoryFactory.create(SkillRepository.class, Skill.class);

        testListDepartments();
        testListSkills();
        testListEmployees();
    }

    private static void testListDepartments() {
        LOGGER.info("Start - testListDepartments");
        List<Department> departments = departmentRepository.findAll();
        for (Department d : departments) {
            System.out.println(d);
        }
        LOGGER.info("End - testListDepartments");
    }

    private static void testListSkills() {
        LOGGER.info("Start - testListSkills");
        List<Skill> skills = skillRepository.findAll();
        for (Skill s : skills) {
            System.out.println(s);
        }
        LOGGER.info("End - testListSkills");
    }

    private static void testListEmployees() {
        LOGGER.info("Start - testListEmployees");
        List<Employee> employees = employeeRepository.findAll();
        for (Employee e : employees) {
            System.out.println(e);
        }
        LOGGER.info("End - testListEmployees");
    }
}

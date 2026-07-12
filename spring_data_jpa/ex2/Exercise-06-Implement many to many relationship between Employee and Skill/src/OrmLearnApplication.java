import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Entry point for Exercise 06. Follows the hands-on's flow:
 *
 *  Step 1 (testGetEmployee): Employee.skillList is @ManyToMany with no
 *  explicit fetch - defaults to FetchType.LAZY per the JPA spec - so
 *  employee.getSkillList() throws LazyInitializationException.
 *
 *  Step 2 (testGetEmployeeEager): after adding fetch = FetchType.EAGER to
 *  Employee.skillList (represented by the separate EmployeeEager class so
 *  both steps run in one program), the skill list is already populated.
 *
 *  Step 3 (testAddSkillToEmployee): adds a new employee/skill pairing
 *  through the (now-fixed) EAGER employee, and writes the join-table row.
 */
public class OrmLearnApplication {

    private static final Logger LOGGER = Logger.getLogger(OrmLearnApplication.class);

    private static EmployeeService employeeService;
    private static EmployeeEagerService employeeEagerService;
    private static SkillService skillService;

    public static void main(String[] args) {
        employeeService = new EmployeeService();
        employeeEagerService = new EmployeeEagerService();
        skillService = new SkillService();

        testGetEmployee();
        testGetEmployeeEager();
        testAddSkillToEmployee();
    }

    /** Step 1: @ManyToMany default fetch = LAZY -> getSkillList() throws LazyInitializationException. */
    private static void testGetEmployee() {
        LOGGER.info("Start");
        Employee employee = employeeService.get(1);
        LOGGER.debug("Employee:{}", employee);
        LOGGER.debug("Department:{}", employee.getDepartment());
        try {
            LOGGER.debug("Skills:{}", employee.getSkillList());
        } catch (LazyInitializationException e) {
            System.out.println("Caught expected LazyInitializationException: " + e.getMessage());
        }
        LOGGER.info("End");
    }

    /** Step 2: fetch = FetchType.EAGER -> skillList is already populated, no exception. */
    private static void testGetEmployeeEager() {
        LOGGER.info("Start");
        EmployeeEager employee = employeeEagerService.get(1);
        LOGGER.debug("Employee:{}", employee);
        LOGGER.debug("Skills:{}", employee.getSkillList());
        LOGGER.info("End");
    }

    /** Step 3: assign an existing skill to an employee that doesn't already have it. */
    private static void testAddSkillToEmployee() {
        LOGGER.info("Start");
        // employee id 3 (Carol White) and skill id 4 (Project Management)
        // do not yet have a relationship in the seed data
        EmployeeEager employee = employeeEagerService.get(3);
        Skill skill = skillService.get(4);

        Set<Skill> skillList = new HashSet<>(employee.getSkillList());
        skillList.add(skill);
        employee.setSkillList(skillList);

        insertEmployeeSkillLink(employee.getId(), skill.getId());

        EmployeeEager reloaded = employeeEagerService.get(3);
        LOGGER.debug("Employee skills after update:{}", reloaded.getSkillList());
        LOGGER.info("End");
    }

    /**
     * Writes the join-table row directly - persisting @ManyToMany
     * collection changes through the join table, since the mini-ORM's
     * generic save() only handles an entity's own columns.
     */
    private static void insertEmployeeSkillLink(int employeeId, int skillId) {
        String sql = "INSERT IGNORE INTO employee_skill (es_em_id, es_sk_id) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ps.setInt(2, skillId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

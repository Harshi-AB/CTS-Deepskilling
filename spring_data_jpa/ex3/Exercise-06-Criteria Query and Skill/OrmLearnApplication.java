/*
 * OrmLearnApplication.java
 * -------------------------
 * Application entry point for Hands-on 6. Runs two dynamic Criteria Query
 * scenarios:
 *   1) Retail laptop search - user selects only SOME of the 7 filter
 *      categories described in the document (here: RAM >= 8GB and
 *      OS = "Windows 11"); the other filters are left null/unselected.
 *   2) Employee search filtered by Skill - finds all employees who know
 *      the "Java" skill AND are permanent staff.
 */
import java.util.List;

public class OrmLearnApplication {

    private static final AppLogger LOGGER = AppLogger.getLogger(OrmLearnApplication.class);

    private static final ProductRepository productRepository = new ProductRepository();
    private static final EmployeeCriteriaRepository employeeCriteriaRepository = new EmployeeCriteriaRepository();
    private static final CriteriaQueryService criteriaQueryService =
            new CriteriaQueryService(productRepository, employeeCriteriaRepository);

    public static void main(String[] args) {
        try {
            testProductCriteriaSearch();
            testEmployeeCriteriaSearchBySkill();
        } finally {
            JPAUtil.shutdown();
        }
    }

    /**
     * Simulates a user who only ticked "RAM >= 8GB" and "OS = Windows 11"
     * on the retail filter panel - every other criteria field stays null,
     * and the repository must silently skip predicates for those.
     */
    private static void testProductCriteriaSearch() {
        LOGGER.info("Start - testProductCriteriaSearch");

        ProductSearchCriteria criteria = new ProductSearchCriteria()
                .setMinRamSizeGb(8)
                .setOperatingSystem("Windows 11");

        List<Product> products = criteriaQueryService.searchProducts(criteria);
        products.forEach(p -> LOGGER.debug("Matched product -> {}", p));

        LOGGER.info("End - testProductCriteriaSearch");
    }

    /**
     * Finds every permanent employee who has the "Java" skill.
     */
    private static void testEmployeeCriteriaSearchBySkill() {
        LOGGER.info("Start - testEmployeeCriteriaSearchBySkill");

        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria()
                .setPermanent(true)
                .setSkillName("Java");

        List<Employee> employees = criteriaQueryService.searchEmployees(criteria);
        employees.forEach(e -> LOGGER.debug("Matched employee -> {}", e));

        LOGGER.info("End - testEmployeeCriteriaSearchBySkill");
    }
}

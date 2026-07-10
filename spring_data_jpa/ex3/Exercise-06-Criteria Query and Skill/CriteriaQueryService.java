/*
 * CriteriaQueryService.java
 * --------------------------
 * Service layer wrapping both Criteria Query repositories used in this
 * exercise (Product search and Employee/Skill search).
 */
import java.util.List;

public class CriteriaQueryService {

    private final ProductRepository productRepository;
    private final EmployeeCriteriaRepository employeeCriteriaRepository;

    public CriteriaQueryService(ProductRepository productRepository,
                                 EmployeeCriteriaRepository employeeCriteriaRepository) {
        this.productRepository = productRepository;
        this.employeeCriteriaRepository = employeeCriteriaRepository;
    }

    public List<Product> searchProducts(ProductSearchCriteria criteria) {
        return productRepository.search(criteria);
    }

    public List<Employee> searchEmployees(EmployeeSearchCriteria criteria) {
        return employeeCriteriaRepository.search(criteria);
    }
}

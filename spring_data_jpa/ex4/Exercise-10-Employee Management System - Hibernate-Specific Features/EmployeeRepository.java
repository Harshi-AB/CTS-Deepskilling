import java.math.BigDecimal;
import java.util.List;

/**
 * EmployeeRepository.java
 *
 * Exercise 05 - Defining Query Methods
 * ---------------------------------------------
 * Beyond the inherited CRUD methods, this interface now declares
 * QUERY METHODS whose SQL is derived purely from the method name by
 * DerivedQueryExecutor - no implementation body, exactly like real
 * Spring Data JPA.
 */
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findByActiveTrue();

    List<Employee> findByActiveFalse();

    List<Employee> findByNameContaining(String keyword);

    List<Employee> findBySalaryGreaterThan(BigDecimal salary);

    List<Employee> findByActiveAndSalaryGreaterThan(boolean active, BigDecimal salary);

    List<Employee> findByOrderBySalaryDesc();

    long countByActive(boolean active);

    boolean existsByEmail(String email);

    // ---- Exercise 08: Creating Projections ----
    // The trailing Class<T> parameter tells RepositoryFactory which
    // projection interface to wrap each result in (dynamic projections,
    // same technique real Spring Data JPA uses).

    <T> List<T> findByActiveTrue(Class<T> type);

    <T> List<T> findBySalaryGreaterThan(BigDecimal salary, Class<T> type);
}

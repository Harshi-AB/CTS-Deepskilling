/**
 * EmployeeRepository
 * ------------------
 * The exact Spring Data JPA snippet from Hands-on 4:
 *
 *   public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
 *   }
 *
 * No implementation class, no boilerplate session/transaction handling -
 * JpaRepositoryProxyFactory builds a working proxy for this at runtime.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}

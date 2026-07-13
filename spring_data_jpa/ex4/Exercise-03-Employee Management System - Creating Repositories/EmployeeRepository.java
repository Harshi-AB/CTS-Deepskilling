/**
 * EmployeeRepository.java
 *
 * Exercise 03 - Creating Repositories
 * -------------------------------------
 * The repository the rest of the application will actually use.
 * Exactly like real Spring Data JPA, the developer only writes this
 * INTERFACE - no implementation class. A dynamic proxy (built in
 * Exercise 04 with RepositoryFactory) will supply the runtime behaviour.
 *
 * Extending JpaRepository<Employee, Integer> automatically inherits
 * save(), findById(), findAll(), deleteById(), count(), etc.
 */
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    // Custom derived-query methods are added in Exercise 05.
}

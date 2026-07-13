/**
 * DepartmentRepository.java
 *
 * Exercise 03 - Creating Repositories
 * -------------------------------------
 * A second repository to prove the hierarchy is reusable/generic across
 * multiple entities, not hard-coded to Employee.
 */
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}

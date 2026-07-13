import java.math.BigDecimal;

/**
 * EmployeeSummary.java
 *
 * Exercise 08 - Creating Projections
 * -----------------------------------------------------
 * A CLOSED PROJECTION - mirrors how you'd write an interface-based
 * projection in real Spring Data JPA. Only exposes a subset of Employee's
 * fields (empId, name, salary), which is useful when a caller (e.g. a
 * REST endpoint) doesn't need the whole entity graph.
 */
public interface EmployeeSummary {
    int getEmpId();
    String getName();
    BigDecimal getSalary();
}

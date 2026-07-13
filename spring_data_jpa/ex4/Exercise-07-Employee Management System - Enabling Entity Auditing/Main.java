import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Main.java
 *
 * Exercise 07 - Enabling Entity Auditing
 * -----------------------------------------------------
 * Saves a new employee (createdAt/updatedAt get stamped automatically),
 * then updates it (only updatedAt changes) - proving AuditingEntityListener
 * fires correctly on both code paths without the developer ever touching
 * the timestamp fields directly.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Exercise 07: Enabling Entity Auditing ===\n");

        DepartmentRepository departmentRepository =
                RepositoryFactory.getRepository(DepartmentRepository.class, Department.class);
        EmployeeRepository employeeRepository =
                RepositoryFactory.getRepository(EmployeeRepository.class, Employee.class);

        try {
            Department eng = departmentRepository.save(new Department("Engineering", "Bangalore"));

            Employee emp = new Employee("Harshitha R", "harshitha@example.com",
                    new BigDecimal("75000"), LocalDate.now(), true, eng);
            emp = employeeRepository.save(emp);
            System.out.println("After insert:");
            System.out.println("  createdAt = " + emp.getCreatedAt());
            System.out.println("  updatedAt = " + emp.getUpdatedAt());

            Thread.sleep(1000); // just so the two timestamps are visibly different

            emp.setSalary(new BigDecimal("81000"));
            emp = employeeRepository.save(emp);
            System.out.println("\nAfter update:");
            System.out.println("  createdAt = " + emp.getCreatedAt() + "  (unchanged)");
            System.out.println("  updatedAt = " + emp.getUpdatedAt() + "  (refreshed)");

        } catch (Exception e) {
            System.err.println("Demo failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

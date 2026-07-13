import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Main.java
 *
 * Exercise 05 - Defining Query Methods
 * ---------------------------------------------
 * Seeds a few employees, then exercises every derived query method
 * declared on EmployeeRepository to prove the method-name-to-SQL
 * translation works end to end against MySQL.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Exercise 05: Defining Query Methods ===\n");

        DepartmentRepository departmentRepository =
                RepositoryFactory.getRepository(DepartmentRepository.class, Department.class);
        EmployeeRepository employeeRepository =
                RepositoryFactory.getRepository(EmployeeRepository.class, Employee.class);

        try {
            Department eng = departmentRepository.save(new Department("Engineering", "Bangalore"));

            employeeRepository.save(new Employee("Arun Kumar", "arun@example.com",
                    new BigDecimal("60000"), LocalDate.now(), true, eng));
            employeeRepository.save(new Employee("Divya Sree", "divya@example.com",
                    new BigDecimal("95000"), LocalDate.now(), true, eng));
            employeeRepository.save(new Employee("Karthik R", "karthik@example.com",
                    new BigDecimal("45000"), LocalDate.now(), false, eng));

            System.out.println("Active employees: " + employeeRepository.findByActiveTrue());
            System.out.println("\nName contains 'Sree': " + employeeRepository.findByNameContaining("Sree"));
            System.out.println("\nSalary > 50000: " + employeeRepository.findBySalaryGreaterThan(new BigDecimal("50000")));
            System.out.println("\nActive AND salary > 50000: "
                    + employeeRepository.findByActiveAndSalaryGreaterThan(true, new BigDecimal("50000")));
            System.out.println("\nAll ordered by salary desc: " + employeeRepository.findByOrderBySalaryDesc());
            System.out.println("\ncountByActive(true): " + employeeRepository.countByActive(true));
            System.out.println("existsByEmail('arun@example.com'): "
                    + employeeRepository.existsByEmail("arun@example.com"));

        } catch (Exception e) {
            System.err.println("Demo failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

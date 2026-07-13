import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Main.java
 *
 * Exercise 08 - Creating Projections
 * -----------------------------------------------------
 * Fetches the SAME underlying employee rows through two different
 * projection shapes (EmployeeSummary and EmployeeContactInfo) to prove
 * one entity can be viewed multiple ways without loading extra data
 * into application-visible fields it doesn't need.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Exercise 08: Creating Projections ===\n");

        DepartmentRepository departmentRepository =
                RepositoryFactory.getRepository(DepartmentRepository.class, Department.class);
        EmployeeRepository employeeRepository =
                RepositoryFactory.getRepository(EmployeeRepository.class, Employee.class);

        try {
            Department eng = departmentRepository.save(new Department("Engineering", "Bangalore"));
            employeeRepository.save(new Employee("Harshitha R", "harshitha@example.com",
                    new BigDecimal("75000"), LocalDate.now(), true, eng));
            employeeRepository.save(new Employee("Arun Kumar", "arun@example.com",
                    new BigDecimal("60000"), LocalDate.now(), true, eng));

            System.out.println("EmployeeSummary projection (empId, name, salary only):");
            List<EmployeeSummary> summaries = employeeRepository.findByActiveTrue(EmployeeSummary.class);
            for (EmployeeSummary s : summaries) {
                System.out.println("  #" + s.getEmpId() + " " + s.getName() + " - " + s.getSalary());
            }

            System.out.println("\nEmployeeContactInfo projection (name, email only) for salary > 50000:");
            List<EmployeeContactInfo> contacts =
                    employeeRepository.findBySalaryGreaterThan(new BigDecimal("50000"), EmployeeContactInfo.class);
            for (EmployeeContactInfo c : contacts) {
                System.out.println("  " + c.getName() + " <" + c.getEmail() + ">");
            }

        } catch (Exception e) {
            System.err.println("Demo failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

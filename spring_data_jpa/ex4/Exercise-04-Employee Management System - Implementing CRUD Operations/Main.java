import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Main.java
 *
 * Exercise 04 - Implementing CRUD Operations
 * ---------------------------------------------
 * End-to-end demo: obtain a dynamic-proxy EmployeeRepository from
 * RepositoryFactory and exercise every CRUD operation against a real
 * MySQL database, exactly as application code would with genuine
 * Spring Data JPA.
 *
 * Prerequisite: run schema.sql first, and make sure DBConfig.java
 * points at your MySQL instance with mysql-connector-j on the classpath.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Exercise 04: Implementing CRUD Operations ===\n");

        DepartmentRepository departmentRepository =
                RepositoryFactory.getRepository(DepartmentRepository.class, Department.class);
        EmployeeRepository employeeRepository =
                RepositoryFactory.getRepository(EmployeeRepository.class, Employee.class);

        try {
            // ---------- CREATE ----------
            Department engineering = new Department("Engineering", "Bangalore");
            engineering = departmentRepository.save(engineering);
            System.out.println("Saved department: " + engineering);

            Employee emp = new Employee("Harshitha R", "harshitha@example.com",
                    new BigDecimal("75000.00"), LocalDate.now(), true, engineering);
            emp = employeeRepository.save(emp);
            System.out.println("Saved employee: " + emp);

            // ---------- READ ----------
            Optional<Employee> found = employeeRepository.findById(emp.getEmpId());
            System.out.println("\nfindById(" + emp.getEmpId() + "): " + found.orElse(null));

            System.out.println("existsById: " + employeeRepository.existsById(emp.getEmpId()));
            System.out.println("count: " + employeeRepository.count());

            // ---------- UPDATE ----------
            emp.setSalary(new BigDecimal("82000.00"));
            employeeRepository.save(emp);
            System.out.println("\nAfter update: " + employeeRepository.findById(emp.getEmpId()).orElse(null));

            // ---------- LIST ----------
            List<Employee> all = employeeRepository.findAll();
            System.out.println("\nfindAll() -> " + all.size() + " employee(s)");
            all.forEach(e -> System.out.println("  " + e));

            // ---------- DELETE ----------
            employeeRepository.deleteById(emp.getEmpId());
            System.out.println("\nDeleted employee " + emp.getEmpId()
                    + " -> now exists? " + employeeRepository.existsById(emp.getEmpId()));

        } catch (Exception e) {
            System.err.println("Demo failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

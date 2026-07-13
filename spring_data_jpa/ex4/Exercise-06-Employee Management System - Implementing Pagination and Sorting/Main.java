import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Main.java
 *
 * Exercise 06 - Implementing Pagination and Sorting
 * -----------------------------------------------------
 * Seeds several employees, then demonstrates:
 *   1) findAll(Sort)      - all rows, sorted
 *   2) findAll(Pageable)  - one page at a time, sorted
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Exercise 06: Implementing Pagination and Sorting ===\n");

        DepartmentRepository departmentRepository =
                RepositoryFactory.getRepository(DepartmentRepository.class, Department.class);
        EmployeeRepository employeeRepository =
                RepositoryFactory.getRepository(EmployeeRepository.class, Employee.class);

        try {
            Department eng = departmentRepository.save(new Department("Engineering", "Bangalore"));

            String[] names = {"Arun", "Divya", "Karthik", "Meena", "Suresh", "Priya"};
            int salary = 40000;
            for (String n : names) {
                employeeRepository.save(new Employee(n, n.toLowerCase() + "@example.com",
                        new BigDecimal(salary), LocalDate.now(), true, eng));
                salary += 7000;
            }

            System.out.println("All employees sorted by salary DESC:");
            employeeRepository.findAll(Sort.by(Sort.Direction.DESC, "salary"))
                    .forEach(e -> System.out.println("  " + e.getName() + " - " + e.getSalary()));

            System.out.println("\nPage 1 (size 2), sorted by name ASC:");
            Page<Employee> page1 = employeeRepository.findAll(
                    PageRequest.of(0, 2, Sort.by("name")));
            System.out.println(page1);

            System.out.println("\nPage 2 (size 2), sorted by name ASC:");
            Page<Employee> page2 = employeeRepository.findAll(
                    PageRequest.of(1, 2, Sort.by("name")));
            System.out.println(page2);

        } catch (Exception e) {
            System.err.println("Demo failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Main.java
 *
 * Exercise 09 - Customizing Data Source Configuration
 * -----------------------------------------------------
 * Proves connections now come from the pool (availableConnections() count
 * dips while a connection is borrowed and recovers once it's "closed"),
 * then runs a normal save/find through EmployeeRepository exactly like
 * earlier exercises to show the switch to pooling is transparent.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Exercise 09: Customizing Data Source Configuration ===\n");

        System.out.println("Pool size from application.properties: "
                + DataSourceProperties.getInstance().getPoolSize());
        System.out.println("Available connections before borrowing: "
                + DataSourceConfig.getPool().availableConnections());

        try (var conn = DBConnectionUtil.getConnection()) {
            System.out.println("Borrowed one connection -> available now: "
                    + DataSourceConfig.getPool().availableConnections());
        }
        System.out.println("Connection released (proxy close()) -> available again: "
                + DataSourceConfig.getPool().availableConnections());

        DepartmentRepository departmentRepository =
                RepositoryFactory.getRepository(DepartmentRepository.class, Department.class);
        EmployeeRepository employeeRepository =
                RepositoryFactory.getRepository(EmployeeRepository.class, Employee.class);

        Department eng = departmentRepository.save(new Department("Engineering", "Bangalore"));
        Employee emp = employeeRepository.save(new Employee("Harshitha R", "harshitha@example.com",
                new BigDecimal("75000"), LocalDate.now(), true, eng));
        System.out.println("\nSaved through pooled connections: " + employeeRepository.findById(emp.getEmpId()).orElse(null));
    }
}

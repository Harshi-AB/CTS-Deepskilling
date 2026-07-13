import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Main.java
 *
 * Exercise 10 - Hibernate-Specific Features
 * -----------------------------------------------------
 * Demonstrates, in order:
 *   1) BATCH PROCESSING   - insert many employees in one round trip.
 *   2) FIRST-LEVEL CACHE  - loading the same id twice in one Session only
 *      hits the database once.
 *   3) SECOND-LEVEL CACHE - a brand-new Session still gets a cache hit
 *      for a row an earlier (now-closed) Session already loaded.
 *   4) DYNAMIC UPDATE     - changing one field only emits an UPDATE for
 *      that one column (dirty checking), and a no-op change emits no
 *      UPDATE at all.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Exercise 10: Hibernate-Specific Features ===\n");

        DepartmentRepository departmentRepository =
                RepositoryFactory.getRepository(DepartmentRepository.class, Department.class);

        Department eng = departmentRepository.save(new Department("Engineering", "Bangalore"));

        // ---------- 1) Batch processing ----------
        System.out.println("1) Batch inserting 5 employees in a single round trip...");
        List<Employee> batch = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            batch.add(new Employee("Employee " + i, "emp" + i + "@example.com",
                    new BigDecimal(40000 + i * 1000), LocalDate.now(), true, eng));
        }
        BatchInsertProcessor<Employee> batchProcessor = new BatchInsertProcessor<>(Employee.class);
        int[] batchResults = batchProcessor.insertAll(batch);
        System.out.println("   Batch executed, rows affected per statement: " + batchResults.length);

        // Grab a real id to use for the caching demo below.
        EmployeeRepository employeeRepository =
                RepositoryFactory.getRepository(EmployeeRepository.class, Employee.class);
        int sampleId = employeeRepository.findAll().get(0).getEmpId();

        // ---------- 2) First-level cache ----------
        System.out.println("\n2) First-level cache (same Session, same id, loaded twice):");
        try (Session session1 = new Session()) {
            session1.load(Employee.class, sampleId); // MISS -> hits DB, populates L1 + L2
            session1.load(Employee.class, sampleId); // HIT  -> served from L1
        }

        // ---------- 3) Second-level cache ----------
        System.out.println("\n3) Second-level cache (new Session, same id):");
        try (Session session2 = new Session()) {
            session2.load(Employee.class, sampleId); // HIT -> served from L2 even though session2 is brand-new

            // ---------- 4) Dynamic update / dirty checking ----------
            System.out.println("\n4) Dynamic update - changing only the salary field:");
            Employee emp = (Employee) session2.load(Employee.class, sampleId).orElseThrow();
            emp.setSalary(new BigDecimal("99999.00"));
            session2.update(emp); // should generate "UPDATE employee SET salary = ? ..." only

            System.out.println("\n   Calling update() again with NO changes:");
            session2.update(emp); // should print "no changed fields -> UPDATE skipped"
        }
    }
}

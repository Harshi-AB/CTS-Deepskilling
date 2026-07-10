/*
 * EmployeeRepository.java
 * ------------------------
 * Hands-on 5: Get all employees using a Native Query.
 *
 * Native queries are plain SQL sent straight to the database, bypassing
 * HQL/JPQL entirely. They map to Spring Data JPA's:
 *
 *   @Query(value = "SELECT * FROM employee", nativeQuery = true)
 *   List<Employee> getAllEmployeesNative();
 *
 * The plain-JPA equivalent is EntityManager.createNativeQuery(sql, EntityClass).
 *
 * IMPORTANT (per the document): native queries should be used sparingly.
 * They tie the code to a specific database's SQL dialect and lose the
 * database portability that HQL/JPQL provide - prefer HQL/JPQL whenever
 * possible and reserve native queries for cases HQL cannot express
 * (vendor-specific functions, complex reporting queries, etc.).
 */
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class EmployeeRepository {

    /**
     * Native SQL query - executed exactly as written against the
     * 'employee' MySQL table. Because the second argument is the entity
     * class, Hibernate maps each result row back into an Employee object
     * automatically (matching column names to @Column mappings).
     */
    @SuppressWarnings("unchecked")
    public List<Employee> getAllEmployeesNative() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Query query = em.createNativeQuery("SELECT * FROM employee", Employee.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}

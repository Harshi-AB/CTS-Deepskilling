/*
 * EmployeeRepository.java
 * ------------------------
 * DAO / Repository class (Repository design pattern) that centralises all
 * database access for the Employee entity.
 *
 * Hands-on 1 goal: demonstrate the difference between HQL (Hibernate Query
 * Language) and JPQL (Java Persistence Query Language).
 *
 *   - JPQL is a STRICT SUBSET of HQL: every valid JPQL query is valid HQL,
 *     but not every HQL query is valid JPQL.
 *   - Because Hibernate is the JPA provider here, EntityManager.createQuery()
 *     actually accepts the full HQL grammar - but to keep the two concepts
 *     conceptually separate we still write:
 *        * getAllEmployeesJPQL()   -> uses the strict JPQL form ("SELECT e FROM ...")
 *        * getAllEmployeesHQL()    -> uses an HQL-only shorthand ("FROM Employee")
 *        * copyEmployeeUsingHQLInsert() -> uses the HQL-only bulk INSERT statement,
 *          which has NO equivalent in the JPQL specification.
 */
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class EmployeeRepository {

    /**
     * JPQL form - always starts with SELECT <alias> FROM <Entity> <alias>.
     * This is valid under BOTH the JPQL and HQL grammars.
     */
    public List<Employee> getAllEmployeesJPQL() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Employee> query =
                    em.createQuery("SELECT e FROM Employee e", Employee.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * HQL shorthand form - "FROM Employee" without an explicit SELECT clause
     * and without an alias. This shorthand is legal in HQL but is NOT legal
     * JPQL (JPQL always requires the SELECT clause and an identification
     * variable).
     */
    public List<Employee> getAllEmployeesHQL() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Employee> query = em.createQuery("FROM Employee", Employee.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * HQL-only feature: bulk INSERT ... SELECT statement.
     * JPQL only supports bulk UPDATE and DELETE - it has no INSERT statement
     * at all. HQL additionally supports this "insert into ... select" form,
     * which copies data from existing rows into new rows in a single query.
     *
     * Here we duplicate one employee row (minus the id, which is generated)
     * with a new name, entirely inside the database via one HQL statement.
     */
    public int copyEmployeeUsingHQLInsert(int sourceEmployeeId, String newName) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            int rowsInserted = em.createQuery(
                            "INSERT INTO Employee (name, dateOfBirth, salary, permanent, department) " +
                                    "SELECT :newName, e.dateOfBirth, e.salary, e.permanent, e.department " +
                                    "FROM Employee e WHERE e.id = :sourceId")
                    .setParameter("newName", newName)
                    .setParameter("sourceId", sourceEmployeeId)
                    .executeUpdate();
            em.getTransaction().commit();
            return rowsInserted;
        } finally {
            em.close();
        }
    }
}

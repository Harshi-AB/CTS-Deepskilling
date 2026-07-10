/*
 * EmployeeRepository.java
 * ------------------------
 * Hands-on 2: Get all permanent employees using HQL, retrieving each
 * employee's department AND skill list in the SAME query.
 *
 * The document walks through 3 iterations while explaining "join" vs
 * "join fetch" - only the final, OPTIMAL version is kept here:
 *
 *   SELECT e FROM Employee e
 *   left join fetch e.department d
 *   left join fetch e.skillList
 *   WHERE e.permanent = 1
 *
 * Why this is the right version:
 *   1) "SELECT e FROM Employee e WHERE e.permanent = 1" alone only fetches
 *      the Employee row - department/skillList stay LAZY, so accessing
 *      them later triggers one extra SELECT per employee (N+1 problem).
 *   2) "left join e.department d left join e.skillList" (without "fetch")
 *      only joins the tables in SQL to support filtering; it does NOT
 *      populate the Java object graph. Accessing e.getSkillList()
 *      afterwards would either lazy-load again or throw an exception if
 *      the EntityManager/session is already closed.
 *   3) Adding "fetch" after each join tells Hibernate to actually populate
 *      those associations from the joined columns - resulting in exactly
 *      ONE SQL SELECT that returns employees + department + skills together.
 */
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class EmployeeRepository {

    /**
     * Optimised HQL: single query, "left join fetch" populates both the
     * department and skillList associations in one round trip to MySQL.
     */
    public List<Employee> getAllPermanentEmployees() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String hql = "SELECT e FROM Employee e " +
                    "left join fetch e.department d " +
                    "left join fetch e.skillList " +
                    "WHERE e.permanent = true";
            TypedQuery<Employee> query = em.createQuery(hql, Employee.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}

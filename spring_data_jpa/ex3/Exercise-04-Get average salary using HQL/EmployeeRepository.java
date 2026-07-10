/*
 * EmployeeRepository.java
 * ------------------------
 * Hands-on 4: Compute the average salary using the HQL aggregate function
 * AVG(), first across all employees, then filtered by department id using
 * a named parameter (:id) bound with @Param-equivalent setParameter().
 */
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class EmployeeRepository {

    /**
     * @Query(value = "SELECT AVG(e.salary) FROM Employee e")
     * double getAverageSalary();
     */
    public double getAverageSalary() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Double> query =
                    em.createQuery("SELECT AVG(e.salary) FROM Employee e", Double.class);
            Double result = query.getSingleResult();
            return result == null ? 0.0 : result;
        } finally {
            em.close();
        }
    }

    /**
     * @Query(value = "SELECT AVG(e.salary) FROM Employee e where e.department.id = :id")
     * double getAverageSalary(@Param("id") int id);
     *
     * Notice how the department id is referenced through the association
     * path "e.department.id" (not a raw FK column name) - HQL/JPQL navigate
     * object relationships, not table columns. The colon (:id) defines a
     * named parameter, bound below via setParameter("id", id).
     */
    public double getAverageSalary(int departmentId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Double> query = em.createQuery(
                    "SELECT AVG(e.salary) FROM Employee e WHERE e.department.id = :id", Double.class);
            query.setParameter("id", departmentId);
            Double result = query.getSingleResult();
            return result == null ? 0.0 : result;
        } finally {
            em.close();
        }
    }
}

/*
 * EmployeeCriteriaRepository.java
 * --------------------------------
 * Second Criteria Query demonstration for this exercise ("...and Skill" in
 * the title): dynamically filters Employees by department name, employment
 * type (permanent/contract), minimum salary, AND by whether the employee
 * has a particular Skill - built entirely with the JPA Criteria API
 * (CriteriaBuilder, CriteriaQuery, Root, Join, Predicate, TypedQuery),
 * joining across the Employee -> Department and Employee -> Skill
 * (many-to-many) associations already used in Hands-on 2.
 */
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class EmployeeCriteriaRepository {

    public List<Employee> search(EmployeeSearchCriteria criteria) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
            Root<Employee> employee = cq.from(Employee.class);

            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getDepartmentName() != null) {
                Join<Employee, Department> department = employee.join("department");
                predicates.add(cb.equal(department.get("name"), criteria.getDepartmentName()));
            }

            if (criteria.getPermanent() != null) {
                predicates.add(cb.equal(employee.get("permanent"), criteria.getPermanent()));
            }

            if (criteria.getMinSalary() != null) {
                predicates.add(cb.greaterThanOrEqualTo(employee.get("salary"), criteria.getMinSalary()));
            }

            if (criteria.getSkillName() != null) {
                // Many-to-many join to Skill so we can filter on skill name
                Join<Employee, Skill> skill = employee.join("skillList");
                predicates.add(cb.equal(skill.get("name"), criteria.getSkillName()));
            }

            // distinct(true) avoids duplicate Employee rows that the
            // Skill join could otherwise introduce (one row per matching skill)
            cq.select(employee).distinct(true).where(cb.and(predicates.toArray(new Predicate[0])));

            TypedQuery<Employee> query = em.createQuery(cq);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}

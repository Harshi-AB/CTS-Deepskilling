/*
 * ProductRepository.java
 * -----------------------
 * Hands-on 6: Criteria Query - retail "search for laptop" scenario.
 *
 * Builds the WHERE clause PROGRAMMATICALLY, one Predicate per filter the
 * user actually selected, instead of trying to maintain N different fixed
 * HQL strings (or fragile string concatenation) for every possible
 * combination of the 7 filter categories described in the document
 * (customer review, hard disk size, RAM size, CPU speed, OS, weight, CPU).
 *
 * Key Criteria API building blocks used here (as referenced in the
 * objectives): CriteriaBuilder, CriteriaQuery, Root, TypedQuery.
 */
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    public List<Product> search(ProductSearchCriteria criteria) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Product> cq = cb.createQuery(Product.class);
            Root<Product> product = cq.from(Product.class);

            // Collect one predicate per filter that was actually supplied.
            // A null value in the criteria object means "this filter was
            // not selected by the user" -> simply skip adding a predicate.
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getMinCustomerReview() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        product.get("customerReview"), criteria.getMinCustomerReview()));
            }
            if (criteria.getMinHardDiskSizeGb() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        product.get("hardDiskSizeGb"), criteria.getMinHardDiskSizeGb()));
            }
            if (criteria.getMinRamSizeGb() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        product.get("ramSizeGb"), criteria.getMinRamSizeGb()));
            }
            if (criteria.getMinCpuSpeedGhz() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        product.get("cpuSpeedGhz"), criteria.getMinCpuSpeedGhz()));
            }
            if (criteria.getOperatingSystem() != null) {
                predicates.add(cb.equal(product.get("operatingSystem"), criteria.getOperatingSystem()));
            }
            if (criteria.getMaxWeightKg() != null) {
                predicates.add(cb.lessThanOrEqualTo(product.get("weightKg"), criteria.getMaxWeightKg()));
            }
            if (criteria.getCpu() != null) {
                predicates.add(cb.equal(product.get("cpu"), criteria.getCpu()));
            }

            cq.select(product).where(cb.and(predicates.toArray(new Predicate[0])));

            TypedQuery<Product> query = em.createQuery(cq);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}

/*
 * AttemptRepository.java
 * -----------------------
 * Hands-on 3: Fetch quiz attempt details using HQL.
 *
 * public Attempt getAttempt(int userId, int attemptId)
 *
 * The HQL below joins the tables in the exact order specified by the
 * document: user -> attempt -> attempt_question -> question ->
 * attempt_option -> options, and uses "fetch" on every one-to-many
 * association so the whole object graph is populated in a SINGLE query
 * (no lazy-loading surprises once the EntityManager is closed).
 *
 * NOTE ON COLLECTIONS: the associations that get fetch-joined together
 * here (attemptQuestionList, optionList, attemptOptionList) are declared
 * as java.util.Set in the entity classes rather than List. Fetch-joining
 * more than one java.util.List ("bag") association in a single HQL query
 * triggers Hibernate's MultipleBagFetchException; Set-based collections
 * do not have this restriction, which is why Set was used consistently
 * across the quiz entities.
 */
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class AttemptRepository {

    public Attempt getAttempt(int userId, int attemptId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String hql = "SELECT a FROM Attempt a " +
                    "left join fetch a.user u " +
                    "left join fetch a.attemptQuestionList aq " +
                    "left join fetch aq.question q " +
                    "left join fetch q.optionList " +
                    "left join fetch aq.attemptOptionList ao " +
                    "left join fetch ao.option " +
                    "WHERE u.id = :userId AND a.id = :attemptId";

            TypedQuery<Attempt> query = em.createQuery(hql, Attempt.class);
            query.setParameter("userId", userId);
            query.setParameter("attemptId", attemptId);

            List<Attempt> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }
}

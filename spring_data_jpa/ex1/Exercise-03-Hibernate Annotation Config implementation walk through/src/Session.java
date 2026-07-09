/**
 * Custom re-implementation of org.hibernate.Session.
 *
 * A lightweight, single-threaded unit-of-work object obtained from
 * SessionFactory.openSession(). Implements exactly the operations called
 * out in the Hands-on 2 explanation topics:
 *   beginTransaction(), session.save(), session.createQuery().list(),
 *   session.get(), session.delete()
 */
public class Session {

    private static final Logger LOGGER = LoggerFactory.getLogger(Session.class);

    // Simulates MySQL's AUTO_INCREMENT / Hibernate's "native" id generator
    private static int nativeIdSequence = 1000;

    private final EntityMapping mapping;

    Session(EntityMapping mapping) {
        this.mapping = mapping;
    }

    public Transaction beginTransaction() {
        LOGGER.debug("Beginning transaction");
        return new Transaction();
    }

    /** Simulates session.save(employee) - inserts a new row, generating the id. */
    public Integer save(Object entity) {
        Integer currentId = (Integer) SessionEntityMapper.getIdValue(mapping, entity);
        if (currentId == null || currentId == 0) {
            currentId = ++nativeIdSequence;
            SessionEntityMapper.setIdValue(mapping, entity, currentId);
        }
        MySQLDatabase.save(mapping.getTableName(), currentId, SessionEntityMapper.toRow(mapping, entity));
        return currentId;
    }

    /** Simulates session.get(Employee.class, id). */
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> entityClass, Integer id) {
        java.util.Map<String, Object> row = MySQLDatabase.findById(mapping.getTableName(), id);
        return row == null ? null : (T) SessionEntityMapper.hydrate(mapping, row);
    }

    /** Simulates session.createQuery("FROM Employee"). */
    public Query createQuery(String hql) {
        return new Query(hql, mapping);
    }

    /** Simulates session.delete(employee). */
    public void delete(Object entity) {
        Object id = SessionEntityMapper.getIdValue(mapping, entity);
        MySQLDatabase.deleteById(mapping.getTableName(), id);
    }

    public void close() {
        LOGGER.debug("Closing session");
    }
}

/**
 * Custom re-implementation of org.hibernate.Transaction.
 * Wraps a unit of work with commit()/rollback() semantics.
 */
public class Transaction {

    private static final Logger LOGGER = LoggerFactory.getLogger(Transaction.class);
    private boolean active = true;

    public void commit() {
        if (!active) {
            throw new HibernateException("Transaction already completed");
        }
        LOGGER.debug("Committing transaction");
        active = false;
    }

    public void rollback() {
        if (!active) {
            return;
        }
        LOGGER.debug("Rolling back transaction");
        active = false;
    }
}

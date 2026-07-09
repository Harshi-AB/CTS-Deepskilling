/**
 * Custom re-implementation of org.hibernate.HibernateException.
 * A generic runtime exception thrown when a Session operation fails.
 */
public class HibernateException extends RuntimeException {

    public HibernateException(String message) {
        super(message);
    }

    public HibernateException(String message, Throwable cause) {
        super(message, cause);
    }
}

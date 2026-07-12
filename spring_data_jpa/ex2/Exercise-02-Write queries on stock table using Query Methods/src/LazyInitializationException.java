/**
 * Thrown when application code accesses a relationship field that was
 * declared with FetchType.LAZY and was therefore never populated by the
 * repository. Mirrors org.hibernate.LazyInitializationException so the
 * hands-on exercise around EAGER vs LAZY fetching behaves the same way
 * it would with real Spring Data JPA / Hibernate.
 */
public class LazyInitializationException extends RuntimeException {

    public LazyInitializationException(String message) {
        super(message);
    }
}

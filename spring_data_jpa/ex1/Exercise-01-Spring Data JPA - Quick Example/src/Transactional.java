import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom re-implementation of org.springframework.transaction.annotation.Transactional.
 *
 * In real Spring, this annotation causes a proxy to open a Hibernate Session /
 * begin a transaction before the method runs, and commit (or rollback on
 * exception) after it returns. Our MySQLDatabase simulator mimics that same
 * begin/commit/rollback log sequence whenever a method carrying this
 * annotation is invoked (see ApplicationContext's service proxy logic).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Transactional {
}

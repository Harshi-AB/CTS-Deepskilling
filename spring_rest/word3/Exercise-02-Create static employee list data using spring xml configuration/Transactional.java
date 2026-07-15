import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Transactional.java
 *
 * Local, functionally-equivalent stand-in for Spring's
 * org.springframework.transaction.annotation.Transactional annotation,
 * used here because this project uses Core Java only. Marks a service
 * method as one that should be executed within a transaction boundary.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Transactional {
}

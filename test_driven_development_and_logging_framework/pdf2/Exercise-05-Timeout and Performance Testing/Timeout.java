import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation declaring the maximum time (in milliseconds) a
 * test method is allowed to run before it is considered failed
 * (stands in for JUnit 5's @Timeout).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Timeout {
    long millis();
}

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Service.java
 *
 * Local, functionally-equivalent stand-in for Spring's
 * org.springframework.stereotype.Service annotation, used here because
 * this project uses Core Java only (no Spring dependency available).
 * Marks a class as belonging to the Service layer.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Service {
}

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Component.java
 *
 * Custom annotation that plays the same role as Spring's @Component:
 * it marks a class as eligible to be managed (instantiated and
 * wired) by our own annotation-driven container.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {
}

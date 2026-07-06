import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Component.java
 *
 * Custom annotation mirroring Spring's @Component, marking a class as
 * a bean to be managed by our lightweight SpringApplication runner.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {
}

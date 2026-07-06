import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Autowired.java
 *
 * Custom annotation mirroring Spring's @Autowired. Fields marked with
 * this annotation will have their dependency injected automatically
 * by AnnotationConfigContainer once all @Component beans have been
 * instantiated.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Autowired {
}

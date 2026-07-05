import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation supplying a set of int values to feed into a
 * parameterized test method, one invocation per value.
 * Mirrors JUnit 5's @ValueSource(ints = {...}).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ValueSource {
    int[] ints() default {};
}

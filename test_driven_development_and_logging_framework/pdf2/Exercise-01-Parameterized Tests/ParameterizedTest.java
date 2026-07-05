import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation marking a method as a parameterized test.
 * Mirrors the behaviour of JUnit 5's @ParameterizedTest annotation,
 * implemented here using plain Java reflection (no external libraries).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ParameterizedTest {
}

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation mirroring JUnit's @Before.
 *
 * A method annotated with @Before is a "setup" / test fixture method:
 * the TestRunner will execute it immediately before EVERY @Test method,
 * giving each test a fresh, known starting state (fixture).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Before {
}

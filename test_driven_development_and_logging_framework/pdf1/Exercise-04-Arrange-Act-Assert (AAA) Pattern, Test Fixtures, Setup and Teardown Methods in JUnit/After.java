import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation mirroring JUnit's @After.
 *
 * A method annotated with @After is a "teardown" method: the TestRunner
 * will execute it immediately after EVERY @Test method (even conceptually
 * whether the test passed or failed), allowing cleanup of resources
 * created during setup or the test itself.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface After {
}

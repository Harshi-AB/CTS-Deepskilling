import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom class-level annotation indicating that the test engine should
 * execute this class's @Test methods according to their @Order value
 * (stands in for org.junit.jupiter.api.TestMethodOrder).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TestMethodOrder {
}

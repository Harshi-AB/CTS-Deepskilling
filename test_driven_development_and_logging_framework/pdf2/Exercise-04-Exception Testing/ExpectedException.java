import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation declaring the exact exception type a test method
 * is expected to throw (stands in for JUnit 5's Assertions.assertThrows,
 * expressed declaratively as an annotation).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExpectedException {
    Class<? extends Throwable> value();
}

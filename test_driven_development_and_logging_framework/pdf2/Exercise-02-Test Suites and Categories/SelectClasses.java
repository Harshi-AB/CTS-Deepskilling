import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation listing the test classes that belong to a @Suite
 * (stands in for org.junit.platform.suite.api.SelectClasses).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SelectClasses {
    Class<?>[] value();
}

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a POJO class as a JPA-style persistent entity.
 * Equivalent in purpose to javax.persistence.Entity, hand-written
 * here so the project needs no external JAR other than the MySQL driver.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {
}

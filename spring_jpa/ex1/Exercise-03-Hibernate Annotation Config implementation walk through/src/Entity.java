import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom re-implementation of javax.persistence.Entity.
 *
 * Marks a POJO as a persistence entity. Any class carrying this annotation
 * is treated by our mini-JPA runtime (see JpaRepositoryProxyFactory) as a
 * row-mapped object backed by an in-memory MySQL-like table.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {
}

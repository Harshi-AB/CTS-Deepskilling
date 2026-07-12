import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a many-to-many association.
 * The owning side defines @JoinTable; the inverse side sets mappedBy.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManyToMany {
    String mappedBy() default "";
    FetchType fetch() default FetchType.LAZY;
}

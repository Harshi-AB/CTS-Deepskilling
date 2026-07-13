import java.lang.annotation.*;

/** Custom stand-in for @Transient - excludes a field from persistence entirely. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Transient {
}

import java.lang.annotation.*;

/** Custom stand-in for @Id - marks the primary-key field of an entity. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {
}

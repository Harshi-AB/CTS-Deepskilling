import java.lang.annotation.*;

/**
 * LastModifiedDate.java
 * Custom stand-in for org.springframework.data.annotation.LastModifiedDate.
 * Marks the field that should be re-stamped with "now" on every save,
 * both insert AND update.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LastModifiedDate {
}

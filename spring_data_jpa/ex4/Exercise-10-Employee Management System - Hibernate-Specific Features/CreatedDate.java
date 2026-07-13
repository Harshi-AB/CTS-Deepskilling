import java.lang.annotation.*;

/**
 * CreatedDate.java
 * Custom stand-in for org.springframework.data.annotation.CreatedDate.
 * Marks the field that should be stamped with "now" exactly once, the
 * first time an entity is persisted, and never touched again afterwards.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CreatedDate {
}

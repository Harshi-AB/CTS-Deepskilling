import java.lang.annotation.*;

/** Custom stand-in for @ManyToOne - marks the "many" side of a relationship. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManyToOne {
}

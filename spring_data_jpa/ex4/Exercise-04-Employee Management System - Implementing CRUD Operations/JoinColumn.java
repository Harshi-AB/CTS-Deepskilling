import java.lang.annotation.*;

/** Custom stand-in for @JoinColumn - names the FK column used by a @ManyToOne field. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JoinColumn {
    String name();
}

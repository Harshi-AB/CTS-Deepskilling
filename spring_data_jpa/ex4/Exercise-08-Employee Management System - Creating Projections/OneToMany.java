import java.lang.annotation.*;

/** Custom stand-in for @OneToMany - marks the "one" side of a bidirectional relationship. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OneToMany {
    String mappedBy();
}

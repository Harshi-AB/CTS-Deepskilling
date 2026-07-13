import java.lang.annotation.*;

/** Custom stand-in for @Column - maps a field to a column, with basic constraints. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String name();
    boolean nullable() default true;
    boolean unique() default false;
}

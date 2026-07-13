import java.lang.annotation.*;

/** Custom stand-in for @Table - lets an entity specify its physical table name. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    String name();
}

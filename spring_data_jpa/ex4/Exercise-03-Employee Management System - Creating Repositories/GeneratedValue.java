import java.lang.annotation.*;

/** Custom stand-in for @GeneratedValue - declares how the PK value is produced. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GeneratedValue {
    GenerationType strategy() default GenerationType.IDENTITY;
}

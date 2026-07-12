import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Declares the physical join table backing a @ManyToMany association. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JoinTable {
    String name();
    JoinColumn[] joinColumns();
    JoinColumn[] inverseJoinColumns();
}

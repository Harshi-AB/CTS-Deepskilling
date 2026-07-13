import java.lang.annotation.*;

/**
 * Entity.java
 * Custom stand-in for javax.persistence.Entity / jakarta.persistence.Entity.
 * Since this project cannot pull the JPA spec jar from Maven Central,
 * we author our own minimal annotation set that our hand-rolled ORM
 * engine (introduced in later exercises) reads via reflection at runtime.
 * Marks a POJO as mapped to a database table.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {
}

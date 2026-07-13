import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * AuditingEntityListener.java
 *
 * Exercise 07 - Enabling Entity Auditing
 * -----------------------------------------------------
 * Custom stand-in for Spring Data's real AuditingEntityListener.
 * Called by SimpleJpaRepository right before an INSERT or UPDATE is
 * executed, so @CreatedDate / @LastModifiedDate fields are populated
 * automatically - the developer never sets these fields by hand.
 *
 * Design pattern: Observer/Listener (invoked as a lifecycle callback
 * around the persistence operation, just like JPA's @PrePersist/@PreUpdate).
 */
public class AuditingEntityListener {

    public static void applyOnCreate(Object entity) {
        LocalDateTime now = LocalDateTime.now();
        for (Field f : entity.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            try {
                if (f.isAnnotationPresent(CreatedDate.class)) {
                    f.set(entity, now);
                }
                if (f.isAnnotationPresent(LastModifiedDate.class)) {
                    f.set(entity, now);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Auditing failed on field " + f.getName(), e);
            }
        }
    }

    public static void applyOnUpdate(Object entity) {
        LocalDateTime now = LocalDateTime.now();
        for (Field f : entity.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            try {
                if (f.isAnnotationPresent(LastModifiedDate.class)) {
                    f.set(entity, now);
                }
                // Note: @CreatedDate is intentionally left untouched on update,
                // exactly like real Spring Data JPA auditing behaviour.
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Auditing failed on field " + f.getName(), e);
            }
        }
    }
}

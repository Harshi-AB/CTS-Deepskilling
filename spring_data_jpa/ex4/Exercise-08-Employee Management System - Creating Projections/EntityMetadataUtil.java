import java.lang.reflect.Field;

/**
 * EntityMetadataUtil.java
 *
 * Exercise 04 - Implementing CRUD Operations
 * ---------------------------------------------
 * Shared reflection helpers used by SimpleJpaRepository to translate
 * annotated entity classes into table/column metadata at runtime -
 * exactly the kind of metadata model Hibernate builds internally
 * (its "EntityPersister"), just drastically simplified.
 */
public class EntityMetadataUtil {

    /** Returns the physical table name declared on @Table, or the class name lower-cased as a fallback. */
    public static String getTableName(Class<?> entityClass) {
        if (entityClass.isAnnotationPresent(Table.class)) {
            return entityClass.getAnnotation(Table.class).name();
        }
        return entityClass.getSimpleName().toLowerCase();
    }

    /** Locates the single field annotated with @Id. Throws if none is found - every entity must have one. */
    public static Field getIdField(Class<?> entityClass) {
        for (Field f : entityClass.getDeclaredFields()) {
            if (f.isAnnotationPresent(Id.class)) {
                f.setAccessible(true);
                return f;
            }
        }
        throw new IllegalStateException("Entity " + entityClass.getName() + " has no @Id field");
    }

    /** Returns the @Column name mapped to a field, skipping @Transient / @ManyToOne fields (handled separately). */
    public static String getColumnName(Field field) {
        if (field.isAnnotationPresent(Column.class)) {
            return field.getAnnotation(Column.class).name();
        }
        if (field.isAnnotationPresent(Id.class)) {
            // @Id fields in this project always also carry @Column, but fall back just in case.
            return field.getName();
        }
        return null;
    }

    public static boolean isPersistable(Field field) {
        return !field.isAnnotationPresent(Transient.class)
                && !field.isAnnotationPresent(OneToMany.class);
    }
}

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Reflection based helper that inspects classes annotated with @Entity
 * and exposes their table name, simple columns, and relationship fields.
 * This is the metadata layer of the hand-rolled mini-ORM used across
 * every exercise in this project.
 */
public class EntityUtils {

    private EntityUtils() {
        // utility class - no instances
    }

    public static String tableName(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        return (table != null) ? table.name() : clazz.getSimpleName().toLowerCase();
    }

    public static Field idField(Class<?> clazz) {
        for (Field f : clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(Id.class)) {
                return f;
            }
        }
        throw new IllegalStateException("No @Id field declared in " + clazz.getName());
    }

    /** Simple (non-relationship) persistent fields - those annotated with @Column. */
    public static List<Field> columnFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        for (Field f : clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(Column.class) && !f.isAnnotationPresent(Transient.class)) {
                fields.add(f);
            }
        }
        return fields;
    }

    public static String columnName(Field f) {
        Column column = f.getAnnotation(Column.class);
        return (column != null) ? column.name() : f.getName();
    }

    public static List<Field> manyToOneFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        for (Field f : clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(ManyToOne.class)) {
                fields.add(f);
            }
        }
        return fields;
    }

    public static List<Field> oneToManyFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        for (Field f : clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(OneToMany.class)) {
                fields.add(f);
            }
        }
        return fields;
    }

    public static List<Field> manyToManyFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        for (Field f : clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(ManyToMany.class)) {
                fields.add(f);
            }
        }
        return fields;
    }

    public static Object get(Field f, Object target) {
        try {
            f.setAccessible(true);
            return f.get(target);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void set(Field f, Object target, Object value) {
        try {
            f.setAccessible(true);
            f.set(target, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * SessionEntityMapper
 * -------------------
 * Shared reflection logic used by Session and Query to convert between a
 * mapped POJO (e.g. Employee) and a row Map<column, value>, driven entirely
 * by the EntityMapping parsed from the .hbm.xml file - NOT by annotations.
 * This is what makes Exercise 2's mapping mechanism genuinely different
 * from Exercise 3's annotation-driven mapping.
 */
public class SessionEntityMapper {

    /** Converts a mapped entity instance into a column-name -> value row map. */
    public static Map<String, Object> toRow(EntityMapping mapping, Object entity) {
        try {
            Map<String, Object> row = new LinkedHashMap<>();
            Class<?> entityClass = entity.getClass();

            Field idField = entityClass.getDeclaredField(mapping.getIdPropertyName());
            idField.setAccessible(true);
            row.put(mapping.getIdColumnName(), idField.get(entity));

            for (Map.Entry<String, String> prop : mapping.getPropertyToColumn().entrySet()) {
                Field field = entityClass.getDeclaredField(prop.getKey());
                field.setAccessible(true);
                row.put(prop.getValue(), field.get(entity));
            }
            return row;
        } catch (ReflectiveOperationException e) {
            throw new HibernateException("Unable to map entity to row: " + entity, e);
        }
    }

    /** Reconstructs a mapped entity instance from a row Map<column, value>. */
    public static Object hydrate(EntityMapping mapping, Map<String, Object> row) {
        try {
            Class<?> entityClass = Class.forName(mapping.getClassName());
            Object entity = entityClass.getDeclaredConstructor().newInstance();

            Field idField = entityClass.getDeclaredField(mapping.getIdPropertyName());
            idField.setAccessible(true);
            idField.set(entity, row.get(mapping.getIdColumnName()));

            for (Map.Entry<String, String> prop : mapping.getPropertyToColumn().entrySet()) {
                Field field = entityClass.getDeclaredField(prop.getKey());
                field.setAccessible(true);
                field.set(entity, row.get(prop.getValue()));
            }
            return entity;
        } catch (ReflectiveOperationException e) {
            throw new HibernateException("Unable to hydrate row into entity: " + row, e);
        }
    }

    public static Object getIdValue(EntityMapping mapping, Object entity) {
        try {
            Field idField = entity.getClass().getDeclaredField(mapping.getIdPropertyName());
            idField.setAccessible(true);
            return idField.get(entity);
        } catch (ReflectiveOperationException e) {
            throw new HibernateException("Unable to read id from entity: " + entity, e);
        }
    }

    public static void setIdValue(EntityMapping mapping, Object entity, Object idValue) {
        try {
            Field idField = entity.getClass().getDeclaredField(mapping.getIdPropertyName());
            idField.setAccessible(true);
            idField.set(entity, idValue);
        } catch (ReflectiveOperationException e) {
            throw new HibernateException("Unable to set id on entity: " + entity, e);
        }
    }
}

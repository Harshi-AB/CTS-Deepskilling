import java.lang.reflect.Field;

/**
 * AnnotationEntityScanner
 * -----------------------
 * Reads @Entity/@Table/@Id/@GeneratedValue/@Column annotations off an
 * entity class via reflection and builds the same EntityMapping object
 * that Exercise 2 builds by parsing Employee.hbm.xml. This is exactly the
 * job Hibernate's AnnotationConfiguration.addAnnotatedClass() performs
 * internally.
 */
public class AnnotationEntityScanner {

    public static EntityMapping scan(Class<?> entityClass) {
        if (!entityClass.isAnnotationPresent(Entity.class)) {
            throw new HibernateException(entityClass.getName() + " is not annotated with @Entity");
        }
        Table table = entityClass.getAnnotation(Table.class);
        if (table == null) {
            throw new HibernateException(entityClass.getName() + " is missing @Table");
        }

        EntityMapping mapping = new EntityMapping();
        mapping.setClassName(entityClass.getName());
        mapping.setTableName(table.name());

        for (Field field : entityClass.getDeclaredFields()) {
            Column column = field.getAnnotation(Column.class);
            if (field.isAnnotationPresent(Id.class)) {
                mapping.setIdPropertyName(field.getName());
                mapping.setIdColumnName(column != null ? column.name() : field.getName());
                if (field.isAnnotationPresent(GeneratedValue.class)) {
                    mapping.setGeneratorClass("native");
                }
            } else if (column != null) {
                mapping.addProperty(field.getName(), column.name());
            }
        }
        return mapping;
    }
}

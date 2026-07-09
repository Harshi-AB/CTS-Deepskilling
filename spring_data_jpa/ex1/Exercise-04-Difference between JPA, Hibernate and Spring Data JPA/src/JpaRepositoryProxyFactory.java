import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * JpaRepositoryProxyFactory
 * -------------------------
 * The heart of our "Spring Data JPA" simulation.
 *
 * Real Spring Data JPA never asks you to write an implementation class for
 * "interface CountryRepository extends JpaRepository<Country, String>".
 * Instead, at application startup, Spring inspects the interface, and
 * generates a proxy object implementing it, backed by a shared
 * SimpleJpaRepository implementation that talks to Hibernate.
 *
 * This class reproduces that exact behaviour using java.lang.reflect.Proxy
 * (the GoF Proxy design pattern), with MySQLDatabase standing in for the
 * real Hibernate/JDBC layer.
 */
public class JpaRepositoryProxyFactory {

    // Simulates AUTO_INCREMENT for @Id fields annotated with @GeneratedValue,
    // keyed per entity class - mirrors Hibernate's "native" id generator.
    private static final Map<Class<?>, AtomicInteger> ID_SEQUENCES = new ConcurrentHashMap<>();

    /**
     * Builds a runtime proxy for the given repository interface.
     *
     * @param repositoryInterface an interface extending JpaRepository<T, ID>
     * @param <R> the repository interface type
     * @return a working proxy instance implementing findAll/findById/save/deleteById
     */
    @SuppressWarnings("unchecked")
    public static <R> R create(Class<R> repositoryInterface) {
        Class<?> entityClass = resolveEntityClass(repositoryInterface);
        String tableName = resolveTableName(entityClass);

        InvocationHandler handler = (proxy, method, args) -> {
            switch (method.getName()) {
                case "findAll":
                    return findAll(entityClass, tableName);
                case "findById":
                    return findById(entityClass, tableName, args[0]);
                case "save":
                    return save(entityClass, tableName, args[0]);
                case "deleteById":
                    MySQLDatabase.deleteById(tableName, args[0]);
                    return null;
                case "toString":
                    return "JpaRepositoryProxy<" + entityClass.getSimpleName() + ">";
                case "hashCode":
                    return System.identityHashCode(proxy);
                case "equals":
                    return proxy == args[0];
                default:
                    throw new UnsupportedOperationException(
                            "Query method not implemented in this simulation: " + method.getName());
            }
        };

        return (R) Proxy.newProxyInstance(
                repositoryInterface.getClassLoader(),
                new Class<?>[]{repositoryInterface},
                handler);
    }

    /** Reads the generic type argument T from "X extends JpaRepository<T, ID>". */
    private static Class<?> resolveEntityClass(Class<?> repositoryInterface) {
        for (Type genericInterface : repositoryInterface.getGenericInterfaces()) {
            if (genericInterface instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) genericInterface;
                if (pt.getRawType() == JpaRepository.class) {
                    return (Class<?>) pt.getActualTypeArguments()[0];
                }
            }
        }
        throw new IllegalStateException("Repository must extend JpaRepository<T, ID>: " + repositoryInterface);
    }

    private static String resolveTableName(Class<?> entityClass) {
        Table table = entityClass.getAnnotation(Table.class);
        if (table == null) {
            throw new IllegalStateException("Entity missing @Table annotation: " + entityClass);
        }
        return table.name();
    }

    private static Field idField(Class<?> entityClass) {
        for (Field f : entityClass.getDeclaredFields()) {
            if (f.isAnnotationPresent(Id.class)) {
                return f;
            }
        }
        throw new IllegalStateException("Entity missing @Id field: " + entityClass);
    }

    /** Converts an @Entity instance into a column-name -> value row map. */
    private static Map<String, Object> toRow(Object entity) throws IllegalAccessException {
        Map<String, Object> row = new LinkedHashMap<>();
        for (Field f : entity.getClass().getDeclaredFields()) {
            Column column = f.getAnnotation(Column.class);
            if (column != null) {
                f.setAccessible(true);
                row.put(column.name(), f.get(entity));
            }
        }
        return row;
    }

    /** Builds an entity instance from a row map using the no-arg constructor. */
    private static Object fromRow(Class<?> entityClass, Map<String, Object> row) {
        try {
            Object entity = entityClass.getDeclaredConstructor().newInstance();
            for (Field f : entityClass.getDeclaredFields()) {
                Column column = f.getAnnotation(Column.class);
                if (column != null && row.containsKey(column.name())) {
                    f.setAccessible(true);
                    f.set(entity, row.get(column.name()));
                }
            }
            return entity;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Unable to hydrate entity " + entityClass, e);
        }
    }

    private static Object getIdValue(Object entity, Field idField) throws IllegalAccessException {
        idField.setAccessible(true);
        return idField.get(entity);
    }

    private static List<Object> findAll(Class<?> entityClass, String tableName) {
        List<Object> results = new ArrayList<>();
        for (Map<String, Object> row : MySQLDatabase.findAll(tableName)) {
            results.add(fromRow(entityClass, row));
        }
        return results;
    }

    private static Optional<Object> findById(Class<?> entityClass, String tableName, Object id) {
        Map<String, Object> row = MySQLDatabase.findById(tableName, id);
        return row == null ? Optional.empty() : Optional.of(fromRow(entityClass, row));
    }

    private static Object save(Class<?> entityClass, String tableName, Object entity) {
        try {
            Field idField = idField(entityClass);
            idField.setAccessible(true);
            Object id = getIdValue(entity, idField);

            boolean isGenerated = idField.isAnnotationPresent(GeneratedValue.class);
            boolean idIsUnset = (id == null) || (id instanceof Integer && (Integer) id == 0)
                    || (id instanceof Long && (Long) id == 0L);
            if (isGenerated && idIsUnset) {
                // Starts at 5000 (rather than Hibernate's 1000) purely so this demo's two
                // independent persistence contexts don't collide over the same id space.
                int nextId = ID_SEQUENCES.computeIfAbsent(entityClass, c -> new AtomicInteger(5000)).incrementAndGet();
                if (idField.getType() == long.class || idField.getType() == Long.class) {
                    idField.set(entity, (long) nextId);
                } else {
                    idField.set(entity, nextId);
                }
                id = getIdValue(entity, idField);
            }

            Map<String, Object> row = toRow(entity);
            MySQLDatabase.save(tableName, id, row);
            return entity;
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to save entity " + entityClass, e);
        }
    }
}

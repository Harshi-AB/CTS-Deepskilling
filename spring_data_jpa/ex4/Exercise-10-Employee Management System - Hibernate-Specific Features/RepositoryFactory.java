import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * RepositoryFactory.java
 *
 * Exercise 05 - Defining Query Methods
 * ---------------------------------------------
 * Extended from Exercise 04: the InvocationHandler now tries the generic
 * CRUD implementation FIRST, and falls back to DerivedQueryExecutor for
 * any method it doesn't recognize (findByXxx, countByXxx, ...). This
 * two-tier lookup mirrors how Spring Data JPA's RepositoryFactorySupport
 * combines SimpleJpaRepository with a QueryLookupStrategy.
 */
public class RepositoryFactory {

    @SuppressWarnings("unchecked")
    public static <T, ID extends java.io.Serializable, R> R getRepository(
            Class<R> repositoryInterface, Class<T> entityClass) {

        SimpleJpaRepository<T, ID> backingImpl = new SimpleJpaRepository<>(entityClass);
        String tableName = EntityMetadataUtil.getTableName(entityClass);
        DerivedQueryExecutor<T> queryExecutor = new DerivedQueryExecutor<>(entityClass, tableName);

        InvocationHandler handler = (proxy, method, args) -> {
            try {
                Method target = SimpleJpaRepository.class.getMethod(method.getName(), method.getParameterTypes());
                return target.invoke(backingImpl, args);
            } catch (NoSuchMethodException notCrud) {
                if (queryExecutor.supports(method)) {
                    // Exercise 08: a trailing Class<T> argument requests a dynamic
                    // projection - strip it before building the SQL, then wrap
                    // the raw entity result(s) in that projection afterwards.
                    Class<?> projectionType = null;
                    Object[] effectiveArgs = args;
                    if (args != null && args.length > 0 && args[args.length - 1] instanceof Class) {
                        projectionType = (Class<?>) args[args.length - 1];
                        effectiveArgs = java.util.Arrays.copyOf(args, args.length - 1);
                    }

                    Object result = queryExecutor.execute(method, effectiveArgs);

                    if (projectionType != null && !projectionType.equals(entityClass)) {
                        return ProjectionFactory.project(result, projectionType);
                    }
                    return result;
                }
                throw new UnsupportedOperationException("No implementation available for " + method.getName());
            }
        };

        return (R) Proxy.newProxyInstance(
                repositoryInterface.getClassLoader(),
                new Class<?>[]{repositoryInterface},
                handler);
    }
}

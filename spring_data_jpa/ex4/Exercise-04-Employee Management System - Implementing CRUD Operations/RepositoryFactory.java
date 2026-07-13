import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * RepositoryFactory.java
 *
 * Exercise 04 - Implementing CRUD Operations
 * ---------------------------------------------
 * The piece of "magic" that lets a developer write ONLY an interface
 * (EmployeeRepository extends JpaRepository<Employee,Integer>) and get a
 * fully working implementation back - exactly how Spring Data JPA's real
 * RepositoryFactoryBean works internally, minus classpath scanning.
 *
 * Design pattern: Proxy (java.lang.reflect.Proxy) + Factory Method.
 * At runtime, every method call on the returned proxy is intercepted by
 * the InvocationHandler and delegated to a backing SimpleJpaRepository
 * instance that actually knows how to talk to MySQL.
 */
public class RepositoryFactory {

    @SuppressWarnings("unchecked")
    public static <T, ID extends java.io.Serializable, R> R getRepository(
            Class<R> repositoryInterface, Class<T> entityClass) {

        SimpleJpaRepository<T, ID> backingImpl = new SimpleJpaRepository<>(entityClass);

        InvocationHandler handler = (proxy, method, args) -> {
            try {
                // Delegate the call to the generic implementation by matching
                // method name + parameter types (Object.equals/hashCode/toString
                // are handled too, so the proxy behaves like a normal object).
                Method target = SimpleJpaRepository.class.getMethod(method.getName(), method.getParameterTypes());
                return target.invoke(backingImpl, args);
            } catch (NoSuchMethodException e) {
                throw new UnsupportedOperationException(
                        "No implementation available for " + method.getName()
                        + " - custom query methods are wired in Exercise 05.");
            }
        };

        return (R) Proxy.newProxyInstance(
                repositoryInterface.getClassLoader(),
                new Class<?>[]{repositoryInterface},
                handler);
    }
}

import java.lang.reflect.Proxy;

/**
 * Factory that creates runtime dynamic-proxy implementations of
 * repository interfaces (mirrors how Spring Data JPA instantiates
 * repository beans automatically at application startup), without
 * requiring any Spring/Hibernate JAR.
 */
public class RepositoryFactory {

    private RepositoryFactory() {
        // utility class - no instances
    }

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> repositoryInterface, Class<?> entityClass) {
        return (T) Proxy.newProxyInstance(
                repositoryInterface.getClassLoader(),
                new Class<?>[] { repositoryInterface },
                new RepositoryInvocationHandler(entityClass));
    }
}

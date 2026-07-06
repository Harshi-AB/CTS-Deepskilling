import java.util.HashMap;
import java.util.Map;

/**
 * DIContainer.java
 *
 * A tiny hand-rolled Dependency Injection container. It stores bean
 * instances keyed by interface/class type and hands them out on
 * request, mirroring how Spring's ApplicationContext resolves and
 * injects dependencies without the calling code ever using "new"
 * directly on the dependency.
 */
public class DIContainer {

    private final Map<Class<?>, Object> registry = new HashMap<>();

    public <T> void registerBean(Class<T> type, T instance) {
        registry.put(type, instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T resolve(Class<T> type) {
        Object bean = registry.get(type);
        if (bean == null) {
            throw new IllegalStateException("No implementation registered for: " + type.getName());
        }
        return (T) bean;
    }
}

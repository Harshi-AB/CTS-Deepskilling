import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * SpringContainer.java
 *
 * A minimal hand-written IoC container that mimics the core behaviour
 * of Spring's ApplicationContext:
 *   1. It is given a configuration object (like an @Configuration class).
 *   2. It scans that object for "bean factory methods" (methods whose
 *      name starts with "create").
 *   3. It invokes each factory method once and stores the resulting
 *      bean in an internal registry, keyed by the bean's simple type name.
 *   4. Beans can later be retrieved with getBean(Class) - just like
 *      ApplicationContext.getBean(Class) in real Spring.
 */
public class SpringContainer {

    private final Map<String, Object> beanRegistry = new HashMap<>();

    /**
     * Scans the given configuration object and registers every bean
     * produced by its "create*" factory methods.
     */
    public void register(Object configuration) {
        Class<?> configClass = configuration.getClass();

        for (Method method : configClass.getDeclaredMethods()) {
            if (method.getName().startsWith("create") && method.getParameterCount() == 0) {
                try {
                    Object bean = method.invoke(configuration);
                    beanRegistry.put(bean.getClass().getSimpleName(), bean);
                    System.out.println("[SpringContainer] Registered bean: " + bean.getClass().getSimpleName());
                } catch (Exception e) {
                    throw new RuntimeException("Failed to create bean from method: " + method.getName(), e);
                }
            }
        }
    }

    /**
     * Retrieves a previously registered bean by its type.
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> type) {
        Object bean = beanRegistry.get(type.getSimpleName());
        if (bean == null) {
            throw new IllegalStateException("No bean registered of type: " + type.getSimpleName());
        }
        return (T) bean;
    }
}

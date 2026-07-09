import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ApplicationContext
 * ------------------
 * A tiny hand-rolled stand-in for Spring's ApplicationContext /
 * AnnotationConfigApplicationContext.
 *
 * Responsibilities it reproduces from real Spring:
 *  1. Holding singleton beans (repositories, services) in a registry.
 *  2. Creating @Repository beans as dynamic proxies via JpaRepositoryProxyFactory
 *     (so you never write "class CountryRepositoryImpl implements CountryRepository").
 *  3. Performing @Autowired field injection by matching field type to a
 *     registered bean (constructor-less "field injection", same as Spring).
 */
public class ApplicationContext {

    private final Map<Class<?>, Object> beans = new LinkedHashMap<>();

    /** Registers a @Repository interface, backing it with a dynamic proxy. */
    public <R> R registerRepository(Class<R> repositoryInterface) {
        R proxy = JpaRepositoryProxyFactory.create(repositoryInterface);
        beans.put(repositoryInterface, proxy);
        return proxy;
    }

    /** Registers an already-constructed @Service (or any) bean and wires its @Autowired fields. */
    public <T> T registerBean(T bean) {
        autowire(bean);
        beans.put(bean.getClass(), bean);
        return bean;
    }

    /** Retrieves a bean by its declared type (interface or concrete class). */
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> type) {
        Object bean = beans.get(type);
        if (bean == null) {
            // fall back to an assignability search, e.g. requesting an interface
            for (Object candidate : beans.values()) {
                if (type.isInstance(candidate)) {
                    return (T) candidate;
                }
            }
            throw new IllegalStateException("No bean of type " + type.getName() + " registered in context");
        }
        return (T) bean;
    }

    /** Injects matching registered beans into fields annotated with @Autowired. */
    private void autowire(Object bean) {
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                Object dependency = getBean(field.getType());
                field.setAccessible(true);
                try {
                    field.set(bean, dependency);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to autowire field " + field.getName() + " on " + bean.getClass(), e);
                }
            }
        }
    }
}

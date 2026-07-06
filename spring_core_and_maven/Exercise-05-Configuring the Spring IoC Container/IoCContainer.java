import java.util.HashMap;
import java.util.Map;

/**
 * IoCContainer.java
 *
 * A minimal hand-written Inversion of Control container that mirrors
 * the core responsibilities of Spring's BeanFactory / ApplicationContext:
 *   1. registerBeanDefinition(name, definition) - configure how a bean
 *      should be built (its class + scope).
 *   2. getBean(name) - resolve the bean, instantiating it via
 *      reflection the first time, and reusing the same instance on
 *      later calls if it is configured as a singleton.
 */
public class IoCContainer {

    private final Map<String, BeanDefinition> beanDefinitions = new HashMap<>();
    private final Map<String, Object> singletonCache = new HashMap<>();

    public void registerBeanDefinition(String beanName, BeanDefinition definition) {
        beanDefinitions.put(beanName, definition);
        System.out.println("[IoCContainer] Registered bean definition: " + beanName +
                " (singleton=" + definition.isSingleton() + ")");
    }

    public Object getBean(String beanName) {
        BeanDefinition definition = beanDefinitions.get(beanName);
        if (definition == null) {
            throw new IllegalStateException("No bean definition found for: " + beanName);
        }

        if (definition.isSingleton() && singletonCache.containsKey(beanName)) {
            return singletonCache.get(beanName);
        }

        try {
            Object instance = definition.getBeanClass().getDeclaredConstructor().newInstance();
            if (definition.isSingleton()) {
                singletonCache.put(beanName, instance);
            }
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Unable to instantiate bean: " + beanName, e);
        }
    }
}

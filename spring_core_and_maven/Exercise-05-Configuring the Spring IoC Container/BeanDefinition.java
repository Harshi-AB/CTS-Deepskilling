/**
 * BeanDefinition.java
 *
 * Metadata describing how a bean should be created and managed,
 * mirroring Spring's internal BeanDefinition concept:
 *   - the bean's implementation class
 *   - whether it should be treated as a singleton or a new
 *     instance per request (prototype scope)
 */
public class BeanDefinition {

    private final Class<?> beanClass;
    private final boolean singleton;

    public BeanDefinition(Class<?> beanClass, boolean singleton) {
        this.beanClass = beanClass;
        this.singleton = singleton;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public boolean isSingleton() {
        return singleton;
    }
}

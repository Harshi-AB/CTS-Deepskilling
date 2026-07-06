import java.lang.reflect.Proxy;

/**
 * ProxyFactory.java
 *
 * Helper that wraps a target object with a JDK dynamic proxy backed by
 * LoggingInvocationHandler. This plays the same role as Spring's
 * ProxyFactoryBean / auto-proxying mechanism: given a plain target
 * bean, it returns an "advised" proxy that implements the same
 * interface(s).
 */
public class ProxyFactory {

    @SuppressWarnings("unchecked")
    public static <T> T createLoggingProxy(T target, Class<T> interfaceType) {
        return (T) Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class<?>[]{interfaceType},
                new LoggingInvocationHandler(target)
        );
    }
}

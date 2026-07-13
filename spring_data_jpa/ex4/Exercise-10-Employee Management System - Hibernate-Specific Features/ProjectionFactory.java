import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * ProjectionFactory.java
 *
 * Exercise 08 - Creating Projections
 * -----------------------------------------------------
 * Wraps a fully-loaded entity (e.g. Employee) behind a dynamic proxy that
 * only exposes the getters declared on a projection interface
 * (e.g. EmployeeSummary). Every proxy method call is redirected to the
 * identically-named getter on the underlying entity via reflection.
 *
 * This is exactly how Spring Data JPA implements closed interface-based
 * projections internally (org.springframework.data.projection).
 *
 * Design pattern: Proxy + Adapter (adapts the wide Employee entity to a
 * narrow, caller-specific view).
 */
public class ProjectionFactory {

    @SuppressWarnings("unchecked")
    public static <P> P createProjection(Class<P> projectionInterface, Object source) {
        InvocationHandler handler = (proxy, method, args) -> {
            if (method.getDeclaringClass() == Object.class) {
                return switch (method.getName()) {
                    case "toString" -> projectionInterface.getSimpleName() + " projection of " + source;
                    case "hashCode" -> System.identityHashCode(proxy);
                    case "equals" -> proxy == args[0];
                    default -> null;
                };
            }
            // Delegate getX()/isX() to the same-named accessor on the source entity.
            Method sourceMethod = source.getClass().getMethod(method.getName());
            return sourceMethod.invoke(source);
        };

        return (P) Proxy.newProxyInstance(
                projectionInterface.getClassLoader(),
                new Class<?>[]{projectionInterface},
                handler);
    }

    /** Applies createProjection() across a List, or to a single object, transparently. */
    @SuppressWarnings("unchecked")
    public static Object project(Object result, Class<?> projectionType) {
        if (result instanceof List<?> list) {
            List<Object> projected = new ArrayList<>();
            for (Object item : list) {
                projected.add(createProjection((Class<Object>) projectionType, item));
            }
            return projected;
        }
        return createProjection((Class<Object>) projectionType, result);
    }
}

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AnnotationConfigContainer.java
 *
 * A minimal annotation-driven IoC container that mimics Spring's
 * AnnotationConfigApplicationContext:
 *   1. Given a fixed list of candidate classes (standing in for
 *      classpath component scanning), it instantiates every class
 *      annotated with @Component.
 *   2. It then performs a second pass, injecting an appropriate bean
 *      into every field annotated with @Autowired, matching by field
 *      type - the same strategy Spring's autowire-by-type uses.
 */
public class AnnotationConfigContainer {

    private final Map<Class<?>, Object> beans = new HashMap<>();

    public void scanAndRegister(Class<?>... candidateClasses) {
        // Pass 1: instantiate every @Component-annotated class
        for (Class<?> candidate : candidateClasses) {
            if (candidate.isAnnotationPresent(Component.class)) {
                try {
                    Object instance = candidate.getDeclaredConstructor().newInstance();
                    beans.put(candidate, instance);
                    System.out.println("[AnnotationConfigContainer] Created bean: " + candidate.getSimpleName());
                } catch (Exception e) {
                    throw new RuntimeException("Could not instantiate component: " + candidate.getName(), e);
                }
            }
        }

        // Pass 2: resolve @Autowired fields on every registered bean
        for (Object bean : beans.values()) {
            injectDependencies(bean);
        }
    }

    private void injectDependencies(Object bean) {
        List<Field> autowiredFields = new ArrayList<>();
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                autowiredFields.add(field);
            }
        }

        for (Field field : autowiredFields) {
            Object dependency = beans.get(field.getType());
            if (dependency == null) {
                throw new IllegalStateException("No bean available for autowiring: " + field.getType().getName());
            }
            field.setAccessible(true);
            try {
                field.set(bean, dependency);
                System.out.println("[AnnotationConfigContainer] Autowired " + field.getType().getSimpleName() +
                        " into " + bean.getClass().getSimpleName());
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to autowire field: " + field.getName(), e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> type) {
        return (T) beans.get(type);
    }
}

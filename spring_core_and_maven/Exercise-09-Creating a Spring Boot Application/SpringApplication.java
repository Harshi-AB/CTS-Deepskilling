import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SpringApplication.java
 *
 * A minimal reimplementation of Spring Boot's SpringApplication.run()
 * entry point, since the real Spring Boot starter/framework cannot be
 * used in this exercise. It performs the same conceptual startup
 * sequence:
 *   1. Print a startup banner.
 *   2. "Scan" a fixed set of candidate classes for @Component.
 *   3. Instantiate each @Component bean.
 *   4. Autowire @Autowired fields by type.
 *   5. Invoke run() on every bean that implements CommandLineRunner,
 *      just like Spring Boot does after context refresh.
 */
public class SpringApplication {

    public static void run(Class<?> primarySource, Class<?>... componentClasses) throws Exception {
        printBanner();

        if (!primarySource.isAnnotationPresent(SpringBootApplication.class)) {
            throw new IllegalStateException(primarySource.getName() + " must be annotated with @SpringBootApplication");
        }

        Map<Class<?>, Object> beans = new HashMap<>();

        // Step 1: instantiate every @Component
        for (Class<?> candidate : componentClasses) {
            if (candidate.isAnnotationPresent(Component.class)) {
                Object instance = candidate.getDeclaredConstructor().newInstance();
                beans.put(candidate, instance);
                System.out.println("[SpringApplication] Created bean: " + candidate.getSimpleName());
            }
        }

        // Step 2: autowire fields by type
        for (Object bean : beans.values()) {
            for (Field field : bean.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Object dependency = beans.get(field.getType());
                    if (dependency == null) {
                        throw new IllegalStateException("No bean found for autowiring: " + field.getType().getName());
                    }
                    field.setAccessible(true);
                    field.set(bean, dependency);
                    System.out.println("[SpringApplication] Autowired " + field.getType().getSimpleName() +
                            " into " + bean.getClass().getSimpleName());
                }
            }
        }

        System.out.println("[SpringApplication] Application context started.\n");

        // Step 3: run all CommandLineRunner beans, like Spring Boot does automatically
        List<CommandLineRunner> runners = new ArrayList<>();
        for (Object bean : beans.values()) {
            if (bean instanceof CommandLineRunner) {
                runners.add((CommandLineRunner) bean);
            }
        }
        for (CommandLineRunner runner : runners) {
            runner.run();
        }
    }

    private static void printBanner() {
        System.out.println("  ____             _              ____              _   ");
        System.out.println(" / ___| _ __  _ __(_)_ __   __ _ | __ )  ___   ___ | |_ ");
        System.out.println(" \\___ \\| '_ \\| '__| | '_ \\ / _` ||  _ \\ / _ \\ / _ \\| __|");
        System.out.println("  ___) | |_) | |  | | | | | (_| || |_) | (_) | (_) | |_ ");
        System.out.println(" |____/| .__/|_|  |_|_| |_|\\__, ||____/ \\___/ \\___/ \\__|");
        System.out.println("       |_|                 |___/   (simulated, plain Java)\n");
    }
}

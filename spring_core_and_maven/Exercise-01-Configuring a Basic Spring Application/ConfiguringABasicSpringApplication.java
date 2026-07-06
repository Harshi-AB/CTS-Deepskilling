/**
 * ConfiguringABasicSpringApplication.java
 *
 * Entry point demonstrating a basic Spring-style application setup
 * using our own lightweight container (SpringContainer) since the
 * real Spring framework and Maven cannot be used in this exercise.
 *
 * Flow:
 *   1. Create the configuration object (AppConfig).
 *   2. Register it with the SpringContainer, which scans it for beans.
 *   3. Fetch the GreetingService bean from the container.
 *   4. Use the bean exactly as an application would with real Spring DI.
 */
public class ConfiguringABasicSpringApplication {

    public static void main(String[] args) {
        System.out.println("Starting basic Spring-style application...\n");

        // 1. Create the configuration object
        AppConfig appConfig = new AppConfig();

        // 2. Bootstrap the container with the configuration
        SpringContainer context = new SpringContainer();
        context.register(appConfig);

        // 3. Retrieve the managed bean from the container
        GreetingService greetingService = context.getBean(GreetingService.class);

        // 4. Use the bean
        String message = greetingService.greet("Harshitha");
        System.out.println("\nOutput: " + message);
    }
}

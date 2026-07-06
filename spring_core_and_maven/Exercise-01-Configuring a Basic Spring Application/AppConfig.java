/**
 * AppConfig.java
 *
 * Plays the role of a Spring @Configuration class. In real Spring,
 * methods annotated with @Bean would be picked up automatically by
 * the container. Since we cannot use the Spring framework here, this
 * class simply exposes "bean factory methods" that our SpringContainer
 * calls by naming convention (methods starting with "create").
 */
public class AppConfig {

    /**
     * Factory method for the GreetingService bean.
     * The container will call this method (via reflection) to build
     * the bean, exactly like Spring would call an @Bean method.
     */
    public GreetingService createGreetingService() {
        return new GreetingService("Hello");
    }
}

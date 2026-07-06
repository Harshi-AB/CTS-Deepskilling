/**
 * GreetingRepository.java
 *
 * A simple @Component bean representing a data layer, injected into
 * GreetingController to demonstrate dependency wiring at Spring Boot
 * startup.
 */
@Component
public class GreetingRepository {
    public String fetchGreeting() {
        return "Welcome to the Cognizant Deep Skilling Spring Boot exercise!";
    }
}

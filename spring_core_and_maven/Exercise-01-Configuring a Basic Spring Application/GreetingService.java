/**
 * GreetingService.java
 *
 * A simple "bean" class that would normally be managed by the Spring
 * IoC container. Here it is a plain Java class with no framework
 * dependency, so it can be wired up manually by our own mini container.
 */
public class GreetingService {

    private final String greetingPrefix;

    public GreetingService(String greetingPrefix) {
        this.greetingPrefix = greetingPrefix;
    }

    public String greet(String name) {
        return greetingPrefix + ", " + name + "!";
    }
}

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ServiceRegistry simulates the role played by Spring Cloud Netflix
 * Eureka in a real microservice deployment: a central directory that
 * services REGISTER themselves with on startup, and that other
 * services QUERY to DISCOVER where a dependency currently lives,
 * instead of relying on a hardcoded host/port.
 *
 * Implemented as a classic Singleton so every service in this JVM
 * shares exactly one registry instance, mirroring how all services
 * in a real deployment point at the same Eureka server.
 */
public final class ServiceRegistry {

    private static final ServiceRegistry INSTANCE = new ServiceRegistry();

    // serviceName -> base URL (e.g. "PRODUCT-SERVICE" -> "http://localhost:9081")
    private final Map<String, String> registry = new ConcurrentHashMap<>();

    private ServiceRegistry() {
        // Private constructor enforces Singleton pattern
    }

    public static ServiceRegistry getInstance() {
        return INSTANCE;
    }

    /** Registers a service instance under a logical service name. */
    public void register(String serviceName, String baseUrl) {
        registry.put(serviceName.toUpperCase(), baseUrl);
        System.out.println("[ServiceRegistry] Registered " + serviceName.toUpperCase()
                + " -> " + baseUrl);
    }

    /** Removes a service instance from the registry (graceful shutdown). */
    public void deregister(String serviceName) {
        registry.remove(serviceName.toUpperCase());
        System.out.println("[ServiceRegistry] Deregistered " + serviceName.toUpperCase());
    }

    /**
     * Looks up the base URL for a logical service name.
     * Returns null if the service has not registered (i.e. is "down").
     */
    public String discover(String serviceName) {
        return registry.get(serviceName.toUpperCase());
    }

    /** Returns true if the given service is currently registered/available. */
    public boolean isAvailable(String serviceName) {
        return registry.containsKey(serviceName.toUpperCase());
    }

    /** Prints the current registry contents (useful for demo/debugging). */
    public void printRegistry() {
        System.out.println("[ServiceRegistry] Current registrations:");
        for (Map.Entry<String, String> entry : registry.entrySet()) {
            System.out.println("   " + entry.getKey() + " -> " + entry.getValue());
        }
    }
}

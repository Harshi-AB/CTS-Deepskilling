import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ConfigServer simulates Spring Cloud Config Server: a single, central
 * place that holds configuration values for every microservice, so
 * settings are not hardcoded/duplicated inside each service.
 *
 * Implemented as a Singleton holding a simple in-memory key/value
 * store. In a real system this would be backed by a Git repository or
 * database; here the values are seeded programmatically at startup.
 */
public final class ConfigServer {

    private static final ConfigServer INSTANCE = new ConfigServer();

    private final Map<String, String> configuration = new ConcurrentHashMap<>();

    private ConfigServer() {
        loadDefaultConfiguration();
    }

    public static ConfigServer getInstance() {
        return INSTANCE;
    }

    /** Loads the centralized configuration values used by all services. */
    private void loadDefaultConfiguration() {
        configuration.put("product-service.port", "9081");
        configuration.put("inventory-service.port", "9082");
        configuration.put("inventory.low-stock-threshold", "5");
        configuration.put("inventory.default-restock-quantity", "20");
    }

    /** Retrieves a configuration value by key. */
    public String get(String key) {
        return configuration.get(key);
    }

    /** Retrieves a configuration value as an int, with a fallback default. */
    public int getInt(String key, int defaultValue) {
        String value = configuration.get(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /** Allows a value to be updated centrally and picked up by all services. */
    public void set(String key, String value) {
        configuration.put(key, value);
        System.out.println("[ConfigServer] Updated " + key + " = " + value);
    }

    /** Prints all configuration currently held (useful for demo/debugging). */
    public void printConfiguration() {
        System.out.println("[ConfigServer] Centralized configuration:");
        for (Map.Entry<String, String> entry : configuration.entrySet()) {
            System.out.println("   " + entry.getKey() + " = " + entry.getValue());
        }
    }
}

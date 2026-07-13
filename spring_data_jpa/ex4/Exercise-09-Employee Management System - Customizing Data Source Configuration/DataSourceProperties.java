import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * DataSourceProperties.java
 *
 * Exercise 09 - Customizing Data Source Configuration
 * -----------------------------------------------------
 * Reads application.properties from the classpath, mirroring how Spring
 * Boot resolves spring.datasource.* keys - just done by hand with
 * java.util.Properties instead of relying on the Environment abstraction.
 *
 * Design pattern: Singleton (one shared, immutable properties holder).
 */
public class DataSourceProperties {

    private static final DataSourceProperties INSTANCE = new DataSourceProperties();

    private final String url;
    private final String username;
    private final String password;
    private final String driver;
    private final int poolSize;

    private DataSourceProperties() {
        Properties props = new Properties();
        try (InputStream in = DataSourceProperties.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (in == null) {
                throw new IllegalStateException("application.properties not found on classpath");
            }
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application.properties", e);
        }

        this.url = props.getProperty("db.url");
        this.username = props.getProperty("db.username");
        this.password = props.getProperty("db.password");
        this.driver = props.getProperty("db.driver");
        this.poolSize = Integer.parseInt(props.getProperty("db.pool.size", "5"));
    }

    public static DataSourceProperties getInstance() {
        return INSTANCE;
    }

    public String getUrl() { return url; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getDriver() { return driver; }
    public int getPoolSize() { return poolSize; }
}

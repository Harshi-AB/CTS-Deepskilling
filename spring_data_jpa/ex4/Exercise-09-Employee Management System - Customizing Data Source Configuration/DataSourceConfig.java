/**
 * DataSourceConfig.java
 *
 * Exercise 09 - Customizing Data Source Configuration
 * -----------------------------------------------------
 * Builds and exposes the ONE shared SimpleConnectionPool for the whole
 * application, sized and pointed at MySQL entirely from
 * application.properties - equivalent to Spring Boot auto-configuring a
 * HikariDataSource bean from spring.datasource.* properties.
 *
 * Design pattern: Singleton.
 */
public class DataSourceConfig {

    private static final SimpleConnectionPool POOL = buildPool();

    private static SimpleConnectionPool buildPool() {
        DataSourceProperties props = DataSourceProperties.getInstance();
        return new SimpleConnectionPool(
                props.getUrl(), props.getUsername(), props.getPassword(),
                props.getDriver(), props.getPoolSize());
    }

    private DataSourceConfig() { }

    public static SimpleConnectionPool getPool() {
        return POOL;
    }
}

import java.sql.Connection;
import java.sql.SQLException;

/**
 * DBConnectionUtil.java
 *
 * Exercise 09 - Customizing Data Source Configuration
 * -----------------------------------------------------
 * Same public API as Exercises 01-08 (getConnection()/close()), but now
 * backed by the pooled DataSourceConfig instead of opening a fresh
 * DriverManager connection on every call - every repository class from
 * earlier exercises keeps working unchanged.
 */
public class DBConnectionUtil {

    public static Connection getConnection() throws SQLException {
        return DataSourceConfig.getPool().getConnection();
    }

    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close(); // intercepted by the pool's proxy -> returned to pool, not truly closed
            } catch (SQLException e) {
                System.err.println("Error releasing pooled connection: " + e.getMessage());
            }
        }
    }
}

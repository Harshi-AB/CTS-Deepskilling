import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnectionUtil.java
 *
 * Exercise 01 - Overview and Setup
 * ---------------------------------
 * Simulates the piece of infrastructure that Spring Data JPA normally
 * hides behind "spring-boot-starter-data-jpa": obtaining a live JDBC
 * Connection from the configured DataSource.
 *
 * Design pattern used: Utility / Static Factory - a single, stateless
 * access point for Connection objects, so every DAO/Repository in later
 * exercises reuses this instead of duplicating connection logic.
 */
public class DBConnectionUtil {

    /**
     * Loads the MySQL JDBC driver once (kept explicit and visible here for
     * teaching purposes, even though modern drivers auto-register via SPI)
     * and returns a fresh Connection.
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(DBConfig.DRIVER);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found on classpath. "
                    + "Download mysql-connector-j and add it with -cp", e);
        }
        return DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
    }

    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}

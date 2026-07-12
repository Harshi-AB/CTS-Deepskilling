import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class that hands out plain java.sql JDBC connections to the
 * core MySQL "ormlearn" database. No ORM/Spring/Hibernate JAR is used -
 * only the java.sql API bundled with the JDK, plus the MySQL Connector/J
 * driver JAR on the runtime classpath.
 *
 * Update USER / PASSWORD / URL below to match your local MySQL setup.
 */
public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/ormlearn?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private DBConnection() {
        // utility class - no instances
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

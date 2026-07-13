import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * Main.java
 *
 * Exercise 01 - Overview and Setup
 * ---------------------------------
 * Goal: prove that the project can talk to MySQL using nothing but core
 * JDBC (no Maven, no Spring context to bootstrap). This is the
 * "does my environment actually work" checkpoint before entities and
 * repositories are introduced in Exercises 02-10.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Spring Data JPA Simulation - Exercise 01: Overview and Setup ===");

        try (Connection connection = DBConnectionUtil.getConnection()) {
            System.out.println("Connection established successfully with employee_db!");

            try (Statement stmt = connection.createStatement()) {
                stmt.execute("INSERT INTO connection_test (message) VALUES ('Setup verified')");

                try (ResultSet rs = stmt.executeQuery("SELECT id, message, created_at FROM connection_test")) {
                    System.out.println("\nRows currently in connection_test:");
                    while (rs.next()) {
                        System.out.printf("  id=%d | message=%s | created_at=%s%n",
                                rs.getInt("id"), rs.getString("message"), rs.getTimestamp("created_at"));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Setup FAILED: " + e.getMessage());
            System.err.println("Check DBConfig.java values and that MySQL is running, "
                    + "and that mysql-connector-j jar is on the classpath.");
        }
    }
}

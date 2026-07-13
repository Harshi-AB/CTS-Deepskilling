/**
 * DBConfig.java
 *
 * Exercise 01 - Overview and Setup
 * ---------------------------------
 * In a real Spring Data JPA project this class would be replaced by
 * "application.properties" (spring.datasource.url / username / password)
 * combined with Spring Boot auto-configuration that builds a DataSource
 * bean automatically.
 *
 * Since this project intentionally avoids Maven/Gradle and the Spring
 * Framework jars, DBConfig plays the same role by hand: it centralizes
 * every connection setting in ONE place so the rest of the application
 * never hard-codes DB details (Single Responsibility Principle).
 */
public class DBConfig {

    // ---- Update these 4 values to match your local MySQL instance ----
    public static final String URL      = "jdbc:mysql://localhost:3306/employee_db?useSSL=false&serverTimezone=UTC";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "root";
    public static final String DRIVER   = "com.mysql.cj.jdbc.Driver";

    // Prevent instantiation - this is a pure constant holder (utility class pattern)
    private DBConfig() { }
}

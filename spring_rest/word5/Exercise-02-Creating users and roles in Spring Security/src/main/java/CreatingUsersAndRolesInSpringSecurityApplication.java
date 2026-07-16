import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Exercise 02: Creating users and roles in Spring Security.
 *
 * Entry point of the application. Builds on Exercise 01 by defining
 * multiple users, each carrying one or more roles (USER / ADMIN), and
 * demonstrates role-based authorization on different endpoints.
 */
@SpringBootApplication
public class CreatingUsersAndRolesInSpringSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(CreatingUsersAndRolesInSpringSecurityApplication.class, args);
    }
}

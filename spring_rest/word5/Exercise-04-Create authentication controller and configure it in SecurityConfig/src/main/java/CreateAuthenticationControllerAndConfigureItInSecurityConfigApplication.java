import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Exercise 04: Create authentication controller and configure it in
 * SecurityConfig.
 *
 * Entry point of the application. Wires together:
 *   - JwtService          (from Exercise 03)
 *   - AuthController      (new: exposes POST /authenticate)
 *   - SecurityConfig      (updated: permits /authenticate, secures the rest)
 *
 * A client posts a username/password to /authenticate and, if the
 * Spring AuthenticationManager approves the credentials, receives a
 * signed JWT in the response body.
 */
@SpringBootApplication
public class CreateAuthenticationControllerAndConfigureItInSecurityConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                CreateAuthenticationControllerAndConfigureItInSecurityConfigApplication.class, args);
    }
}

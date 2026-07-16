import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Exercise 06: Authorize based on JWT.
 *
 * This is the capstone exercise, bringing everything together:
 *   - JwtService              (Exercise 03) generates/validates tokens
 *   - AuthController          (Exercise 04) issues tokens at /authenticate
 *   - JwtAuthenticationFilter (new)         reads "Authorization: Bearer
 *                                            &lt;token&gt;" on every request,
 *                                            validates it, and - if valid -
 *                                            populates the SecurityContext
 *                                            so downstream role checks
 *                                            (hasRole/hasAnyRole) work.
 *   - SecurityConfig          (updated)     wires the filter into the
 *                                            chain and removes HTTP Basic
 *                                            in favour of stateless JWT
 *                                            authentication.
 */
@SpringBootApplication
public class AuthorizeBasedOnJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizeBasedOnJwtApplication.class, args);
    }
}

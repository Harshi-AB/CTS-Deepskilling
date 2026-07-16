import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Exercise 03: Create authentication service that returns JWT.
 *
 * Entry point of the application. Introduces the {@link JwtService},
 * which is responsible for generating and validating signed JSON Web
 * Tokens. A demonstration endpoint is exposed so the token generation
 * can be exercised over HTTP before wiring it into a full login flow
 * in Exercise 04.
 */
@SpringBootApplication
public class CreateAuthenticationServiceThatReturnsJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(CreateAuthenticationServiceThatReturnsJwtApplication.class, args);
    }
}

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Exercise 05: Read Authorization header and decode the username and
 * password.
 *
 * This exercise demonstrates - without relying on Spring Security's
 * automatic HTTP Basic support - exactly how an "Authorization: Basic
 * &lt;base64&gt;" header is structured and decoded by hand:
 *
 *   Authorization: Basic dXNlcjpwYXNz
 *
 * where "dXNlcjpwYXNz" is Base64 for "user:pass". This low-level
 * understanding is what Spring Security's BasicAuthenticationFilter
 * performs internally before Exercise 01/02's filter chain ever runs.
 */
@SpringBootApplication
public class ReadAuthorizationHeaderAndDecodeTheUsernameAndPasswordApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                ReadAuthorizationHeaderAndDecodeTheUsernameAndPasswordApplication.class, args);
    }
}

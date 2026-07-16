import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Exercise 01: Securing RESTful Web Services with Spring Security.
 *
 * This is the entry point of the Spring Boot application. It bootstraps the
 * embedded Tomcat server, sets up the Spring application context and
 * triggers component scanning for controllers and security configuration
 * classes that live alongside this class in the default (unnamed) package.
 *
 * No package declaration is used anywhere in this project, as required.
 */
@SpringBootApplication
public class SecuringRestfulWebServicesWithSpringSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecuringRestfulWebServicesWithSpringSecurityApplication.class, args);
    }
}

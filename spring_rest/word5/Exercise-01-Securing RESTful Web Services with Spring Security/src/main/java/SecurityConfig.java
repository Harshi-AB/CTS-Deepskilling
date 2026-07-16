import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Central Spring Security configuration.
 *
 * This class demonstrates the fundamentals of securing a RESTful web
 * service: a single in-memory user is defined and HTTP Basic
 * authentication is required to access protected endpoints, while a
 * public endpoint is left open to demonstrate the contrast.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Defines a single in-memory user "restuser" with password "restpass".
     * PasswordEncoderFactories.createDelegatingPasswordEncoder() lets us
     * store the password using the recommended {bcrypt} prefix scheme.
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails restUser = User.withUsername("restuser")
                .password(passwordEncoder.encode("restpass"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(restUser);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * Defines the HTTP security rules:
     *  - /api/public/**  -> open to everyone
     *  - everything else -> requires authentication (HTTP Basic)
     * CSRF is disabled because this is a stateless REST API tested with
     * tools like curl/Postman rather than a browser form.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasicConfig -> {}); // Enable HTTP Basic authentication

        return http.build();
    }
}

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Because spring-boot-starter-security is on the classpath, Spring Boot
 * would otherwise secure every endpoint with a randomly generated
 * password by default. This exercise focuses purely on JWT
 * *generation*, so the demo endpoints are explicitly permitted here.
 * Full JWT-based request authorization is introduced in Exercise 06.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/token/**").permitAll()
                .anyRequest().permitAll()
            );

        return http.build();
    }
}

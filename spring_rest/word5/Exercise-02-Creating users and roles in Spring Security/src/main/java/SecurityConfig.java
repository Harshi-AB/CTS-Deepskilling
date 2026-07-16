import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration demonstrating the creation of multiple users,
 * each assigned one or more roles, and role-based (authority-based)
 * access control on different URL patterns.
 *
 * Users defined:
 *   - "user"  / "user123"  -> ROLE_USER
 *   - "admin" / "admin123" -> ROLE_USER, ROLE_ADMIN
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails normalUser = User.withUsername("user")
                .password(passwordEncoder.encode("user123"))
                .roles("USER")
                .build();

        UserDetails adminUser = User.withUsername("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("USER", "ADMIN") // Admin also has USER privileges
                .build();

        return new InMemoryUserDetailsManager(normalUser, adminUser);
    }

    /**
     * Authorization rules:
     *   - /api/public/**  -> open to everyone
     *   - /api/user/**    -> requires ROLE_USER or ROLE_ADMIN
     *   - /api/admin/**   -> requires ROLE_ADMIN only
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasicConfig -> {});

        return http.build();
    }
}

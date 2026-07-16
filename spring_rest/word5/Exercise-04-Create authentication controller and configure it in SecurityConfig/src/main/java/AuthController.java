import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication controller: the single entry point clients use to
 * exchange a username/password for a signed JWT.
 *
 * Flow:
 *   1. Client POSTs { "username": ..., "password": ... } to /authenticate
 *   2. The Spring {@link AuthenticationManager} verifies the credentials
 *      against the UserDetailsService configured in SecurityConfig.
 *   3. If valid, a JWT is generated via {@link JwtService} and returned.
 *   4. If invalid, an HTTP 401 with an error message is returned.
 */
@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest) {
        try {
            // Delegates to Spring Security's authentication provider,
            // which checks the password against the stored (encoded) one.
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        String token = jwtService.generateToken(authRequest.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}

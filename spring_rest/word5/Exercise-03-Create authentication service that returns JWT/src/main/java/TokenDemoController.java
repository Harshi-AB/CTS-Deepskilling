import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Demonstration controller that shows the {@link JwtService} in action
 * before it is wired into a full login endpoint (that comes in
 * Exercise 04). Two endpoints are exposed:
 *   - /api/token/generate : builds a JWT for a given username
 *   - /api/token/validate : validates a previously issued JWT
 */
@RestController
public class TokenDemoController {

    @Autowired
    private JwtService jwtService;

    @GetMapping("/api/token/generate")
    public Map<String, String> generateToken(@RequestParam String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("issuedBy", "CognizantNurtureExercise03");

        String token = jwtService.generateToken(claims, username);

        Map<String, String> response = new HashMap<>();
        response.put("username", username);
        response.put("token", token);
        return response;
    }

    @GetMapping("/api/token/validate")
    public Map<String, Object> validateToken(@RequestParam String token, @RequestParam String username) {
        boolean valid = jwtService.validateToken(token, username);

        Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("valid", valid);
        response.put("extractedUsername", jwtService.extractUsername(token));
        return response;
    }
}

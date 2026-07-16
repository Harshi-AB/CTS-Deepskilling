import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller demonstrating manual reading and decoding of the raw
 * "Authorization" HTTP header, using {@link BasicAuthDecoder}.
 *
 * Try it with curl:
 *   curl -H "Authorization: Basic dXNlcjpwYXNz" http://localhost:8085/api/decode
 */
@RestController
public class AuthHeaderController {

    @GetMapping("/api/decode")
    public Map<String, String> decodeAuthorizationHeader(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        Map<String, String> response = new HashMap<>();

        try {
            BasicAuthDecoder.Credentials credentials = BasicAuthDecoder.decode(authorizationHeader);
            response.put("status", "SUCCESS");
            response.put("username", credentials.getUsername());
            response.put("password", credentials.getPassword());
        } catch (IllegalArgumentException e) {
            response.put("status", "ERROR");
            response.put("message", e.getMessage());
        }

        return response;
    }
}

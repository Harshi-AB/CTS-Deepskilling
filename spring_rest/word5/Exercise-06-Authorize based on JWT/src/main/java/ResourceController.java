import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Protected resources used to prove that access is now being granted or
 * denied purely based on the claims inside a caller's JWT, with no
 * server-side session involved at all.
 */
@RestController
@RequestMapping("/api")
public class ResourceController {

    @GetMapping("/user/profile")
    public String userProfile(Authentication authentication) {
        return "Hello " + authentication.getName()
                + "! Access GRANTED to /api/user/profile based on your JWT. Authorities: "
                + authentication.getAuthorities();
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Authentication authentication) {
        return "Welcome " + authentication.getName()
                + "! Access GRANTED to /api/admin/dashboard based on your JWT. Authorities: "
                + authentication.getAuthorities();
    }
}

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;

/**
 * REST controller exposing three endpoints, each requiring a different
 * level of privilege, to demonstrate role-based authorization.
 */
@RestController
@RequestMapping("/api")
public class RoleController {

    @GetMapping("/public/info")
    public String publicInfo() {
        return "This is public information. No login required.";
    }

    @GetMapping("/user/profile")
    public String userProfile(Principal principal) {
        return "Hello " + principal.getName() + ", this is your USER profile page.";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Principal principal) {
        return "Welcome " + principal.getName() + ", this is the ADMIN dashboard.";
    }
}

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;

/**
 * A couple of sample protected endpoints used to confirm that a user who
 * has authenticated (either via HTTP Basic, or by first hitting
 * /authenticate to prove their credentials) can reach role-protected
 * resources.
 */
@RestController
@RequestMapping("/api")
public class SampleController {

    @GetMapping("/user/profile")
    public String userProfile(Principal principal) {
        return "Hello " + principal.getName() + ", this is your USER profile.";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Principal principal) {
        return "Welcome " + principal.getName() + ", this is the ADMIN dashboard.";
    }
}

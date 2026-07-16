import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;

/**
 * A simple REST controller exposing one public and one secured endpoint,
 * used to demonstrate that Spring Security is correctly protecting the
 * secured endpoint while leaving the public one open.
 */
@RestController
@RequestMapping("/api")
public class HelloController {

    /** Open to everyone - no credentials required. */
    @GetMapping("/public/hello")
    public String publicHello() {
        return "Hello, anonymous visitor! This endpoint is public.";
    }

    /**
     * Protected endpoint - Spring Security will demand HTTP Basic
     * credentials before this method is ever invoked.
     */
    @GetMapping("/secure/hello")
    public String secureHello(Principal principal) {
        return "Hello, " + principal.getName() + "! You have accessed a SECURED endpoint.";
    }
}

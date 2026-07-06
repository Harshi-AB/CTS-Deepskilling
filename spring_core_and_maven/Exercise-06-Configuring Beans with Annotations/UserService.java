/**
 * UserService.java
 *
 * A @Component bean that declares a dependency on UserRepository via
 * an @Autowired field. The container is responsible for detecting
 * this annotation and injecting the correct instance, exactly like
 * Spring's annotation-based dependency injection.
 */
@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String getUserDetails(int id) {
        return "Details -> " + userRepository.findUserById(id);
    }
}

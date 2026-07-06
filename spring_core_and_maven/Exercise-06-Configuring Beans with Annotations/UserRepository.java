/**
 * UserRepository.java
 *
 * A simple @Component bean representing the data-access layer.
 */
@Component
public class UserRepository {

    public String findUserById(int id) {
        return "User#" + id + " (Harshitha)";
    }
}

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an end-user (Resource Owner) registered with the
 * Centralized Authentication system.
 *
 * In a real OIDC provider this would be backed by a user directory
 * (LDAP / database). Here it is kept in memory to demonstrate the
 * concept in Core Java.
 */
public class User {

    private final String username;
    private final String password;
    private final Map<String, Object> claims; // OIDC identity claims (name, email, etc.)

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.claims = new HashMap<>();
    }

    public String getUsername() {
        return username;
    }

    /**
     * Verifies the supplied credentials against this user's stored password.
     */
    public boolean checkPassword(String candidatePassword) {
        return this.password.equals(candidatePassword);
    }

    public void addClaim(String key, Object value) {
        claims.put(key, value);
    }

    public Map<String, Object> getClaims() {
        return new HashMap<>(claims);
    }

    @Override
    public String toString() {
        return "User{username='" + username + "'}";
    }
}

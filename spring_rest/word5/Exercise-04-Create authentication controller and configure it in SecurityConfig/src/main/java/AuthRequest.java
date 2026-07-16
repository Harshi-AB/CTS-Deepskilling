/**
 * Simple data-transfer object representing the JSON body of a login
 * request: { "username": "...", "password": "..." }
 *
 * A plain POJO with getters/setters is used (rather than a Java record)
 * so that Jackson can deserialize incoming JSON into it via the
 * no-argument constructor + setters convention.
 */
public class AuthRequest {

    private String username;
    private String password;

    public AuthRequest() {
        // Required no-arg constructor for Jackson JSON deserialization
    }

    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

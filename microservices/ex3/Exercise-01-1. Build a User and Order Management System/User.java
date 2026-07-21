import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User is the domain model managed by the User Service.
 * Encapsulates id, name and email with standard getters/setters (OOP).
 */
public class User {

    private int id;
    private String name;
    private String email;

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Converts this User into a JSON string using the shared JsonUtil helper.
     */
    public String toJson() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("email", email);
        return JsonUtil.toJson(map);
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', email='" + email + "'}";
    }
}

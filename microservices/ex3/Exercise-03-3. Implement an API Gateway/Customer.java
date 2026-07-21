import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Customer is the domain model managed by the Customer Service.
 */
public class Customer {

    private int id;
    private String name;
    private String city;

    public Customer(int id, String name, String city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String toJson() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("city", city);
        return JsonUtil.toJson(map);
    }
}

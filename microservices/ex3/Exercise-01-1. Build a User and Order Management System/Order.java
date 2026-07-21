import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Order is the domain model managed by the Order Service.
 * Each order references the id of the User who placed it (foreign-key style
 * relationship, mirroring how a real Order microservice would only store
 * the userId and fetch full user details from the User Service on demand).
 */
public class Order {

    private int id;
    private int userId;
    private String product;
    private int quantity;
    private String status;

    public Order(int id, int userId, String product, int quantity, String status) {
        this.id = id;
        this.userId = userId;
        this.product = product;
        this.quantity = quantity;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Converts this Order into a JSON string.
     */
    public String toJson() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("userId", userId);
        map.put("product", product);
        map.put("quantity", quantity);
        map.put("status", status);
        return JsonUtil.toJson(map);
    }

    /**
     * Converts this Order into JSON, additionally embedding the full user
     * details fetched from the User Service. This demonstrates
     * inter-service composition at the API layer. Builds the nested object
     * manually so the "user" field is a real JSON object, not an escaped
     * string.
     */
    public String toJsonWithUser(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"id\":").append(id).append(",");
        sb.append("\"product\":\"").append(product).append("\",");
        sb.append("\"quantity\":").append(quantity).append(",");
        sb.append("\"status\":\"").append(status).append("\",");
        sb.append("\"user\":").append(user == null ? "null" : user.toJson());
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Order{id=" + id + ", userId=" + userId + ", product='" + product
                + "', quantity=" + quantity + ", status='" + status + "'}";
    }
}

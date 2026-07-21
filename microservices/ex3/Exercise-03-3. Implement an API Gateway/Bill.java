import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Bill is the domain model managed by the Billing Service.
 */
public class Bill {

    private int id;
    private int customerId;
    private double amount;
    private String status;

    public Bill(int id, int customerId, double amount, String status) {
        this.id = id;
        this.customerId = customerId;
        this.amount = amount;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public String toJson() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("customerId", customerId);
        map.put("amount", amount);
        map.put("status", status);
        return JsonUtil.toJson(map);
    }
}

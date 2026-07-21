import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Product is the domain model managed by the Product Service.
 * Holds the catalogue information plus the current stock count.
 */
public class Product {

    private int id;
    private String name;
    private double price;
    private int stock;

    public Product(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String toJson() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("price", price);
        map.put("stock", stock);
        return JsonUtil.toJson(map);
    }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', price=" + price + ", stock=" + stock + "}";
    }
}

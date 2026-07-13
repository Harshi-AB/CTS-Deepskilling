import java.util.ArrayList;
import java.util.List;

/**
 * Sort.java
 *
 * Exercise 06 - Implementing Pagination and Sorting
 * -----------------------------------------------------
 * Custom stand-in for org.springframework.data.domain.Sort.
 * Represents an ordered list of "sort by column X, direction Y" rules,
 * translated into a plain SQL ORDER BY clause by PageRequest/Page code.
 * Design pattern: Builder (via static factory methods) + Value Object.
 */
public class Sort {

    public enum Direction { ASC, DESC }

    public static class Order {
        private final String property;
        private final Direction direction;

        public Order(String property, Direction direction) {
            this.property = property;
            this.direction = direction;
        }

        public String getProperty() { return property; }
        public Direction getDirection() { return direction; }
    }

    private final List<Order> orders = new ArrayList<>();

    private Sort(List<Order> orders) {
        this.orders.addAll(orders);
    }

    public static Sort by(String property) {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(property, Direction.ASC));
        return new Sort(orders);
    }

    public static Sort by(Direction direction, String property) {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(property, direction));
        return new Sort(orders);
    }

    public static Sort unsorted() {
        return new Sort(new ArrayList<>());
    }

    public Sort and(Sort other) {
        List<Order> combined = new ArrayList<>(this.orders);
        combined.addAll(other.orders);
        return new Sort(combined);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public boolean isSorted() {
        return !orders.isEmpty();
    }
}

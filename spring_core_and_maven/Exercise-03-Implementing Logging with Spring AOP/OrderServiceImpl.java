/**
 * OrderServiceImpl.java
 *
 * Concrete "business logic" implementation. Notice this class knows
 * nothing about logging - the cross-cutting concern is woven in
 * separately by the proxy, which is the entire point of AOP.
 */
public class OrderServiceImpl implements OrderService {

    @Override
    public String placeOrder(String item, int quantity) {
        String orderId = "ORD-" + (int) (Math.random() * 9000 + 1000);
        System.out.println("   -> Processing order for " + quantity + " x " + item);
        return orderId;
    }

    @Override
    public void cancelOrder(String orderId) {
        System.out.println("   -> Cancelling order " + orderId);
    }
}

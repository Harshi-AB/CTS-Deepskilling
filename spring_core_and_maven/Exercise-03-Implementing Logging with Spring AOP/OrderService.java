/**
 * OrderService.java
 *
 * Target interface whose methods will be intercepted by our AOP-style
 * logging proxy, just like Spring AOP intercepts calls to beans
 * matched by a pointcut expression.
 */
public interface OrderService {
    String placeOrder(String item, int quantity);
    void cancelOrder(String orderId);
}

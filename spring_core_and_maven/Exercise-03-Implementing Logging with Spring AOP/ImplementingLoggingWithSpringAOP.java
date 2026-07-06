/**
 * ImplementingLoggingWithSpringAOP.java
 *
 * Demonstrates AOP-style logging using a JDK dynamic proxy, since the
 * real Spring AOP module cannot be used in this exercise. The proxy
 * transparently adds logging (a cross-cutting concern) around the
 * real OrderServiceImpl business logic, without OrderServiceImpl
 * having any knowledge of logging.
 */
public class ImplementingLoggingWithSpringAOP {

    public static void main(String[] args) {
        // Real business object (the "target")
        OrderService realService = new OrderServiceImpl();

        // Wrap it with a logging proxy - conceptually an "advised bean"
        OrderService proxiedService = ProxyFactory.createLoggingProxy(realService, OrderService.class);

        System.out.println("Calling placeOrder() through the AOP proxy:");
        String orderId = proxiedService.placeOrder("Laptop", 2);

        System.out.println("\nCalling cancelOrder() through the AOP proxy:");
        proxiedService.cancelOrder(orderId);
    }
}

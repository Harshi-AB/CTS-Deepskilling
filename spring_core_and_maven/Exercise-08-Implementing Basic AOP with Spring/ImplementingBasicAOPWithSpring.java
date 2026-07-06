import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

/**
 * ImplementingBasicAOPWithSpring.java
 *
 * Demonstrates basic AOP concepts - before advice, after-returning
 * advice, and around-style behaviour - using JDK dynamic proxies,
 * since the real Spring AOP module is not permitted in this exercise.
 */
public class ImplementingBasicAOPWithSpring {

    public static void main(String[] args) {
        PaymentService target = new PaymentServiceImpl();

        List<BeforeAdvice> beforeAdvices = Arrays.asList(new LoggingBeforeAdvice());
        List<AfterAdvice> afterAdvices = Arrays.asList(new LoggingAfterAdvice());

        AopProxyHandler handler = new AopProxyHandler(target, beforeAdvices, afterAdvices);

        PaymentService proxiedService = (PaymentService) Proxy.newProxyInstance(
                PaymentService.class.getClassLoader(),
                new Class<?>[]{PaymentService.class},
                handler
        );

        System.out.println("Invoking processPayment() through the AOP proxy:\n");
        String transactionId = proxiedService.processPayment(2500.00);
        System.out.println("\nFinal transaction id received by caller: " + transactionId);
    }
}

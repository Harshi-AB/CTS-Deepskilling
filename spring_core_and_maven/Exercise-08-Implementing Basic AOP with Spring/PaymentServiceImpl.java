/**
 * PaymentServiceImpl.java
 *
 * Core business logic, completely unaware of any advice (logging,
 * validation, timing) that will later be woven around it.
 */
public class PaymentServiceImpl implements PaymentService {
    @Override
    public String processPayment(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
        System.out.println("   -> Charging amount: " + amount);
        return "TXN-" + (int) (Math.random() * 90000 + 10000);
    }
}

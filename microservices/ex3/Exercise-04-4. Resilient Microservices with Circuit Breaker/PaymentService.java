/**
 * PaymentService represents the Payment Service described in the
 * exercise: it needs to call a slow, occasionally-failing third-party
 * payment API, but must remain resilient rather than letting failures
 * cascade or letting callers hang on a slow dependency.
 *
 * It does this by wrapping every call to ThirdPartyPaymentGateway with
 * a CircuitBreaker:
 *   - While the breaker is CLOSED/HALF_OPEN, the real call is attempted.
 *   - Successes reset the breaker; failures count towards tripping it.
 *   - Once the breaker is OPEN, PaymentService skips the real call
 *     entirely and immediately returns a FALLBACK result instead,
 *     logging the event via FallbackMonitor.
 */
public class PaymentService {

    private final ThirdPartyPaymentGateway paymentGateway;
    private final CircuitBreaker circuitBreaker;
    private final FallbackMonitor fallbackMonitor;

    public PaymentService(ThirdPartyPaymentGateway paymentGateway,
                           CircuitBreaker circuitBreaker,
                           FallbackMonitor fallbackMonitor) {
        this.paymentGateway = paymentGateway;
        this.circuitBreaker = circuitBreaker;
        this.fallbackMonitor = fallbackMonitor;
    }

    /**
     * Processes a payment for the given order, protected by the circuit
     * breaker. Returns a human-readable result describing what happened
     * (successful charge, gateway failure handled via fallback, or
     * short-circuited straight to fallback because the breaker is OPEN).
     */
    public String processPayment(int orderId, double amount) {
        if (!circuitBreaker.allowRequest()) {
            fallbackMonitor.recordFallback(orderId, "circuit OPEN - third-party gateway skipped entirely");
            return fallbackResponse(orderId, amount);
        }

        try {
            String transactionId = paymentGateway.charge(amount);
            circuitBreaker.recordSuccess();
            return "Order #" + orderId + " -> SUCCESS (transactionId=" + transactionId + ")";
        } catch (PaymentGatewayException e) {
            circuitBreaker.recordFailure();
            fallbackMonitor.recordFallback(orderId, e.getMessage());
            return fallbackResponse(orderId, amount);
        }
    }

    /**
     * The fallback logic: rather than propagating the failure to the
     * caller, queue the payment for later retry and let the customer
     * know it is being processed - a common, user-friendly fallback
     * strategy for payment systems.
     */
    private String fallbackResponse(int orderId, double amount) {
        return "Order #" + orderId + " -> FALLBACK (amount " + amount
                + " queued for retry; customer notified payment is processing)";
    }

    public CircuitBreaker.State getCircuitState() {
        return circuitBreaker.getState();
    }
}

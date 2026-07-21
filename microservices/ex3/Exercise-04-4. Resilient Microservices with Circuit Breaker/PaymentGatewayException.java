/**
 * PaymentGatewayException represents a failure (timeout, error response,
 * connection refused, etc.) when calling the third-party payment API.
 * Declared as a checked exception so calling code is forced to
 * explicitly handle failures - which is exactly where the Circuit
 * Breaker's recordFailure()/fallback logic hooks in.
 */
public class PaymentGatewayException extends Exception {

    public PaymentGatewayException(String message) {
        super(message);
    }
}

/**
 * ThirdPartyPaymentGateway simulates the slow, occasionally unreliable
 * third-party payment API described in the exercise. It deliberately:
 *
 *   - Adds artificial latency to every call, to represent a "slow"
 *     external dependency.
 *   - Fails deterministically for its first several invocations to
 *     represent an outage, then recovers - this lets the demo clearly
 *     show the Circuit Breaker tripping OPEN and later recovering to
 *     CLOSED without relying on real network conditions.
 */
public class ThirdPartyPaymentGateway {

    private int callCount = 0;
    private final int failuresBeforeRecovery;

    public ThirdPartyPaymentGateway(int failuresBeforeRecovery) {
        this.failuresBeforeRecovery = failuresBeforeRecovery;
    }

    /**
     * Attempts to charge the given amount through the (simulated) third
     * party API. Throws a PaymentGatewayException to represent a failed
     * or timed-out call.
     */
    public String charge(double amount) throws PaymentGatewayException {
        callCount++;
        simulateNetworkLatency();

        if (callCount <= failuresBeforeRecovery) {
            throw new PaymentGatewayException(
                    "Third-party payment gateway timed out (simulated outage, call #" + callCount + ")");
        }

        return "TXN-" + System.currentTimeMillis() + "-" + callCount;
    }

    private void simulateNetworkLatency() {
        try {
            Thread.sleep(150); // simulate a "slow" third-party API
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

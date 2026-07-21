/**
 * CircuitBreaker implements the Circuit Breaker design pattern, the same
 * pattern provided by Resilience4j in a real Spring Boot application.
 *
 * The breaker has three states:
 *
 *   CLOSED     - Normal operation. Calls pass through to the real
 *                dependency. Consecutive failures are counted; once
 *                they reach {@code failureThreshold}, the breaker
 *                TRIPS to OPEN.
 *   OPEN       - Calls are rejected immediately (no attempt is made to
 *                reach the failing dependency), and the fallback path
 *                is used instead. After {@code resetTimeoutMillis} has
 *                elapsed, the breaker moves to HALF_OPEN to test
 *                whether the dependency has recovered.
 *   HALF_OPEN  - A single trial call is allowed through. If it
 *                succeeds, the breaker resets to CLOSED. If it fails,
 *                the breaker trips back to OPEN and the timeout starts
 *                again.
 */
public class CircuitBreaker {

    /** The three states a Circuit Breaker can be in. */
    public enum State {
        CLOSED, OPEN, HALF_OPEN
    }

    private final String name;
    private final int failureThreshold;
    private final long resetTimeoutMillis;

    private State state = State.CLOSED;
    private int consecutiveFailures = 0;
    private long lastFailureTimestampMillis = 0;

    public CircuitBreaker(String name, int failureThreshold, long resetTimeoutMillis) {
        this.name = name;
        this.failureThreshold = failureThreshold;
        this.resetTimeoutMillis = resetTimeoutMillis;
    }

    /**
     * Determines whether a call should be allowed to actually reach the
     * dependency, evaluating and updating the state machine as needed.
     */
    public synchronized boolean allowRequest() {
        if (state == State.OPEN) {
            long elapsed = System.currentTimeMillis() - lastFailureTimestampMillis;
            if (elapsed >= resetTimeoutMillis) {
                state = State.HALF_OPEN;
                System.out.println("[CircuitBreaker:" + name + "] Timeout elapsed -> transitioning OPEN -> HALF_OPEN");
                return true; // allow exactly one trial call
            }
            return false; // still OPEN, reject
        }
        // CLOSED or HALF_OPEN both allow the call through
        return true;
    }

    /** Records that the most recent call (allowed by allowRequest) succeeded. */
    public synchronized void recordSuccess() {
        if (state == State.HALF_OPEN) {
            System.out.println("[CircuitBreaker:" + name + "] Trial call succeeded -> HALF_OPEN -> CLOSED");
        }
        state = State.CLOSED;
        consecutiveFailures = 0;
    }

    /** Records that the most recent call (allowed by allowRequest) failed. */
    public synchronized void recordFailure() {
        consecutiveFailures++;
        lastFailureTimestampMillis = System.currentTimeMillis();

        if (state == State.HALF_OPEN) {
            System.out.println("[CircuitBreaker:" + name + "] Trial call failed -> HALF_OPEN -> OPEN");
            state = State.OPEN;
            return;
        }

        if (state == State.CLOSED && consecutiveFailures >= failureThreshold) {
            System.out.println("[CircuitBreaker:" + name + "] Failure threshold (" + failureThreshold
                    + ") reached -> CLOSED -> OPEN");
            state = State.OPEN;
        }
    }

    public synchronized State getState() {
        return state;
    }

    public synchronized int getConsecutiveFailures() {
        return consecutiveFailures;
    }
}

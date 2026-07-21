import java.util.LinkedList;

/**
 * CircuitBreaker is a plain-Java re-implementation of the core algorithm
 * behind Resilience4j's CircuitBreaker, configured from the
 * application.properties in the exercise:
 *
 *   resilience4j.circuitbreaker.instances.exampleCircuitBreaker.registerHealthIndicator=true
 *   resilience4j.circuitbreaker.instances.exampleCircuitBreaker.slidingWindowSize=10
 *   resilience4j.circuitbreaker.instances.exampleCircuitBreaker.failureRateThreshold=50
 *
 * State machine:
 *   CLOSED    -> calls pass through; results (true=success/false=failure)
 *                are recorded in a fixed-size sliding window. Once the
 *                window is full and the failure rate reaches the
 *                configured threshold, the breaker trips to OPEN.
 *   OPEN      -> calls are rejected immediately without even attempting
 *                the remote call ("fail fast"), until waitDurationInOpenState
 *                has elapsed, at which point the breaker moves to HALF_OPEN.
 *   HALF_OPEN -> a small number of trial calls are permitted through;
 *                if they succeed well enough, the breaker resets to
 *                CLOSED, otherwise it trips back to OPEN.
 */
public class CircuitBreaker {

    private final String name;
    private final CircuitBreakerConfig config;

    private volatile CircuitBreakerState state = CircuitBreakerState.CLOSED;
    private final LinkedList<Boolean> slidingWindow = new LinkedList<>(); // true = success, false = failure
    private long openedAtMillis = 0L;
    private int halfOpenCallsMade = 0;
    private int halfOpenSuccesses = 0;

    public CircuitBreaker(String name, CircuitBreakerConfig config) {
        this.name = name;
        this.config = config;
    }

    public synchronized CircuitBreakerState getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    /**
     * Called by the gateway before attempting a downstream call.
     * Returns true if the call is permitted to proceed.
     */
    public synchronized boolean tryAcquirePermission() {
        if (state == CircuitBreakerState.OPEN) {
            long elapsed = System.currentTimeMillis() - openedAtMillis;
            if (elapsed >= config.getWaitDurationInOpenStateMillis()) {
                transitionToHalfOpen();
                return true;
            }
            return false; // still OPEN -> fail fast, reject the call
        }
        if (state == CircuitBreakerState.HALF_OPEN) {
            return halfOpenCallsMade < config.getPermittedCallsInHalfOpenState();
        }
        return true; // CLOSED
    }

    /** Records a successful call outcome. */
    public synchronized void onSuccess() {
        if (state == CircuitBreakerState.HALF_OPEN) {
            halfOpenCallsMade++;
            halfOpenSuccesses++;
            evaluateHalfOpenOutcome();
            return;
        }
        recordResult(true);
        evaluateFailureRate();
    }

    /** Records a failed call outcome (including timeouts). */
    public synchronized void onFailure() {
        if (state == CircuitBreakerState.HALF_OPEN) {
            halfOpenCallsMade++;
            evaluateHalfOpenOutcome();
            return;
        }
        recordResult(false);
        evaluateFailureRate();
    }

    private void recordResult(boolean success) {
        slidingWindow.addLast(success);
        if (slidingWindow.size() > config.getSlidingWindowSize()) {
            slidingWindow.removeFirst();
        }
    }

    private void evaluateFailureRate() {
        if (slidingWindow.size() < config.getSlidingWindowSize()) {
            return; // not enough data yet to make a decision
        }
        long failures = slidingWindow.stream().filter(success -> !success).count();
        double failureRate = (failures * 100.0) / slidingWindow.size();
        if (failureRate >= config.getFailureRateThreshold()) {
            transitionToOpen(failureRate);
        }
    }

    private void evaluateHalfOpenOutcome() {
        if (halfOpenCallsMade < config.getPermittedCallsInHalfOpenState()) {
            return; // still probing
        }
        double successRate = (halfOpenSuccesses * 100.0) / halfOpenCallsMade;
        if (successRate >= (100.0 - config.getFailureRateThreshold())) {
            transitionToClosed();
        } else {
            transitionToOpen(100.0 - successRate);
        }
    }

    private void transitionToOpen(double observedFailureRate) {
        state = CircuitBreakerState.OPEN;
        openedAtMillis = System.currentTimeMillis();
        slidingWindow.clear();
        System.out.printf("[CircuitBreaker:%s] Tripped to OPEN (observed failure rate=%.1f%%, threshold=%.1f%%)%n",
                name, observedFailureRate, config.getFailureRateThreshold());
    }

    private void transitionToHalfOpen() {
        state = CircuitBreakerState.HALF_OPEN;
        halfOpenCallsMade = 0;
        halfOpenSuccesses = 0;
        System.out.printf("[CircuitBreaker:%s] Wait duration elapsed -> moving to HALF_OPEN%n", name);
    }

    private void transitionToClosed() {
        state = CircuitBreakerState.CLOSED;
        slidingWindow.clear();
        System.out.printf("[CircuitBreaker:%s] Trial calls succeeded -> resetting to CLOSED%n", name);
    }
}

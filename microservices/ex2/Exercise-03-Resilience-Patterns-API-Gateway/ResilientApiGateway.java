/**
 * ResilientApiGateway plays the role of the API Gateway route/filter
 * that, in the real Spring Cloud Gateway world, would be decorated with
 * a Resilience4j CircuitBreaker filter (backed by the
 * ReactiveResilience4JCircuitBreakerFactory bean configured in
 * ResilienceConfiguration). It combines a TimeLimiter and a
 * CircuitBreaker to protect calls made to a RemoteService.
 */
public class ResilientApiGateway {

    private final CircuitBreaker circuitBreaker;
    private final TimeLimiter timeLimiter;

    public ResilientApiGateway(CircuitBreaker circuitBreaker, TimeLimiter timeLimiter) {
        this.circuitBreaker = circuitBreaker;
        this.timeLimiter = timeLimiter;
    }

    /**
     * Routes a single request through the resilience layer:
     *   1. Ask the CircuitBreaker for permission (fail fast if OPEN).
     *   2. If permitted, execute the call under the TimeLimiter.
     *   3. Report the outcome (success/failure) back to the CircuitBreaker.
     */
    public String routeRequest(String requestId, RemoteCall<String> call) {
        if (!circuitBreaker.tryAcquirePermission()) {
            return "[" + requestId + "] REJECTED - circuit '" + circuitBreaker.getName()
                    + "' is OPEN (fail fast, no call attempted)";
        }

        try {
            String result = timeLimiter.executeWithTimeout(call);
            circuitBreaker.onSuccess();
            return "[" + requestId + "] SUCCESS - " + result
                    + "  (circuit state=" + circuitBreaker.getState() + ")";
        } catch (RemoteServiceException e) {
            circuitBreaker.onFailure();
            return "[" + requestId + "] FAILURE - " + e.getMessage()
                    + "  (circuit state=" + circuitBreaker.getState() + ")";
        }
    }
}

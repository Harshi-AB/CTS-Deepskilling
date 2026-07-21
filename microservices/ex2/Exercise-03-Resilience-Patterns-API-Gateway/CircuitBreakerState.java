/**
 * CircuitBreakerState models the three states of the Circuit Breaker
 * pattern, exactly like Resilience4j's CircuitBreaker.State:
 *
 *   CLOSED     - calls flow through normally; failures are being counted.
 *   OPEN       - calls are rejected immediately (fail fast) without
 *                even attempting the remote call.
 *   HALF_OPEN  - a limited number of trial calls are let through to
 *                probe whether the downstream service has recovered.
 */
public enum CircuitBreakerState {
    CLOSED,
    OPEN,
    HALF_OPEN
}

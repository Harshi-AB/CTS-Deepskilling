/**
 * TimeLimiterConfig is the plain-Java equivalent of Resilience4j's
 * TimeLimiterConfig.ofDefaults(), used alongside the circuit breaker to
 * bound how long a downstream call is allowed to take before it is
 * treated as a failure (a timeout).
 */
public class TimeLimiterConfig {

    private final long timeoutMillis;

    private TimeLimiterConfig(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public static TimeLimiterConfig ofDefaults() {
        return new TimeLimiterConfig(1000); // 1 second, a sensible Resilience4j-like default
    }

    public static TimeLimiterConfig of(long timeoutMillis) {
        return new TimeLimiterConfig(timeoutMillis);
    }

    public long getTimeoutMillis() {
        return timeoutMillis;
    }
}

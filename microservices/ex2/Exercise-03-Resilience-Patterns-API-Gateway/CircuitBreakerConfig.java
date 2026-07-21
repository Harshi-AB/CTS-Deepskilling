/**
 * CircuitBreakerConfig is the plain-Java equivalent of the properties:
 *
 *   resilience4j.circuitbreaker.instances.exampleCircuitBreaker.registerHealthIndicator=true
 *   resilience4j.circuitbreaker.instances.exampleCircuitBreaker.slidingWindowSize=10
 *   resilience4j.circuitbreaker.instances.exampleCircuitBreaker.failureRateThreshold=50
 *
 * and of Resilience4j's CircuitBreakerConfig.ofDefaults()/builder API.
 * It simply groups the tunable parameters of a CircuitBreaker instance.
 */
public class CircuitBreakerConfig {

    private final int slidingWindowSize;
    private final double failureRateThreshold; // percentage, e.g. 50.0 = 50%
    private final int waitDurationInOpenStateMillis;
    private final int permittedCallsInHalfOpenState;
    private final boolean registerHealthIndicator;

    private CircuitBreakerConfig(Builder builder) {
        this.slidingWindowSize = builder.slidingWindowSize;
        this.failureRateThreshold = builder.failureRateThreshold;
        this.waitDurationInOpenStateMillis = builder.waitDurationInOpenStateMillis;
        this.permittedCallsInHalfOpenState = builder.permittedCallsInHalfOpenState;
        this.registerHealthIndicator = builder.registerHealthIndicator;
    }

    public static CircuitBreakerConfig ofDefaults() {
        return new Builder().build();
    }

    public int getSlidingWindowSize() {
        return slidingWindowSize;
    }

    public double getFailureRateThreshold() {
        return failureRateThreshold;
    }

    public int getWaitDurationInOpenStateMillis() {
        return waitDurationInOpenStateMillis;
    }

    public int getPermittedCallsInHalfOpenState() {
        return permittedCallsInHalfOpenState;
    }

    public boolean isRegisterHealthIndicator() {
        return registerHealthIndicator;
    }

    /** Builder, mirroring Resilience4JConfigBuilder used in the original exercise. */
    public static class Builder {
        private int slidingWindowSize = 10;              // matches application.properties
        private double failureRateThreshold = 50.0;      // matches application.properties
        private int waitDurationInOpenStateMillis = 5000;
        private int permittedCallsInHalfOpenState = 3;
        private boolean registerHealthIndicator = true;  // matches application.properties

        public Builder slidingWindowSize(int size) {
            this.slidingWindowSize = size;
            return this;
        }

        public Builder failureRateThreshold(double threshold) {
            this.failureRateThreshold = threshold;
            return this;
        }

        public Builder waitDurationInOpenStateMillis(int millis) {
            this.waitDurationInOpenStateMillis = millis;
            return this;
        }

        public Builder permittedCallsInHalfOpenState(int count) {
            this.permittedCallsInHalfOpenState = count;
            return this;
        }

        public Builder registerHealthIndicator(boolean value) {
            this.registerHealthIndicator = value;
            return this;
        }

        public CircuitBreakerConfig build() {
            return new CircuitBreakerConfig(this);
        }
    }
}

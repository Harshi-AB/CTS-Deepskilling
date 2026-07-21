/**
 * ResilienceConfiguration is the plain-Java equivalent of:
 *
 *   @Configuration
 *   public class ResilienceConfiguration {
 *       @Bean
 *       public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
 *           return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
 *                   .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
 *                   .timeLimiterConfig(TimeLimiterConfig.ofDefaults())
 *                   .build());
 *       }
 *   }
 *
 * Instead of a Spring @Bean factory, this class exposes a simple static
 * factory method that builds a fully wired CircuitBreaker + TimeLimiter
 * pair for a given logical name ("id" in the Resilience4j terms),
 * applying the same default configuration for every id - exactly what
 * "factory.configureDefault(...)" does.
 */
public class ResilienceConfiguration {

    /**
     * Builds the default CircuitBreakerConfig, matching the properties:
     *   slidingWindowSize=10
     *   failureRateThreshold=50
     *   registerHealthIndicator=true
     */
    public static CircuitBreakerConfig defaultCircuitBreakerConfig() {
        return new CircuitBreakerConfig.Builder()
                .slidingWindowSize(10)
                .failureRateThreshold(50.0)
                .waitDurationInOpenStateMillis(3000)
                .permittedCallsInHalfOpenState(3)
                .registerHealthIndicator(true)
                .build();
    }

    /** Builds the default TimeLimiterConfig, equivalent to TimeLimiterConfig.ofDefaults(). */
    public static TimeLimiterConfig defaultTimeLimiterConfig() {
        return TimeLimiterConfig.ofDefaults();
    }

    /**
     * Equivalent of factory.configureDefault(id -> ...): creates a new,
     * independently-tracked CircuitBreaker for the given id, using the
     * shared default configuration.
     */
    public static CircuitBreaker createCircuitBreaker(String id) {
        return new CircuitBreaker(id, defaultCircuitBreakerConfig());
    }

    /** Equivalent of the TimeLimiter half of the same customizer. */
    public static TimeLimiter createTimeLimiter() {
        return new TimeLimiter(defaultTimeLimiterConfig());
    }
}

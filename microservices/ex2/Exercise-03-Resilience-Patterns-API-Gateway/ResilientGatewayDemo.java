/**
 * ResilientGatewayDemo
 * ----------------------
 * Plain-Java re-creation of "Exercise 3: Resilience Patterns in an API
 * Gateway" (originally built with Spring Boot 3 + Spring Cloud Gateway
 * + Resilience4j).
 *
 * What used to live in application.properties:
 *
 *   resilience4j.circuitbreaker.instances.exampleCircuitBreaker.registerHealthIndicator=true
 *   resilience4j.circuitbreaker.instances.exampleCircuitBreaker.slidingWindowSize=10
 *   resilience4j.circuitbreaker.instances.exampleCircuitBreaker.failureRateThreshold=50
 *
 * is now built by ResilienceConfiguration.createCircuitBreaker(...),
 * and the @Bean defaultCustomizer() is now the same
 * ResilienceConfiguration class supplying the CircuitBreaker + TimeLimiter
 * used by ResilientApiGateway.
 *
 * The demo deliberately calls a RemoteService with a high failure
 * probability so you can watch the circuit breaker:
 *   1. start CLOSED and count failures in its sliding window,
 *   2. trip to OPEN once the failure-rate threshold (50%) is reached,
 *   3. reject calls instantly ("fail fast") while OPEN,
 *   4. move to HALF_OPEN after the configured wait duration,
 *   5. and either reset to CLOSED or trip back to OPEN based on the
 *      trial calls' outcome.
 */
public class ResilientGatewayDemo {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("===== Resilient API Gateway started =====");
        System.out.println();

        // 1. Build the circuit breaker + time limiter via the "resilience configuration"
        //    (equivalent of the @Bean Customizer<ReactiveResilience4JCircuitBreakerFactory>).
        CircuitBreaker circuitBreaker = ResilienceConfiguration.createCircuitBreaker("exampleCircuitBreaker");
        TimeLimiter timeLimiter = ResilienceConfiguration.createTimeLimiter();
        ResilientApiGateway gateway = new ResilientApiGateway(circuitBreaker, timeLimiter);

        // 2. An unreliable downstream service: fails 70% of the time.
        RemoteService flakyService = new RemoteService("example-service", 0.7, 0.0, 0);

        // 3. Phase 1: send enough traffic to fill the sliding window (size 10)
        //    and push the failure rate above the 50% threshold -> circuit trips OPEN.
        System.out.println("---- Phase 1: sending traffic to a failing downstream service ----");
        for (int i = 1; i <= 12; i++) {
            String requestId = "req-" + i;
            String outcome = gateway.routeRequest(requestId, flakyService::call);
            System.out.println(outcome);
        }

        // 4. Phase 2: circuit should now be OPEN - further calls are rejected immediately,
        //    without even touching the downstream service.
        System.out.println();
        System.out.println("---- Phase 2: circuit should be OPEN now (fail-fast rejections) ----");
        for (int i = 13; i <= 15; i++) {
            String outcome = gateway.routeRequest("req-" + i, flakyService::call);
            System.out.println(outcome);
        }

        // 5. Wait for the wait-duration-in-open-state to elapse so the breaker can
        //    transition to HALF_OPEN and probe the downstream service again.
        System.out.println();
        System.out.println("---- Waiting for the OPEN wait-duration to elapse... ----");
        Thread.sleep(3200);

        // 6. Phase 3: downstream service has "recovered" - simulate a healthy service now.
        RemoteService recoveredService = new RemoteService("example-service", 0.0, 0.0, 0);
        System.out.println();
        System.out.println("---- Phase 3: trial calls in HALF_OPEN against a recovered service ----");
        for (int i = 16; i <= 19; i++) {
            String outcome = gateway.routeRequest("req-" + i, recoveredService::call);
            System.out.println(outcome);
        }

        System.out.println();
        System.out.println("Final circuit breaker state: " + circuitBreaker.getState());
        System.out.println("===== Resilient API Gateway finished handling requests =====");

        timeLimiter.shutdown();
    }
}

/**
 * PaymentResilienceSystem is the entry point that demonstrates the
 * Circuit Breaker design pattern protecting a PaymentService that
 * depends on a slow, occasionally-failing third-party payment API.
 *
 * Demo flow:
 *   1. The simulated third-party gateway is configured to fail its
 *      first 3 calls (representing an outage), then recover.
 *   2. The Circuit Breaker is configured to trip OPEN after 3
 *      consecutive failures, and to wait 2 seconds before testing
 *      recovery (HALF_OPEN).
 *   3. Six payments are processed back-to-back:
 *        - Payments #1-3 hit the failing gateway -> each failure is
 *          recorded, and the 3rd trips the breaker to OPEN.
 *        - Payment #4 arrives while the breaker is OPEN -> it is
 *          short-circuited straight to the fallback, WITHOUT calling
 *          the slow gateway at all.
 *        - After waiting for the reset timeout, payment #5 is allowed
 *          through as a HALF_OPEN trial call; the gateway has now
 *          recovered, so it succeeds and the breaker CLOSES again.
 *        - Payment #6 flows normally through the CLOSED breaker.
 *   4. A final fallback-event summary report is printed, showing every
 *      fallback that was logged and monitored during the run.
 */
public class PaymentResilienceSystem {

    public static void main(String[] args) throws Exception {
        int failureThreshold = 3;
        long resetTimeoutMillis = 2000;
        int gatewayFailuresBeforeRecovery = 3;

        ThirdPartyPaymentGateway gateway = new ThirdPartyPaymentGateway(gatewayFailuresBeforeRecovery);
        CircuitBreaker circuitBreaker = new CircuitBreaker("PaymentGatewayBreaker", failureThreshold, resetTimeoutMillis);
        FallbackMonitor fallbackMonitor = new FallbackMonitor();
        PaymentService paymentService = new PaymentService(gateway, circuitBreaker, fallbackMonitor);

        System.out.println("================ Resilient Payment Processing Demo ================");
        System.out.println("Failure threshold: " + failureThreshold + " consecutive failures trips the breaker");
        System.out.println("Reset timeout: " + resetTimeoutMillis + " ms before a HALF_OPEN trial is attempted");
        System.out.println("Simulated gateway will fail its first " + gatewayFailuresBeforeRecovery + " calls, then recover.\n");

        // Payments #1-3: gateway is failing, breaker counts failures and trips OPEN on the 3rd
        for (int orderId = 1; orderId <= 3; orderId++) {
            System.out.println(">> Processing payment for order #" + orderId
                    + " (breaker state before call: " + circuitBreaker.getState() + ")");
            System.out.println(paymentService.processPayment(orderId, 100.00 * orderId));
            System.out.println();
        }

        // Payment #4: breaker is OPEN, this call is short-circuited (gateway is never touched)
        System.out.println(">> Processing payment for order #4 (breaker state before call: "
                + circuitBreaker.getState() + ")");
        System.out.println(paymentService.processPayment(4, 400.00));
        System.out.println();

        System.out.println("Waiting for the reset timeout (" + resetTimeoutMillis + " ms) to elapse...\n");
        Thread.sleep(resetTimeoutMillis + 200);

        // Payment #5: breaker allows a HALF_OPEN trial call; gateway has recovered, so it succeeds
        System.out.println(">> Processing payment for order #5 (breaker state before call: "
                + circuitBreaker.getState() + ")");
        System.out.println(paymentService.processPayment(5, 500.00));
        System.out.println();

        // Payment #6: breaker is back to CLOSED, normal operation resumes
        System.out.println(">> Processing payment for order #6 (breaker state before call: "
                + circuitBreaker.getState() + ")");
        System.out.println(paymentService.processPayment(6, 600.00));

        fallbackMonitor.printSummary();

        System.out.println("\nFinal circuit breaker state: " + paymentService.getCircuitState());
        System.out.println("================ DEMO COMPLETE ================");
    }
}

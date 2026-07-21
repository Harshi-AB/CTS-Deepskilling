/**
 * ApiGatewaySystem is the entry point that demonstrates an API Gateway
 * (Spring Cloud Gateway stand-in) sitting in front of two backend
 * microservices - CustomerService and BillingService.
 *
 * Running this class will:
 *   1. Start CustomerService and BillingService on their own ports.
 *   2. Start the ApiGateway, which is the ONLY endpoint a client talks to.
 *   3. Demonstrate PATH REWRITING: calling /gateway/customers/1 on the
 *      gateway transparently reaches CustomerService's /api/customers/1.
 *   4. Demonstrate CACHING: the same GET request made twice in a row
 *      shows the second call being served from the ResponseCache.
 *   5. Demonstrate RATE LIMITING: firing a burst of requests quickly
 *      shows later requests being rejected with HTTP 429 once the
 *      client's token bucket is exhausted.
 */
public class ApiGatewaySystem {

    public static void main(String[] args) throws Exception {
        int customerServicePort = 7001;
        int billingServicePort = 7002;
        int gatewayPort = 8080;

        String customerServiceBaseUrl = "http://localhost:" + customerServicePort;
        String billingServiceBaseUrl = "http://localhost:" + billingServicePort;

        System.out.println("================ Starting Backend Services ================\n");

        CustomerService customerService = new CustomerService(customerServicePort);
        customerService.seedData();
        customerService.start();

        BillingService billingService = new BillingService(billingServicePort);
        billingService.seedData();
        billingService.start();

        System.out.println("\n================ Starting API Gateway ================\n");
        ApiGateway gateway = new ApiGateway(gatewayPort, customerServiceBaseUrl, billingServiceBaseUrl);
        gateway.start();

        Thread.sleep(500);

        String gatewayBaseUrl = "http://localhost:" + gatewayPort;

        System.out.println("\n================ DEMO 1: Path Rewriting & Routing ================\n");
        System.out.println(">> GET /gateway/customers  (rewritten internally to /api/customers)");
        System.out.println(HttpClientHelper.get(gatewayBaseUrl + "/gateway/customers"));

        System.out.println("\n>> GET /gateway/billing/2  (rewritten internally to /api/billing/2)");
        System.out.println(HttpClientHelper.get(gatewayBaseUrl + "/gateway/billing/2"));

        System.out.println("\n================ DEMO 2: Response Caching ================\n");
        System.out.println(">> GET /gateway/customers/1  (first call -> cache MISS, hits CustomerService)");
        System.out.println(HttpClientHelper.get(gatewayBaseUrl + "/gateway/customers/1"));

        System.out.println("\n>> GET /gateway/customers/1  (second call, within TTL -> cache HIT)");
        System.out.println(HttpClientHelper.get(gatewayBaseUrl + "/gateway/customers/1"));

        System.out.println("\n================ DEMO 3: Rate Limiting ================\n");
        System.out.println("Bucket capacity = 3 tokens, refill = 1 token/sec.");
        System.out.println("Firing 5 rapid requests to /gateway/billing/1 ...\n");
        for (int i = 1; i <= 5; i++) {
            String result = HttpClientHelper.get(gatewayBaseUrl + "/gateway/billing/1");
            System.out.println("Request #" + i + " -> " + result);
        }

        System.out.println("\nWaiting 2 seconds for the token bucket to partially refill...");
        Thread.sleep(2000);
        System.out.println(">> GET /gateway/billing/1 (after waiting, should be allowed again)");
        System.out.println(HttpClientHelper.get(gatewayBaseUrl + "/gateway/billing/1"));

        System.out.println("\n================ DEMO COMPLETE ================");

        Thread.sleep(500);
        customerService.stop();
        billingService.stop();
        gateway.stop();
        System.out.println("\nAll services and the gateway have been stopped.");
    }
}

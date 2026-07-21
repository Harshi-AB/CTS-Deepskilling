/**
 * UserOrderManagementSystem is the entry point that demonstrates two
 * independent microservices - UserService and OrderService - talking to
 * one another over REST, exactly as they would in a Spring Boot
 * deployment (there, communication would use WebClient or OpenFeign;
 * here it uses the equivalent HttpClientHelper built on core Java).
 *
 * Running this class will:
 *   1. Start the User Service microservice on port 8081.
 *   2. Start the Order Service microservice on port 8082.
 *   3. Seed both services with sample data.
 *   4. Act as a REST client, issuing GET/POST requests against both
 *      services to prove end-to-end functionality, including the
 *      Order Service reaching out to the User Service to enrich an
 *      order with the placing user's details.
 */
public class UserOrderManagementSystem {

    public static void main(String[] args) throws Exception {
        int userServicePort = 8081;
        int orderServicePort = 8082;
        String userServiceBaseUrl = "http://localhost:" + userServicePort;

        // 1. Start the User Service microservice
        UserService userService = new UserService(userServicePort);
        userService.seedData();
        userService.start();

        // 2. Start the Order Service microservice (knows how to reach User Service)
        OrderService orderService = new OrderService(orderServicePort, userServiceBaseUrl);
        orderService.seedData();
        orderService.start();

        // Give the HTTP servers a brief moment to be fully ready
        Thread.sleep(500);

        System.out.println("\n================ DEMO: REST client calls ================\n");

        // 3. GET all users
        System.out.println(">> GET /users");
        System.out.println(HttpClientHelper.get(userServiceBaseUrl + "/users"));

        // 4. GET a single user
        System.out.println("\n>> GET /users/1");
        System.out.println(HttpClientHelper.get(userServiceBaseUrl + "/users/1"));

        // 5. POST a new user
        System.out.println("\n>> POST /users  (create Charlie Davis)");
        String newUserJson = "{\"name\":\"Charlie Davis\",\"email\":\"charlie@example.com\"}";
        System.out.println(HttpClientHelper.post(userServiceBaseUrl + "/users", newUserJson));

        String orderServiceBaseUrl = "http://localhost:" + orderServicePort;

        // 6. GET all orders
        System.out.println("\n>> GET /orders");
        System.out.println(HttpClientHelper.get(orderServiceBaseUrl + "/orders"));

        // 7. GET a single order -> triggers Order Service to call User Service
        System.out.println("\n>> GET /orders/1 (Order Service internally calls User Service)");
        System.out.println(HttpClientHelper.get(orderServiceBaseUrl + "/orders/1"));

        // 8. POST a new order for the newly created user (id 3)
        System.out.println("\n>> POST /orders (Charlie orders a Monitor)");
        String newOrderJson = "{\"userId\":3,\"product\":\"4K Monitor\",\"quantity\":1}";
        System.out.println(HttpClientHelper.post(orderServiceBaseUrl + "/orders", newOrderJson));

        // 9. GET the new order enriched with Charlie's user details
        System.out.println("\n>> GET /orders/3 (should show Charlie's user details embedded)");
        System.out.println(HttpClientHelper.get(orderServiceBaseUrl + "/orders/3"));

        System.out.println("\n================ DEMO COMPLETE ================");
        System.out.println("Servers are still running. Press Ctrl+C to stop, or they");
        System.out.println("will be stopped automatically when this JVM process exits.");

        // Keep the servers alive briefly so a user could also test manually
        // with curl/Postman, then shut down cleanly.
        Thread.sleep(2000);
        userService.stop();
        orderService.stop();
        System.out.println("\nBoth microservices have been stopped.");
    }
}

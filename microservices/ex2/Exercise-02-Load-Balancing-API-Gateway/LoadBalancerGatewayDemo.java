import java.util.List;

/**
 * LoadBalancerGatewayDemo
 * ------------------------
 * Plain-Java re-creation of "Exercise 2: Load Balancing in an API Gateway"
 * (originally built with Spring Boot 3 + Spring Cloud Gateway +
 * Spring Cloud LoadBalancer).
 *
 * What used to live in application.properties:
 *
 *   spring.cloud.gateway.routes[0].id=load_balanced_route
 *   spring.cloud.gateway.routes[0].uri=lb://example-service
 *   spring.cloud.gateway.routes[0].predicates[0]=Path=/loadbalanced/**
 *
 * is now configured on the ApiGatewayRoute object below, and the
 * @Bean randomLoadBalancer(...) is now a plain RandomLoadBalancer
 * instance handed to the gateway.
 *
 * Run this class to see several requests to "/loadbalanced/**" being
 * routed to different backend instances of "example-service".
 */
public class LoadBalancerGatewayDemo {

    public static void main(String[] args) {

        // 1. Service discovery: register 3 running instances of "example-service".
        ServiceRegistry registry = new ServiceRegistry();
        registry.register(new ServiceInstance("example-service", "10.0.0.11", 8081));
        registry.register(new ServiceInstance("example-service", "10.0.0.12", 8082));
        registry.register(new ServiceInstance("example-service", "10.0.0.13", 8083));

        // 2. Configure the load-balanced route, equivalent to application.properties.
        ApiGatewayRoute route = new ApiGatewayRoute(
                "load_balanced_route", "lb://example-service", "/loadbalanced/**");

        // 3. Choose the load balancing strategy (equivalent to the @Bean definition).
        LoadBalancer loadBalancer = new RandomLoadBalancer();

        System.out.println("===== API Gateway (with Load Balancing) started =====");
        System.out.println("Registered instances for 'example-service':");
        for (ServiceInstance instance : registry.getInstances("example-service")) {
            System.out.println("   - " + instance);
        }
        System.out.println();

        // 4. Simulate several incoming requests hitting the load-balanced route.
        String[] incomingRequests = {
                "/loadbalanced/orders",
                "/loadbalanced/orders",
                "/loadbalanced/products",
                "/loadbalanced/products",
                "/loadbalanced/cart"
        };

        for (String requestUri : incomingRequests) {
            handleRequest(requestUri, route, registry, loadBalancer);
        }

        System.out.println();
        System.out.println("---- Now switching strategy to RoundRobinLoadBalancer ----");
        LoadBalancer roundRobin = new RoundRobinLoadBalancer();
        for (String requestUri : incomingRequests) {
            handleRequest(requestUri, route, registry, roundRobin);
        }

        System.out.println();
        System.out.println("===== API Gateway finished handling requests =====");
    }

    /**
     * Simulates the gateway handling one request: match the route,
     * resolve "lb://" to a concrete instance via the LoadBalancer,
     * then forward.
     */
    private static void handleRequest(String requestUri,
                                       ApiGatewayRoute route,
                                       ServiceRegistry registry,
                                       LoadBalancer loadBalancer) {
        if (!route.matches(requestUri)) {
            System.out.println("[Gateway] 404 - No route matched for: " + requestUri);
            return;
        }

        if (route.isLoadBalanced()) {
            String serviceId = route.getServiceId();
            List<ServiceInstance> instances = registry.getInstances(serviceId);
            ServiceInstance chosen = loadBalancer.choose(instances);
            System.out.println("[Gateway] " + requestUri + " -> " + chosen
                    + "  (via " + loadBalancer.getClass().getSimpleName() + ")");
        } else {
            System.out.println("[Gateway] " + requestUri + " -> " + route.getUri());
        }
    }
}

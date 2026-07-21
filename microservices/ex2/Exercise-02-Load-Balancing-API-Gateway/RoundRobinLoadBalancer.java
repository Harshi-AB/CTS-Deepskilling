import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * RoundRobinLoadBalancer is an additional LoadBalancer strategy
 * (the counterpart of Spring Cloud LoadBalancer's built-in
 * RoundRobinLoadBalancer). It is included to demonstrate that the
 * Router/Gateway can work against the LoadBalancer interface and swap
 * strategies without any other code changing - a textbook example of
 * the Strategy design pattern.
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    private final AtomicInteger position = new AtomicInteger(0);

    @Override
    public ServiceInstance choose(List<ServiceInstance> instances) {
        if (instances == null || instances.isEmpty()) {
            throw new IllegalStateException("No available service instances to choose from.");
        }
        int index = Math.abs(position.getAndIncrement() % instances.size());
        return instances.get(index);
    }
}

import java.util.List;

/**
 * LoadBalancer is the plain-Java equivalent of Spring Cloud LoadBalancer's
 * ReactorLoadBalancer<ServiceInstance> interface. Concrete strategies
 * (Random, RoundRobin, ...) implement the "choose" method, which is the
 * Strategy design pattern applied to instance selection.
 */
public interface LoadBalancer {

    /**
     * Picks one instance out of the currently available instances for a
     * service.
     *
     * @param instances the list of currently healthy/registered instances
     * @return the chosen ServiceInstance
     */
    ServiceInstance choose(List<ServiceInstance> instances);
}

import java.util.List;
import java.util.Random;

/**
 * RandomLoadBalancer is the plain-Java equivalent of the Spring Cloud
 * @Bean shown in the exercise:
 *
 *   @Bean
 *   public ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(
 *           Environment environment,
 *           LoadBalancerClientFactory loadBalancerClientFactory) {
 *       String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
 *       return new RandomLoadBalancer(
 *               loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class),
 *               name);
 *   }
 *
 * It simply picks one of the available instances uniformly at random,
 * simulating RandomLoadBalancer's selection algorithm.
 */
public class RandomLoadBalancer implements LoadBalancer {

    private final Random random = new Random();

    @Override
    public ServiceInstance choose(List<ServiceInstance> instances) {
        if (instances == null || instances.isEmpty()) {
            throw new IllegalStateException("No available service instances to choose from.");
        }
        int index = random.nextInt(instances.size());
        return instances.get(index);
    }
}

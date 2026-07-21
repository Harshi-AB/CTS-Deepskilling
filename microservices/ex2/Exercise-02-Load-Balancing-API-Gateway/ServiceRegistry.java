import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ServiceRegistry is a tiny plain-Java stand-in for the service
 * discovery layer (e.g. Eureka) that a real ServiceInstanceListSupplier
 * would normally pull instances from. It simply keeps a Map of
 * serviceId -> list of registered ServiceInstance objects.
 */
public class ServiceRegistry {

    private final Map<String, List<ServiceInstance>> instancesByService = new HashMap<>();

    public void register(ServiceInstance instance) {
        instancesByService
                .computeIfAbsent(instance.getServiceId(), key -> new ArrayList<>())
                .add(instance);
    }

    public List<ServiceInstance> getInstances(String serviceId) {
        return instancesByService.getOrDefault(serviceId, new ArrayList<>());
    }
}

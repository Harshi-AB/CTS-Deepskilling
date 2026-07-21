/**
 * ServiceInstance is the plain-Java equivalent of Spring Cloud's
 * org.springframework.cloud.client.ServiceInstance.
 *
 * It represents one running, discoverable copy of a backend
 * microservice (host + port) that requests can be load balanced to.
 */
public class ServiceInstance {

    private final String serviceId;
    private final String host;
    private final int port;

    public ServiceInstance(String serviceId, String host, int port) {
        this.serviceId = serviceId;
        this.host = host;
        this.port = port;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUri() {
        return "http://" + host + ":" + port;
    }

    @Override
    public String toString() {
        return getUri() + " (serviceId='" + serviceId + "')";
    }
}

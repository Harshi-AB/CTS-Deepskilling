import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * RegistryClient
 * ----------------------------------------------------------------------
 * A small, reusable helper class used by every microservice to REGISTER
 * itself with the DiscoveryServer on startup, and DE-REGISTER on shutdown.
 *
 * This plays the role that @EnableDiscoveryClient + the Eureka client
 * library plays in the real Spring Cloud exercise, but implemented with
 * plain java.net.HttpURLConnection since no external JARs are allowed.
 * ----------------------------------------------------------------------
 */
public class RegistryClient {

    private static final String DISCOVERY_BASE_URL = "http://localhost:8761";

    /**
     * Registers a service instance with the Discovery Server.
     * Equivalent to a microservice appearing under
     * "Instances currently registered with Eureka" on the dashboard.
     *
     * @param serviceName logical service name, e.g. "account-service"
     * @param selfUrl     base URL of this instance, e.g. "http://localhost:8080"
     */
    public static void register(String serviceName, String selfUrl) {
        try {
            String endpoint = DISCOVERY_BASE_URL + "/eureka/apps/register"
                    + "?service=" + encode(serviceName)
                    + "&url=" + encode(selfUrl);
            callPost(endpoint);
            System.out.println("[RegistryClient] Registered '" + serviceName + "' with Discovery Server.");
        } catch (IOException e) {
            System.out.println("[RegistryClient] WARNING: Could not reach Discovery Server. "
                    + "Is DiscoveryServer running on port 8761? (" + e.getMessage() + ")");
        }
    }

    /** De-registers a service instance from the Discovery Server (used on graceful shutdown). */
    public static void deregister(String serviceName, String selfUrl) {
        try {
            String endpoint = DISCOVERY_BASE_URL + "/eureka/apps/deregister"
                    + "?service=" + encode(serviceName)
                    + "&url=" + encode(selfUrl);
            callPost(endpoint);
            System.out.println("[RegistryClient] Deregistered '" + serviceName + "'.");
        } catch (IOException e) {
            System.out.println("[RegistryClient] WARNING: Could not reach Discovery Server to deregister.");
        }
    }

    private static void callPost(String endpoint) throws IOException {
        URL url = URI.create(endpoint).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setConnectTimeout(3000);
        connection.setReadTimeout(3000);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(new byte[0]); // no request body needed, params are in the query string
        }

        int status = connection.getResponseCode();
        if (status != 200) {
            throw new IOException("Discovery Server responded with status " + status);
        }
        connection.disconnect();
    }

    private static String encode(String value) {
        return java.net.URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}

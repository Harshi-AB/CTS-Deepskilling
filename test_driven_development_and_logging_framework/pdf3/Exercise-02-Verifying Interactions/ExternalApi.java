/**
 * ExternalApi represents a third-party / external service dependency.
 */
public interface ExternalApi {

    /**
     * Fetches data from the external system.
     *
     * @return the data returned by the external system
     */
    String getData();
}

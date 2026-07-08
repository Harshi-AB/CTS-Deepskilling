/**
 * ExternalApi represents a third-party / external service dependency.
 */
public interface ExternalApi {

    /**
     * Fetches data from the external system. In this exercise, consecutive
     * calls to this method are expected to return different values.
     *
     * @return the data returned by the external system
     */
    String getData();
}

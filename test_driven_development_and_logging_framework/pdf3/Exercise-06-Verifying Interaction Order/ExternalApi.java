/**
 * ExternalApi represents a third-party / external service dependency.
 * This exercise requires two methods so that a specific CALL ORDER
 * between them can be verified (connect() must happen before getData()).
 */
public interface ExternalApi {

    /**
     * Opens a connection to the external system. Must be called before getData().
     */
    void connect();

    /**
     * Fetches data from the external system.
     *
     * @return the data returned by the external system
     */
    String getData();
}

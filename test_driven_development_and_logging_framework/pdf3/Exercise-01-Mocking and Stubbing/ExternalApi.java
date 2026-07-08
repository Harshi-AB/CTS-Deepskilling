/**
 * ExternalApi represents a third-party / external service dependency.
 * In a real application this could call a REST endpoint, a database, etc.
 * We depend on this abstraction (interface) rather than a concrete class
 * so that it can be mocked during testing.
 */
public interface ExternalApi {

    /**
     * Fetches data from the external system.
     *
     * @return the data returned by the external system
     */
    String getData();
}

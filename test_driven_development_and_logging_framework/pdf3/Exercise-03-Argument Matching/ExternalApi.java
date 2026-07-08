/**
 * ExternalApi represents a third-party / external service dependency.
 * getDataById() takes an argument, which is what allows us to
 * demonstrate argument matching in this exercise.
 */
public interface ExternalApi {

    /**
     * Fetches data associated with a specific id.
     *
     * @param id the identifier to fetch data for
     * @return the data returned by the external system
     */
    String getDataById(String id);
}

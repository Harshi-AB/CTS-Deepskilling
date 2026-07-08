/**
 * ExternalApi represents a third-party / external service dependency.
 * deleteData() is a void method that may fail and throw an exception,
 * which is what this exercise focuses on.
 */
public interface ExternalApi {

    /**
     * Deletes data associated with the given id. May throw a
     * RuntimeException if the deletion fails.
     *
     * @param id the identifier of the data to delete
     */
    void deleteData(String id);
}

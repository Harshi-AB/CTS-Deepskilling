/**
 * MyService is the "system under test". It depends on ExternalApi,
 * injected via the constructor.
 */
public class MyService {

    private final ExternalApi externalApi;

    public MyService(ExternalApi externalApi) {
        this.externalApi = externalApi;
    }

    /**
     * Business logic that deletes data through the ExternalApi dependency.
     * If the underlying deleteData() call fails, the exception propagates
     * to the caller.
     *
     * @param id the identifier of the data to delete
     */
    public void deleteData(String id) {
        externalApi.deleteData(id);
    }
}

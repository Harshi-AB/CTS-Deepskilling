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
     * Business logic that must connect to the external system BEFORE
     * fetching data from it. This ordering is what the test verifies.
     *
     * @return the data obtained from the ExternalApi
     */
    public String initializeAndFetchData() {
        externalApi.connect();
        return externalApi.getData();
    }
}

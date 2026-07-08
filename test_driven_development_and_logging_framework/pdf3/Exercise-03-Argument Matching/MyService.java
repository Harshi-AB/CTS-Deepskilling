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
     * Business logic that fetches data for a given id through the
     * ExternalApi dependency.
     *
     * @param id the identifier to fetch data for
     * @return the data obtained from the ExternalApi
     */
    public String fetchDataById(String id) {
        return externalApi.getDataById(id);
    }
}

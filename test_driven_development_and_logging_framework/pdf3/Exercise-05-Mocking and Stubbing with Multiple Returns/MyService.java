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
     * Business logic that fetches data through the ExternalApi dependency.
     * Each call delegates directly to the dependency's getData() method.
     *
     * @return the data obtained from the ExternalApi
     */
    public String fetchData() {
        return externalApi.getData();
    }
}

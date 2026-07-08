/**
 * MyService is the "system under test". It depends on ExternalApi
 * (constructor injection), so any implementation of ExternalApi -
 * real or mock - can be supplied to it.
 */
public class MyService {

    private final ExternalApi externalApi;

    /**
     * Dependency is injected through the constructor, which is what allows
     * a mock implementation to be substituted for the real one in tests.
     *
     * @param externalApi the ExternalApi dependency (real or mock)
     */
    public MyService(ExternalApi externalApi) {
        this.externalApi = externalApi;
    }

    /**
     * Business logic that fetches data through the ExternalApi dependency.
     *
     * @return the data obtained from the ExternalApi
     */
    public String fetchData() {
        return externalApi.getData();
    }
}

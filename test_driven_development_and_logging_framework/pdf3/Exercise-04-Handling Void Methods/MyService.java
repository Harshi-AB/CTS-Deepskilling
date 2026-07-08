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
     * Business logic that performs an action (saving data) through the
     * ExternalApi dependency's void method.
     *
     * @param data the data to save
     */
    public void save(String data) {
        externalApi.saveData(data);
    }
}

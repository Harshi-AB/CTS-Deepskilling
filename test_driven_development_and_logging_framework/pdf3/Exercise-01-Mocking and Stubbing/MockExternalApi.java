/**
 * MockExternalApi is a hand-written "mock" implementation of ExternalApi.
 *
 * It replicates what Mockito's Mockito.mock(ExternalApi.class) + when()/thenReturn()
 * would do, but using pure Core Java (no external libraries), since the project
 * must compile with plain "javac *.java".
 *
 * This class implements the classic "Mock Object" design pattern:
 *  - It implements the same interface as the real dependency.
 *  - Its behavior (return values) can be programmed ("stubbed") before use.
 *  - It substitutes for the real ExternalApi in tests.
 */
public class MockExternalApi implements ExternalApi {

    // Holds whatever value has been "stubbed" to be returned by getData()
    private String stubbedData;

    /**
     * Equivalent of: when(mockApi.getData()).thenReturn(data);
     * Programs the mock so that any call to getData() returns the given value.
     *
     * @param data the value getData() should return
     */
    public void when_getData_thenReturn(String data) {
        this.stubbedData = data;
    }

    /**
     * Returns whatever value was stubbed via when_getData_thenReturn().
     * This simulates Mockito's stubbing behavior.
     */
    @Override
    public String getData() {
        return stubbedData;
    }
}

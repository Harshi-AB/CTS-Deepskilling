/**
 * MockExternalApi is a hand-written mock implementation of ExternalApi.
 *
 * Since saveData() is a void method, Mockito would normally use
 * doNothing().when(mockApi).saveData(...) (its default behavior for void
 * methods anyway) and later verify(mockApi).saveData(data).
 *
 * Here we replicate that: the mock simply records the call and its
 * argument ("does nothing" beyond that), and exposes a verify method.
 */
public class MockExternalApi implements ExternalApi {

    private boolean saveDataCalled = false;
    private String lastSavedData;
    private int saveDataCallCount = 0;

    @Override
    public void saveData(String data) {
        // doNothing() behavior: simply record that the call happened.
        saveDataCalled = true;
        saveDataCallCount++;
        lastSavedData = data;
    }

    /**
     * Equivalent of Mockito's verify(mockApi).saveData(data);
     * Verifies saveData() was called exactly once, with the given argument.
     *
     * @param expectedData the argument saveData() was expected to be called with
     * @throws AssertionError if the verification fails
     */
    public void verifySaveDataCalledWith(String expectedData) {
        if (!saveDataCalled) {
            throw new AssertionError("Expected saveData() to be called, but it was never called.");
        }
        if (saveDataCallCount != 1) {
            throw new AssertionError("Expected saveData() to be called exactly once, but was called "
                    + saveDataCallCount + " time(s).");
        }
        if (!expectedData.equals(lastSavedData)) {
            throw new AssertionError("Expected saveData() to be called with '" + expectedData
                    + "' but was called with '" + lastSavedData + "'.");
        }
    }
}

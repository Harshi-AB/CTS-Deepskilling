/**
 * MockExternalApi is a hand-written mock implementation of ExternalApi.
 *
 * In addition to stubbing (as in Exercise 1), this mock RECORDS every
 * invocation of getData() so that the test can later verify how many
 * times the method was actually called - replicating Mockito's
 * verify(mockApi).getData() behavior, using pure Core Java.
 */
public class MockExternalApi implements ExternalApi {

    private String stubbedData;

    // Tracks how many times getData() has been invoked.
    private int getDataCallCount = 0;

    /**
     * Stubs getData() to return the given value.
     *
     * @param data the value getData() should return
     */
    public void when_getData_thenReturn(String data) {
        this.stubbedData = data;
    }

    @Override
    public String getData() {
        getDataCallCount++;
        return stubbedData;
    }

    /**
     * Equivalent of Mockito's verify(mockApi).getData()
     * (i.e. verify the method was called exactly once).
     *
     * @throws AssertionError if getData() was not called exactly once
     */
    public void verifyGetDataCalledOnce() {
        verifyGetDataCalled(1);
    }

    /**
     * Equivalent of Mockito's verify(mockApi, times(n)).getData()
     *
     * @param expectedTimes the exact number of times getData() should have been called
     * @throws AssertionError if the actual call count does not match expectedTimes
     */
    public void verifyGetDataCalled(int expectedTimes) {
        if (getDataCallCount != expectedTimes) {
            throw new AssertionError("Expected getData() to be called " + expectedTimes
                    + " time(s) but it was called " + getDataCallCount + " time(s).");
        }
    }
}

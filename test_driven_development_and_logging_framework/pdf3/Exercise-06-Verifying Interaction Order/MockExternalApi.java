/**
 * MockExternalApi is a hand-written mock implementation of ExternalApi
 * that records the ORDER in which its methods are invoked, replicating
 * Mockito's InOrder verification:
 *
 *      InOrder inOrder = inOrder(mockApi);
 *      inOrder.verify(mockApi).connect();
 *      inOrder.verify(mockApi).getData();
 */
public class MockExternalApi implements ExternalApi {

    // Records the name of every method called, in the order they were called.
    private final java.util.List<String> callOrder = new java.util.ArrayList<>();

    private String stubbedData = "Mock Data";

    /**
     * Stubs getData() to return the given value.
     */
    public void when_getData_thenReturn(String data) {
        this.stubbedData = data;
    }

    @Override
    public void connect() {
        callOrder.add("connect");
    }

    @Override
    public String getData() {
        callOrder.add("getData");
        return stubbedData;
    }

    /**
     * Equivalent of Mockito's InOrder verification.
     * Verifies that the given method names occurred, in the given order,
     * within the recorded call history (subsequence match).
     *
     * @param expectedOrder the method names expected to occur in this order
     * @throws AssertionError if the recorded calls do not respect the expected order
     */
    public void verifyOrder(String... expectedOrder) {
        int searchStartIndex = 0;

        for (String expectedCall : expectedOrder) {
            int foundIndex = -1;

            // Manual search from searchStartIndex onward, since List has no
            // indexOf(Object, fromIndex) overload like String does.
            for (int i = searchStartIndex; i < callOrder.size(); i++) {
                if (callOrder.get(i).equals(expectedCall)) {
                    foundIndex = i;
                    break;
                }
            }

            if (foundIndex == -1) {
                throw new AssertionError("Expected call order " + java.util.Arrays.toString(expectedOrder)
                        + " but actual recorded call order was " + callOrder
                        + " (could not find '" + expectedCall + "' after position " + searchStartIndex + ")");
            }
            searchStartIndex = foundIndex + 1;
        }
    }
}

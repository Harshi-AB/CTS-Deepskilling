/**
 * MockExternalApi is a hand-written mock implementation of ExternalApi
 * that supports stubbing DIFFERENT return values on CONSECUTIVE calls,
 * replicating Mockito's:
 *
 *      when(mockApi.getData())
 *          .thenReturn("First Call")
 *          .thenReturn("Second Call")
 *          .thenReturn("Third Call");
 *
 * Just like Mockito, once the stubbed values are exhausted, the LAST
 * stubbed value continues to be returned on every subsequent call.
 */
public class MockExternalApi implements ExternalApi {

    private String[] stubbedValues;
    private int callIndex = 0;

    /**
     * Stubs getData() to return each of the given values in order on
     * successive calls. After the last value is returned once, it keeps
     * being returned on every further call (matching real Mockito behavior).
     *
     * @param values the values to return on successive calls, in order
     */
    public void when_getData_thenReturnConsecutively(String... values) {
        this.stubbedValues = values;
        this.callIndex = 0;
    }

    @Override
    public String getData() {
        if (stubbedValues == null || stubbedValues.length == 0) {
            return null;
        }

        String result;
        if (callIndex < stubbedValues.length) {
            result = stubbedValues[callIndex];
        } else {
            // Mockito repeats the last stubbed value once all are exhausted.
            result = stubbedValues[stubbedValues.length - 1];
        }

        callIndex++;
        return result;
    }
}

/**
 * MockExternalApi is a hand-written mock implementation of ExternalApi
 * that supports argument-matcher based stubbing, replicating Mockito's:
 *
 *      when(mockApi.getDataById(eq("123"))).thenReturn("Data for 123");
 *      when(mockApi.getDataById(anyString())).thenReturn("Default Data");
 *
 * It also records the arguments it was called with, so a test can
 * verify the mock was invoked with the expected argument(s), similar to:
 *
 *      verify(mockApi).getDataById(eq("123"));
 */
public class MockExternalApi implements ExternalApi {

    // The matcher that decides which calls this stub applies to.
    private ArgumentMatcher stubbedMatcher;
    private String stubbedData;

    // Records every argument this mock was called with, in call order.
    private final java.util.List<String> receivedArguments = new java.util.ArrayList<>();

    /**
     * Stubs getDataById(argument) to return the given data whenever the
     * supplied matcher accepts the argument.
     *
     * @param matcher the argument matcher (e.g. ArgumentMatcher.eq("123"))
     * @param data    the value to return when the matcher accepts the argument
     */
    public void when_getDataById_matching_thenReturn(ArgumentMatcher matcher, String data) {
        this.stubbedMatcher = matcher;
        this.stubbedData = data;
    }

    @Override
    public String getDataById(String id) {
        receivedArguments.add(id);
        if (stubbedMatcher != null && stubbedMatcher.matches(id)) {
            return stubbedData;
        }
        return null;
    }

    /**
     * Equivalent of Mockito's verify(mockApi).getDataById(eq(expectedArgument));
     * Verifies that the mock was called at least once with an argument
     * satisfying the given matcher.
     *
     * @param matcher the matcher the argument must satisfy
     * @throws AssertionError if no recorded call matches
     */
    public void verifyCalledWith(ArgumentMatcher matcher) {
        for (String argument : receivedArguments) {
            if (matcher.matches(argument)) {
                return; // Found a matching invocation - verification succeeds.
            }
        }
        throw new AssertionError("No invocation of getDataById() matched the expected argument matcher. "
                + "Recorded arguments: " + receivedArguments);
    }
}

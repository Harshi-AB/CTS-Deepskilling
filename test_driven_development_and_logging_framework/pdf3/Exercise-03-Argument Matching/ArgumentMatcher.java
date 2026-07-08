/**
 * ArgumentMatcher is a small functional interface that mimics Mockito's
 * argument matchers (e.g. eq("value"), anyString(), any()).
 *
 * A matcher decides whether a given argument "matches" some condition.
 */
@FunctionalInterface
public interface ArgumentMatcher {

    /**
     * @param argument the argument passed to the mocked method call
     * @return true if the argument satisfies this matcher's condition
     */
    boolean matches(String argument);

    /**
     * Equivalent of Mockito's eq(value) matcher: matches only the exact value.
     */
    static ArgumentMatcher eq(String expectedValue) {
        return argument -> argument != null && argument.equals(expectedValue);
    }

    /**
     * Equivalent of Mockito's anyString() / any() matcher: matches anything.
     */
    static ArgumentMatcher any() {
        return argument -> true;
    }

    /**
     * Equivalent of Mockito's startsWith()-style custom matcher.
     */
    static ArgumentMatcher startsWith(String prefix) {
        return argument -> argument != null && argument.startsWith(prefix);
    }
}

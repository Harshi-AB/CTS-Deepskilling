/**
 * Minimal hand-written assertion library (stands in for
 * org.junit.jupiter.api.Assertions) used across the custom test engine.
 */
public class Assertions {

    /**
     * Asserts that the given condition is true.
     *
     * @param condition the boolean condition under test
     * @param message   the failure message if the assertion fails
     */
    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    /**
     * Asserts that two objects are equal.
     *
     * @param expected the expected value
     * @param actual   the actual value
     * @param message  the failure message if the assertion fails
     */
    public static void assertEquals(Object expected, Object actual, String message) {
        boolean equal = (expected == null) ? (actual == null) : expected.equals(actual);
        if (!equal) {
            throw new AssertionError(message + " -> expected: <" + expected + "> but was: <" + actual + ">");
        }
    }
}

/**
 * Minimal hand-written assertion library (stands in for
 * org.junit.jupiter.api.Assertions) used across the custom test engine.
 */
public class Assertions {

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static void assertEquals(Object expected, Object actual, String message) {
        boolean equal = (expected == null) ? (actual == null) : expected.equals(actual);
        if (!equal) {
            throw new AssertionError(message + " -> expected: <" + expected + "> but was: <" + actual + ">");
        }
    }
}

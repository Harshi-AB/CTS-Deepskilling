import java.util.Arrays;

/**
 * A minimal, self-contained re-implementation of the assertion utilities
 * normally provided by org.junit.Assert. Built with core Java only since
 * no Maven/Gradle build is used and the real JUnit jar is unavailable.
 *
 * Exercise 3 focuses specifically on exercising a variety of these
 * assertions, so this class intentionally includes more than the bare
 * minimum (assertEquals, assertNotEquals, assertTrue, assertFalse,
 * assertNull, assertNotNull, assertArrayEquals).
 */
public class Assert {

    /** Asserts that two objects are equal. */
    public static void assertEquals(Object expected, Object actual) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError("Expected <" + expected + "> but was <" + actual + ">");
        }
    }

    /** Asserts that two objects are NOT equal. */
    public static void assertNotEquals(Object unexpected, Object actual) {
        if (unexpected == null ? actual == null : unexpected.equals(actual)) {
            throw new AssertionError("Expected value to differ from <" + unexpected + ">");
        }
    }

    /** Asserts that a condition is true. */
    public static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected condition to be true but was false");
        }
    }

    /** Asserts that a condition is false. */
    public static void assertFalse(boolean condition) {
        if (condition) {
            throw new AssertionError("Expected condition to be false but was true");
        }
    }

    /** Asserts that an object is null. */
    public static void assertNull(Object obj) {
        if (obj != null) {
            throw new AssertionError("Expected null but was <" + obj + ">");
        }
    }

    /** Asserts that an object is not null. */
    public static void assertNotNull(Object obj) {
        if (obj == null) {
            throw new AssertionError("Expected a non-null value but got null");
        }
    }

    /** Asserts that two int arrays are equal element-by-element. */
    public static void assertArrayEquals(int[] expected, int[] actual) {
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError("Expected <" + Arrays.toString(expected)
                    + "> but was <" + Arrays.toString(actual) + ">");
        }
    }
}

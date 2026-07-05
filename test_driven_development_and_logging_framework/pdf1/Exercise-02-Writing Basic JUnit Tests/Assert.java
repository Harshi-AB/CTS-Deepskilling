/**
 * A minimal, self-contained re-implementation of the assertion utilities
 * normally provided by org.junit.Assert (assertEquals, assertTrue, etc).
 *
 * This class is required because the project has no Maven/Gradle build
 * and therefore cannot pull in the real JUnit library jar. Every method
 * throws an AssertionError on failure, exactly like the real JUnit
 * assertions do, so that the TestRunner can catch AssertionError to
 * detect a failing test.
 */
public class Assert {

    /**
     * Asserts that two objects are equal.
     * Uses Object.equals() so it works for primitivesWrapped, Strings, etc.
     */
    public static void assertEquals(Object expected, Object actual) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError("Expected <" + expected + "> but was <" + actual + ">");
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
}

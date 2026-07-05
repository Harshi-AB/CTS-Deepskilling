
public class Assert {

    public static void assertEquals(Object expected, Object actual) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError("Expected <" + expected + "> but was <" + actual + ">");
        }
    }

    public static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected condition to be true but was false");
        }
    }

    public static void assertFalse(boolean condition) {
        if (condition) {
            throw new AssertionError("Expected condition to be false but was true");
        }
    }

    public static void assertNull(Object obj) {
        if (obj != null) {
            throw new AssertionError("Expected null but was <" + obj + ">");
        }
    }

    public static void assertNotNull(Object obj) {
        if (obj == null) {
            throw new AssertionError("Expected a non-null value but got null");
        }
    }
}

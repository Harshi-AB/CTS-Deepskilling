/**
 * Utility class that deliberately throws different exception types
 * depending on the requested scenario, used to exercise exception
 * testing.
 */
public class ExceptionThrower {

    /**
     * Throws a specific exception type based on the given scenario key.
     *
     * @param type the scenario key: "illegal", "null", or "arithmetic"
     */
    public void throwException(String type) {
        if ("illegal".equals(type)) {
            throw new IllegalArgumentException("Illegal argument provided");
        } else if ("null".equals(type)) {
            throw new NullPointerException("Null value encountered");
        } else if ("arithmetic".equals(type)) {
            throw new ArithmeticException("Arithmetic error occurred");
        } else {
            throw new RuntimeException("Unknown error for type: " + type);
        }
    }
}

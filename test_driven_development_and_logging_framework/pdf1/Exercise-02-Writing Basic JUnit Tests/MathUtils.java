/**
 * A simple utility class with several methods.
 * This is the class we will write basic JUnit-style tests for (Exercise 2).
 */
public class MathUtils {

    /** Returns the sum of two integers. */
    public int add(int a, int b) {
        return a + b;
    }

    /** Returns the result of subtracting b from a. */
    public int subtract(int a, int b) {
        return a - b;
    }

    /** Returns the product of two integers. */
    public int multiply(int a, int b) {
        return a * b;
    }

    /**
     * Returns the result of dividing a by b.
     * Throws ArithmeticException if b is zero (handled by Java automatically
     * for integer division).
     */
    public int divide(int a, int b) {
        return a / b;
    }

    /** Returns true if the given number is even. */
    public boolean isEven(int number) {
        return number % 2 == 0;
    }
}

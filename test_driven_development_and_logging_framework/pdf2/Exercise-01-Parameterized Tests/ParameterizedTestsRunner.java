import java.lang.reflect.Method;

/**
 * Custom reflection-based test engine that discovers and executes
 * methods annotated with @ParameterizedTest + @ValueSource, invoking
 * each test once per declared int value (equivalent to JUnit 5's
 * parameterized test runner, built with Core Java only).
 */
public class ParameterizedTestsRunner {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println(" Running Parameterized Tests");
        System.out.println("========================================");

        runTests(EvenCheckerTest.class);

        System.out.println("========================================");
        System.out.println("Total Passed: " + passed + ", Total Failed: " + failed);
        System.out.println("========================================");
    }

    private static void runTests(Class<?> testClass) {
        try {
            Object instance = testClass.getDeclaredConstructor().newInstance();

            for (Method method : testClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(ParameterizedTest.class)
                        && method.isAnnotationPresent(ValueSource.class)) {

                    ValueSource source = method.getAnnotation(ValueSource.class);
                    int[] values = source.ints();

                    for (int value : values) {
                        try {
                            method.invoke(instance, value);
                            System.out.println("[PASS] " + method.getName() + "(" + value + ")");
                            passed++;
                        } catch (Exception e) {
                            Throwable cause = (e.getCause() != null) ? e.getCause() : e;
                            System.out.println("[FAIL] " + method.getName() + "(" + value + ") -> " + cause.getMessage());
                            failed++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error running tests for " + testClass.getName() + ": " + e.getMessage());
        }
    }
}

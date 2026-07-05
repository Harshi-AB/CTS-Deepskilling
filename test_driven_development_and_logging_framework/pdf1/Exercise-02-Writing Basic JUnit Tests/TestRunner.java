import java.lang.reflect.Method;

/**
 * A minimal reflection-based test runner.
 *
 * Responsibility: given a test class, find every public no-argument method
 * annotated with @Test, instantiate the test class, invoke each test
 * method, and report PASS/FAIL for each, followed by a summary.
 *
 * This is a simplified stand-in for the JUnit engine, built using only
 * core Java (java.lang.reflect), since no external JUnit jar is available
 * without Maven/Gradle.
 */
public class TestRunner {

    /**
     * Runs all @Test annotated methods found in the given test class.
     *
     * @param testClass the .class object of the test class to execute
     */
    public static void run(Class<?> testClass) {
        int passCount = 0;
        int failCount = 0;

        System.out.println("Running tests for: " + testClass.getSimpleName());
        System.out.println("--------------------------------------------------");

        try {
            Object testInstance = testClass.getDeclaredConstructor().newInstance();

            for (Method method : testClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Test.class)) {
                    try {
                        method.invoke(testInstance);
                        System.out.println("[PASS] " + method.getName());
                        passCount++;
                    } catch (Exception invocationException) {
                        // Unwrap the real cause (AssertionError is wrapped by reflection)
                        Throwable cause = invocationException.getCause() != null
                                ? invocationException.getCause()
                                : invocationException;
                        System.out.println("[FAIL] " + method.getName() + " -> " + cause.getMessage());
                        failCount++;
                    }
                }
            }
        } catch (Exception setupException) {
            System.out.println("Could not run tests: " + setupException.getMessage());
        }

        System.out.println("--------------------------------------------------");
        System.out.println("Tests run: " + (passCount + failCount)
                + ", Passed: " + passCount + ", Failed: " + failCount);
    }
}

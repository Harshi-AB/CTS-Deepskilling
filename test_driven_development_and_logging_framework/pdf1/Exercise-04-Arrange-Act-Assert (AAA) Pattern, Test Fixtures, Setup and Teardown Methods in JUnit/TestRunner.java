import java.lang.reflect.Method;

/**
 * A reflection-based test runner that additionally understands test
 * fixtures: @Before (setup) and @After (teardown) methods.
 *
 * Execution order for each @Test method found:
 *   1. Run every @Before method (setup / "Arrange" support)
 *   2. Run the @Test method itself (contains Act + Assert)
 *   3. Run every @After method (teardown), even if the test failed
 *
 * A brand new instance of the test class is created for every single
 * test method, exactly like real JUnit does, so that fixture state
 * created in @Before never leaks between tests.
 */
public class TestRunner {

    public static void run(Class<?> testClass) {
        int passCount = 0;
        int failCount = 0;

        System.out.println("Running tests for: " + testClass.getSimpleName());
        System.out.println("--------------------------------------------------");

        Method[] allMethods = testClass.getDeclaredMethods();

        for (Method testMethod : allMethods) {
            if (!testMethod.isAnnotationPresent(Test.class)) {
                continue;
            }

            try {
                // A fresh instance per test keeps fixtures isolated between tests.
                Object testInstance = testClass.getDeclaredConstructor().newInstance();

                // 1. Setup - run all @Before methods
                for (Method m : allMethods) {
                    if (m.isAnnotationPresent(Before.class)) {
                        m.invoke(testInstance);
                    }
                }

                // 2. Act + Assert - run the actual test method
                Throwable testFailure = null;
                try {
                    testMethod.invoke(testInstance);
                } catch (Exception invocationException) {
                    testFailure = invocationException.getCause() != null
                            ? invocationException.getCause()
                            : invocationException;
                }

                // 3. Teardown - run all @After methods (always, even after failure)
                for (Method m : allMethods) {
                    if (m.isAnnotationPresent(After.class)) {
                        m.invoke(testInstance);
                    }
                }

                if (testFailure == null) {
                    System.out.println("[PASS] " + testMethod.getName());
                    passCount++;
                } else {
                    System.out.println("[FAIL] " + testMethod.getName() + " -> " + testFailure.getMessage());
                    failCount++;
                }

            } catch (Exception setupException) {
                System.out.println("[ERROR] " + testMethod.getName() + " -> " + setupException.getMessage());
                failCount++;
            }
        }

        System.out.println("--------------------------------------------------");
        System.out.println("Tests run: " + (passCount + failCount)
                + ", Passed: " + passCount + ", Failed: " + failCount);
    }
}

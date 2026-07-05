import java.lang.reflect.Method;

/**
 * Custom reflection-based test engine that reads a class annotated
 * with @Suite and @SelectClasses, then executes every @Test method
 * found in each listed test class (equivalent to JUnit Platform's
 * suite runner, built with Core Java only).
 */
public class TestSuitesAndCategoriesRunner {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println(" Running Test Suite: AllTests");
        System.out.println("========================================");

        Class<?> suiteClass = AllTests.class;

        if (!suiteClass.isAnnotationPresent(Suite.class)) {
            System.out.println("AllTests is not marked with @Suite - aborting.");
            return;
        }

        if (!suiteClass.isAnnotationPresent(SelectClasses.class)) {
            System.out.println("AllTests has no @SelectClasses declared - aborting.");
            return;
        }

        SelectClasses selectClasses = suiteClass.getAnnotation(SelectClasses.class);

        for (Class<?> testClass : selectClasses.value()) {
            System.out.println("--- Category: " + testClass.getSimpleName() + " ---");
            runTestClass(testClass);
        }

        System.out.println("========================================");
        System.out.println("Total Passed: " + passed + ", Total Failed: " + failed);
        System.out.println("========================================");
    }

    private static void runTestClass(Class<?> testClass) {
        try {
            Object instance = testClass.getDeclaredConstructor().newInstance();

            for (Method method : testClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Test.class)) {
                    try {
                        method.invoke(instance);
                        System.out.println("[PASS] " + testClass.getSimpleName() + "." + method.getName());
                        passed++;
                    } catch (Exception e) {
                        Throwable cause = (e.getCause() != null) ? e.getCause() : e;
                        System.out.println("[FAIL] " + testClass.getSimpleName() + "." + method.getName() + " -> " + cause.getMessage());
                        failed++;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error instantiating " + testClass.getName() + ": " + e.getMessage());
        }
    }
}

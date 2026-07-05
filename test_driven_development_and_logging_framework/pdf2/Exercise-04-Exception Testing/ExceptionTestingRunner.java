import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Custom reflection-based test engine that invokes each @Test method
 * and checks whether it throws exactly the exception type declared in
 * its @ExpectedException annotation (equivalent to JUnit 5's
 * Assertions.assertThrows, built with Core Java only).
 */
public class ExceptionTestingRunner {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws Exception {
        System.out.println("========================================");
        System.out.println(" Running Exception Tests");
        System.out.println("========================================");

        Class<?> testClass = ExceptionThrowerTest.class;
        Object instance = testClass.getDeclaredConstructor().newInstance();

        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Test.class) && method.isAnnotationPresent(ExpectedException.class)) {
                Class<? extends Throwable> expected = method.getAnnotation(ExpectedException.class).value();

                try {
                    method.invoke(instance);
                    System.out.println("[FAIL] " + method.getName() + " -> expected " + expected.getSimpleName()
                            + " but no exception was thrown");
                    failed++;
                } catch (InvocationTargetException e) {
                    Throwable actual = e.getCause();
                    if (expected.isInstance(actual)) {
                        System.out.println("[PASS] " + method.getName() + " -> correctly threw "
                                + actual.getClass().getSimpleName() + " (\"" + actual.getMessage() + "\")");
                        passed++;
                    } else {
                        System.out.println("[FAIL] " + method.getName() + " -> expected " + expected.getSimpleName()
                                + " but got " + actual.getClass().getSimpleName());
                        failed++;
                    }
                }
            }
        }

        System.out.println("========================================");
        System.out.println("Total Passed: " + passed + ", Total Failed: " + failed);
        System.out.println("========================================");
    }
}

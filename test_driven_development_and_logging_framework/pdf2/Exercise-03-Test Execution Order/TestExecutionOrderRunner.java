import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Custom reflection-based test engine that discovers @Test methods on a
 * class marked @TestMethodOrder, sorts them by their @Order value, and
 * executes them in that exact sequence (equivalent to JUnit 5's
 * MethodOrderer.OrderAnnotation, built with Core Java only).
 */
public class TestExecutionOrderRunner {

    public static void main(String[] args) throws Exception {
        System.out.println("========================================");
        System.out.println(" Running Ordered Tests");
        System.out.println("========================================");

        Class<?> testClass = OrderedTests.class;

        if (!testClass.isAnnotationPresent(TestMethodOrder.class)) {
            System.out.println(testClass.getSimpleName() + " is not marked with @TestMethodOrder - aborting.");
            return;
        }

        Method[] orderedMethods = Arrays.stream(testClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(Test.class) && m.isAnnotationPresent(Order.class))
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(Order.class).value()))
                .toArray(Method[]::new);

        Object instance = testClass.getDeclaredConstructor().newInstance();

        for (Method method : orderedMethods) {
            method.invoke(instance);
        }

        String log = OrderedTests.getExecutionLog();
        System.out.println("Execution Order Log: " + log);

        if (log.equals("First-Second-Third-")) {
            System.out.println("[PASS] Tests executed in the correct declared order");
        } else {
            System.out.println("[FAIL] Tests executed out of order");
        }

        System.out.println("========================================");
    }
}

import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Custom reflection-based test engine that runs each @Test method on a
 * worker thread and enforces the time limit declared in its @Timeout
 * annotation (equivalent to JUnit 5's assertTimeout / @Timeout support,
 * built with Core Java only using java.util.concurrent).
 */
public class TimeoutAndPerformanceTestingRunner {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws Exception {
        System.out.println("========================================");
        System.out.println(" Running Timeout and Performance Tests");
        System.out.println("========================================");

        Class<?> testClass = PerformanceTesterTest.class;
        Object instance = testClass.getDeclaredConstructor().newInstance();
        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            for (Method method : testClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Test.class) && method.isAnnotationPresent(Timeout.class)) {
                    long timeoutMillis = method.getAnnotation(Timeout.class).millis();

                    Future<?> future = executor.submit(() -> {
                        method.invoke(instance);
                        return null;
                    });

                    long start = System.currentTimeMillis();
                    try {
                        future.get(timeoutMillis, TimeUnit.MILLISECONDS);
                        long duration = System.currentTimeMillis() - start;
                        System.out.println("[PASS] " + method.getName() + " completed in " + duration
                                + "ms (limit " + timeoutMillis + "ms)");
                        passed++;
                    } catch (TimeoutException e) {
                        future.cancel(true);
                        System.out.println("[FAIL] " + method.getName() + " -> exceeded timeout of "
                                + timeoutMillis + "ms");
                        failed++;
                    } catch (ExecutionException e) {
                        System.out.println("[FAIL] " + method.getName() + " -> threw " + e.getCause());
                        failed++;
                    }
                }
            }
        } finally {
            executor.shutdownNow();
        }

        System.out.println("========================================");
        System.out.println("Total Passed: " + passed + ", Total Failed: " + failed);
        System.out.println("========================================");
    }
}

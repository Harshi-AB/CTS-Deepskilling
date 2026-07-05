/**
 * Simulates tasks with different execution durations so that timeout
 * behaviour can be exercised.
 */
public class PerformanceTester {

    /**
     * A quick task that should comfortably finish within a short timeout.
     */
    public void performTask() throws InterruptedException {
        Thread.sleep(200);
    }

    /**
     * A deliberately slow task that should exceed a short timeout.
     */
    public void performSlowTask() throws InterruptedException {
        Thread.sleep(2000);
    }
}

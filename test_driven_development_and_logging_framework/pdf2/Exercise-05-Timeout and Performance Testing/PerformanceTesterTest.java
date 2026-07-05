/**
 * Test class verifying that {@link PerformanceTester} methods complete
 * within their declared @Timeout limits.
 */
public class PerformanceTesterTest {

    private PerformanceTester performanceTester = new PerformanceTester();

    @Test
    @Timeout(millis = 500)
    public void testPerformTask_withinTimeout() throws InterruptedException {
        performanceTester.performTask();
    }

    @Test
    @Timeout(millis = 500)
    public void testPerformSlowTask_exceedsTimeout() throws InterruptedException {
        performanceTester.performSlowTask();
    }
}

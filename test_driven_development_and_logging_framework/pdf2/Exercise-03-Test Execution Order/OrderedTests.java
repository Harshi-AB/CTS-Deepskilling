/**
 * Demonstrates controlling test execution order using @TestMethodOrder
 * and @Order. Each test appends to a shared log so the runner can
 * verify the tests actually executed in the declared sequence.
 */
@TestMethodOrder
public class OrderedTests {

    private static final StringBuilder executionLog = new StringBuilder();

    @Test
    @Order(3)
    public void thirdTest() {
        executionLog.append("Third-");
        System.out.println("Executing Third Test");
    }

    @Test
    @Order(1)
    public void firstTest() {
        executionLog.append("First-");
        System.out.println("Executing First Test");
    }

    @Test
    @Order(2)
    public void secondTest() {
        executionLog.append("Second-");
        System.out.println("Executing Second Test");
    }

    /**
     * Exposes the recorded execution log so the runner can verify order.
     */
    public static String getExecutionLog() {
        return executionLog.toString();
    }
}

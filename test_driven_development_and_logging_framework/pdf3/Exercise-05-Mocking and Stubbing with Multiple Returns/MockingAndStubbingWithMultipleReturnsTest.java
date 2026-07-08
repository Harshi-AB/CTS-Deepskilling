/**
 * MockingAndStubbingWithMultipleReturnsTest
 * ------------------------------------------------------------
 * Exercise 5: Mocking and Stubbing with Multiple Returns
 *
 * Demonstrates:
 *   1. Creating a mock object for the external API.
 *   2. Stubbing the method to return different values on consecutive calls.
 *   3. Writing a test case that uses the mock object across multiple calls.
 */
public class MockingAndStubbingWithMultipleReturnsTest {

    public static void main(String[] args) {
        System.out.println("Running: testMultipleReturns()");

        // Step 1: Create a mock object for the external API.
        MockExternalApi mockApi = new MockExternalApi();

        // Step 2: Stub the method to return different values on consecutive calls.
        mockApi.when_getData_thenReturnConsecutively("First Call", "Second Call", "Third Call");

        MyService service = new MyService(mockApi);

        // Step 3: Write a test case that uses the mock object across multiple invocations.
        String firstResult = service.fetchData();
        String secondResult = service.fetchData();
        String thirdResult = service.fetchData();
        String fourthResult = service.fetchData(); // Beyond stubbed values -> repeats last one

        System.out.println("Call 1 - Expected: First Call  | Actual: " + firstResult);
        System.out.println("Call 2 - Expected: Second Call | Actual: " + secondResult);
        System.out.println("Call 3 - Expected: Third Call  | Actual: " + thirdResult);
        System.out.println("Call 4 - Expected: Third Call  | Actual: " + fourthResult);

        boolean passed = "First Call".equals(firstResult)
                && "Second Call".equals(secondResult)
                && "Third Call".equals(thirdResult)
                && "Third Call".equals(fourthResult);

        if (passed) {
            System.out.println("RESULT: TEST PASSED - testMultipleReturns");
        } else {
            System.out.println("RESULT: TEST FAILED - testMultipleReturns");
        }
    }
}

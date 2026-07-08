/**
 * VerifyingInteractionsTest
 * ------------------------------------------------------------
 * Exercise 2: Verifying Interactions
 *
 * Demonstrates:
 *   1. Creating a mock object.
 *   2. Calling a method on the object under test that in turn
 *      calls the mocked dependency.
 *   3. Verifying that the mocked method was actually invoked
 *      (equivalent to Mockito's verify(mockApi).getData()).
 */
public class VerifyingInteractionsTest {

    public static void main(String[] args) {
        System.out.println("Running: testVerifyInteraction()");

        // Step 1: Create a mock object.
        MockExternalApi mockApi = new MockExternalApi();

        // Step 2: Call the method with specific arguments (fetchData -> getData).
        MyService service = new MyService(mockApi);
        service.fetchData();

        // Step 3: Verify the interaction.
        try {
            mockApi.verifyGetDataCalledOnce();
            System.out.println("RESULT: TEST PASSED - getData() was called exactly once");
        } catch (AssertionError e) {
            System.out.println("RESULT: TEST FAILED - " + e.getMessage());
        }
    }
}

/**
 * VerifyingInteractionOrderTest
 * ------------------------------------------------------------
 * Exercise 6: Verifying Interaction Order
 *
 * Demonstrates:
 *   1. Creating a mock object.
 *   2. Calling methods on it (indirectly, through MyService) in a specific order.
 *   3. Verifying that the methods were called in that exact order
 *      (equivalent to Mockito's InOrder verification).
 */
public class VerifyingInteractionOrderTest {

    public static void main(String[] args) {
        System.out.println("Running: testInteractionOrder()");

        // Step 1: Create a mock object.
        MockExternalApi mockApi = new MockExternalApi();
        mockApi.when_getData_thenReturn("Mock Data");

        MyService service = new MyService(mockApi);

        // Step 2: Call the methods in a specific order (connect() then getData()).
        String result = service.initializeAndFetchData();
        System.out.println("Fetched data: " + result);

        // Step 3: Verify the interaction order.
        try {
            mockApi.verifyOrder("connect", "getData");
            System.out.println("RESULT: TEST PASSED - connect() was called before getData()");
        } catch (AssertionError e) {
            System.out.println("RESULT: TEST FAILED - " + e.getMessage());
        }
    }
}

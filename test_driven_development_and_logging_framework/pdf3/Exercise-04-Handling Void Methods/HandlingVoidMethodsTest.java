/**
 * HandlingVoidMethodsTest
 * ------------------------------------------------------------
 * Exercise 4: Handling Void Methods
 *
 * Demonstrates:
 *   1. Creating a mock object.
 *   2. "Stubbing" a void method (doNothing behavior - the default for void methods).
 *   3. Verifying the void method interaction, including the argument it was called with.
 */
public class HandlingVoidMethodsTest {

    public static void main(String[] args) {
        System.out.println("Running: testVoidMethod()");

        // Step 1: Create a mock object.
        MockExternalApi mockApi = new MockExternalApi();

        // Step 2: The void method saveData() requires no explicit stubbing -
        // by default it "does nothing", which our mock replicates.

        MyService service = new MyService(mockApi);
        service.save("Important Data");

        // Step 3: Verify the interaction.
        try {
            mockApi.verifySaveDataCalledWith("Important Data");
            System.out.println("RESULT: TEST PASSED - saveData() was called with the expected argument");
        } catch (AssertionError e) {
            System.out.println("RESULT: TEST FAILED - " + e.getMessage());
        }
    }
}

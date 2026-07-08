/**
 * HandlingVoidMethodsWithExceptionsTest
 * ------------------------------------------------------------
 * Exercise 7: Handling Void Methods with Exceptions
 *
 * Demonstrates:
 *   1. Creating a mock object.
 *   2. Stubbing a void method to throw an exception
 *      (equivalent to Mockito's doThrow(...).when(mock).method()).
 *   3. Verifying the interaction still happened, and that the
 *      expected exception was actually thrown.
 */
public class HandlingVoidMethodsWithExceptionsTest {

    public static void main(String[] args) {
        System.out.println("Running: testDeleteDataThrowsException()");

        // Step 1: Create a mock object.
        MockExternalApi mockApi = new MockExternalApi();

        // Step 2: Stub the void method to throw an exception.
        mockApi.when_deleteData_thenThrow(new RuntimeException("Delete failed"));

        MyService service = new MyService(mockApi);

        boolean exceptionThrown = false;
        String exceptionMessage = null;

        try {
            service.deleteData("123");
        } catch (RuntimeException e) {
            exceptionThrown = true;
            exceptionMessage = e.getMessage();
        }

        System.out.println("Exception thrown : " + exceptionThrown);
        System.out.println("Exception message: " + exceptionMessage);

        // Step 3: Verify the interaction (the call happened despite throwing).
        boolean verificationPassed;
        try {
            mockApi.verifyDeleteDataCalledWith("123");
            verificationPassed = true;
        } catch (AssertionError e) {
            System.out.println("Verification failed: " + e.getMessage());
            verificationPassed = false;
        }

        boolean passed = exceptionThrown
                && "Delete failed".equals(exceptionMessage)
                && verificationPassed;

        if (passed) {
            System.out.println("RESULT: TEST PASSED - testDeleteDataThrowsException");
        } else {
            System.out.println("RESULT: TEST FAILED - testDeleteDataThrowsException");
        }
    }
}

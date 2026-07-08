/**
 * ArgumentMatchingTest
 * ------------------------------------------------------------
 * Exercise 3: Argument Matching
 *
 * Demonstrates:
 *   1. Creating a mock object.
 *   2. Calling a method with specific arguments.
 *   3. Using argument matchers (eq, any) to stub AND verify the interaction.
 */
public class ArgumentMatchingTest {

    public static void main(String[] args) {
        System.out.println("Running: testArgumentMatching()");

        // Step 1: Create a mock object.
        MockExternalApi mockApi = new MockExternalApi();

        // Stub: only the exact argument "123" returns "Data for 123"
        // (equivalent to when(mockApi.getDataById(eq("123"))).thenReturn("Data for 123"))
        mockApi.when_getDataById_matching_thenReturn(ArgumentMatcher.eq("123"), "Data for 123");

        MyService service = new MyService(mockApi);

        // Step 2: Call the method with a specific argument.
        String result = service.fetchDataById("123");

        System.out.println("Expected : Data for 123");
        System.out.println("Actual   : " + result);

        boolean stubbingPassed = "Data for 123".equals(result);

        // Step 3: Use an argument matcher to verify the interaction.
        // (equivalent to verify(mockApi).getDataById(eq("123")))
        boolean verificationPassed;
        try {
            mockApi.verifyCalledWith(ArgumentMatcher.eq("123"));
            verificationPassed = true;
        } catch (AssertionError e) {
            System.out.println("Verification failed: " + e.getMessage());
            verificationPassed = false;
        }

        if (stubbingPassed && verificationPassed) {
            System.out.println("RESULT: TEST PASSED - testArgumentMatching");
        } else {
            System.out.println("RESULT: TEST FAILED - testArgumentMatching");
        }
    }
}

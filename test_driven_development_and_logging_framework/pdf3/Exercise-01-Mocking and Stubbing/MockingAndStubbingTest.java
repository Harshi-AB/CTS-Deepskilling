/**
 * MockingAndStubbingTest
 * ------------------------------------------------------------
 * Exercise 1: Mocking and Stubbing
 *
 * Demonstrates:
 *   1. Creating a mock object for the external API.
 *   2. Stubbing the mock's method to return a predefined value.
 *   3. Using the mock object inside a test of MyService.
 *
 * NOTE: Since this project must compile with plain "javac *.java"
 * (no JUnit / Mockito jars on the classpath), the test is executed
 * from a main() method instead of a @Test method, and assertions
 * are performed manually with simple if/else checks.
 */
public class MockingAndStubbingTest {

    public static void main(String[] args) {
        System.out.println("Running: testExternalApi()");

        // Step 1: Create a mock object for the external API.
        MockExternalApi mockApi = new MockExternalApi();

        // Step 2: Stub the method to return a predefined value.
        mockApi.when_getData_thenReturn("Mock Data");

        // Step 3: Write a test case that uses the mock object.
        MyService service = new MyService(mockApi);
        String result = service.fetchData();

        System.out.println("Expected : Mock Data");
        System.out.println("Actual   : " + result);

        if ("Mock Data".equals(result)) {
            System.out.println("RESULT: TEST PASSED - testExternalApi");
        } else {
            System.out.println("RESULT: TEST FAILED - testExternalApi");
        }
    }
}

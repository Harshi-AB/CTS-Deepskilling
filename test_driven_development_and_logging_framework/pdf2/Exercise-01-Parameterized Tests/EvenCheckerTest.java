/**
 * Parameterized test class for {@link EvenChecker}.
 * Each test method is executed once per value declared in its @ValueSource.
 */
public class EvenCheckerTest {

    private EvenChecker evenChecker = new EvenChecker();

    @ParameterizedTest
    @ValueSource(ints = {2, 4, 6, 8, 10})
    public void testIsEven_withEvenNumbers(int number) {
        Assertions.assertTrue(evenChecker.isEven(number), number + " should have been reported as even");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5, 7, 9})
    public void testIsEven_withOddNumbers(int number) {
        Assertions.assertTrue(!evenChecker.isEven(number), number + " should have been reported as odd");
    }
}

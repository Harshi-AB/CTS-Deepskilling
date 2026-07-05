/**
 * Basic JUnit-style tests for the MathUtils class (Exercise 2).
 * Each public method of MathUtils gets its own dedicated test method,
 * following JUnit naming convention: test<MethodBeingTested>.
 */
public class MathUtilsTest {

    private final MathUtils mathUtils = new MathUtils();

    @Test
    public void testAdd() {
        Assert.assertEquals(8, mathUtils.add(5, 3));
    }

    @Test
    public void testSubtract() {
        Assert.assertEquals(2, mathUtils.subtract(5, 3));
    }

    @Test
    public void testMultiply() {
        Assert.assertEquals(15, mathUtils.multiply(5, 3));
    }

    @Test
    public void testDivide() {
        Assert.assertEquals(2, mathUtils.divide(6, 3));
    }

    @Test
    public void testIsEvenWithEvenNumber() {
        Assert.assertTrue(mathUtils.isEven(4));
    }

    @Test
    public void testIsEvenWithOddNumber() {
        Assert.assertFalse(mathUtils.isEven(7));
    }
}

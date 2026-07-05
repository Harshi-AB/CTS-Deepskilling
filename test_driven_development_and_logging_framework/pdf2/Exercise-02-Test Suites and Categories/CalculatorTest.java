/**
 * Test class covering {@link Calculator}. Belongs to the AllTests suite.
 */
public class CalculatorTest {

    private Calculator calculator = new Calculator();

    @Test
    public void testAdd() {
        Assertions.assertEquals(5, calculator.add(2, 3), "Addition of 2 and 3 failed");
    }

    @Test
    public void testSubtract() {
        Assertions.assertEquals(1, calculator.subtract(3, 2), "Subtraction of 2 from 3 failed");
    }
}


public class CalculatorTest {

    private final Calculator calculator = new Calculator();

    @Test
    public void testAdd() {
        int result = calculator.add(2, 3);
        Assert.assertEquals(5, result);
    }

    @Test
    public void testSubtract() {
        int result = calculator.subtract(10, 4);
        Assert.assertEquals(6, result);
    }
}

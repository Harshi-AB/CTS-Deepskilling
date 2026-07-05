/**
 * Test class verifying that {@link ExceptionThrower} throws the
 * expected exception type for each scenario.
 */
public class ExceptionThrowerTest {

    private ExceptionThrower exceptionThrower = new ExceptionThrower();

    @Test
    @ExpectedException(IllegalArgumentException.class)
    public void testThrowException_illegalArgument() {
        exceptionThrower.throwException("illegal");
    }

    @Test
    @ExpectedException(NullPointerException.class)
    public void testThrowException_nullPointer() {
        exceptionThrower.throwException("null");
    }

    @Test
    @ExpectedException(ArithmeticException.class)
    public void testThrowException_arithmetic() {
        exceptionThrower.throwException("arithmetic");
    }
}

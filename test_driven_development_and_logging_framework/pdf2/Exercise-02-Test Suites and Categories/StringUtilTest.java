/**
 * Test class covering {@link StringUtil}. Belongs to the AllTests suite.
 */
public class StringUtilTest {

    private StringUtil stringUtil = new StringUtil();

    @Test
    public void testReverse() {
        Assertions.assertEquals("cba", stringUtil.reverse("abc"), "Reversing 'abc' failed");
    }

    @Test
    public void testReverse_singleCharacter() {
        Assertions.assertEquals("x", stringUtil.reverse("x"), "Reversing a single character failed");
    }
}

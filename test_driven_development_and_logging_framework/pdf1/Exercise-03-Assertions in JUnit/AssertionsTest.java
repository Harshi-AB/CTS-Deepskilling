/**
 * Demonstrates the use of various assertions in JUnit-style testing
 * (Exercise 3), matching and extending the exercise's solution code.
 */
public class AssertionsTest {

    @Test
    public void testAssertions() {
        // Assert equals
        Assert.assertEquals(5, 2 + 3);

        // Assert true
        Assert.assertTrue(5 > 3);

        // Assert false
        Assert.assertFalse(5 < 3);

        // Assert null
        Assert.assertNull(null);

        // Assert not null
        Assert.assertNotNull(new Object());
    }

    @Test
    public void testAssertNotEquals() {
        Assert.assertNotEquals(5, 2 + 2);
    }

    @Test
    public void testAssertArrayEquals() {
        int[] expected = {1, 2, 3};
        int[] actual = {1, 2, 3};
        Assert.assertArrayEquals(expected, actual);
    }
}

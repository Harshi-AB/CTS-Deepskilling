/**
 * Test suite grouping all related test classes together.
 * Equivalent to a JUnit 5 @Suite / @SelectClasses declaration.
 */
@Suite
@SelectClasses({ CalculatorTest.class, StringUtilTest.class })
public class AllTests {
    // Marker class only - no logic required. The runner reads the
    // annotations above to discover which test classes to execute.
}

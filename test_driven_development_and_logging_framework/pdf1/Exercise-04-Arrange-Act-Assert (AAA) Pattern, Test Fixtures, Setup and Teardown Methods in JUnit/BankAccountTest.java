/**
 * Demonstrates:
 *   - The Arrange-Act-Assert (AAA) pattern inside each test method.
 *   - Test fixtures via @Before (setUp) and @After (tearDown).
 *
 * setUp() runs before every single @Test method and creates a brand new
 * BankAccount fixture with a known starting balance, so tests never
 * interfere with each other. tearDown() runs after every test to show
 * where cleanup logic would go (e.g. closing a connection, releasing
 * a resource).
 */
public class BankAccountTest {

    // The fixture shared by every test method (re-created fresh before each test).
    private BankAccount account;

    @Before
    public void setUp() {
        // Fixture setup: every test starts with an account holding 100.0
        account = new BankAccount(100.0);
        System.out.println("  [setUp] Created fresh BankAccount with balance 100.0");
    }

    @After
    public void tearDown() {
        // Teardown: release/reset the fixture (nothing external to close here,
        // but this is where such cleanup would happen).
        account = null;
        System.out.println("  [tearDown] Released BankAccount fixture");
    }

    @Test
    public void testDepositIncreasesBalance() {
        // Arrange: fixture account already has balance 100.0 (created in setUp)
        double depositAmount = 50.0;

        // Act
        account.deposit(depositAmount);

        // Assert
        Assert.assertEquals(150.0, account.getBalance());
    }

    @Test
    public void testWithdrawDecreasesBalance() {
        // Arrange
        double withdrawAmount = 30.0;

        // Act
        account.withdraw(withdrawAmount);

        // Assert
        Assert.assertEquals(70.0, account.getBalance());
    }

    @Test
    public void testWithdrawMoreThanBalanceThrowsException() {
        // Arrange
        double withdrawAmount = 1000.0;
        boolean exceptionThrown = false;

        // Act
        try {
            account.withdraw(withdrawAmount);
        } catch (IllegalStateException e) {
            exceptionThrown = true;
        }

        // Assert
        Assert.assertTrue(exceptionThrown);
    }

    @Test
    public void testInitialFixtureBalanceIsUnchangedAtStart() {
        // Arrange: nothing extra needed, fixture already set up

        // Act: no action, just checking the fixture's starting state

        // Assert
        Assert.assertEquals(100.0, account.getBalance());
    }
}

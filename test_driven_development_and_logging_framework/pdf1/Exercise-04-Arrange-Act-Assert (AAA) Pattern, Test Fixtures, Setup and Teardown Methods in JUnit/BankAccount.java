/**
 * A simple BankAccount class, used to demonstrate the Arrange-Act-Assert
 * pattern together with test fixtures (Exercise 4). A bank account is a
 * natural fit for this because each test needs a freshly-created account
 * with a known starting balance (a fixture) before it can act on it.
 */
public class BankAccount {

    private double balance;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    /** Deposits a positive amount into the account. */
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        balance += amount;
    }

    /** Withdraws an amount from the account, if sufficient funds exist. */
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (amount > balance) {
            throw new IllegalStateException("Insufficient funds");
        }
        balance -= amount;
    }

    public double getBalance() {
        return balance;
    }
}

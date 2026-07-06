-- =====================================================================
-- File     : 02_seed_data.sql
-- Exercise : 06 - Cursors
-- Notes    : Transaction dates are relative to SYSDATE so that they
--            always fall inside the "current month" regardless of
--            when this script is executed.
-- =====================================================================

INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastModified)
VALUES (1, 'John Doe', TO_DATE('1985-05-15','YYYY-MM-DD'), 1000, SYSDATE);
INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastModified)
VALUES (2, 'Jane Smith', TO_DATE('1990-07-20','YYYY-MM-DD'), 1500, SYSDATE);

INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastModified)
VALUES (101, 1, 'Savings', 1000, SYSDATE);
INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastModified)
VALUES (102, 2, 'Checking', 1500, SYSDATE);

-- Transactions in the current month (relative to today)
INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
VALUES (1, 101, TRUNC(SYSDATE,'MM') + 2, 500, 'Deposit');
INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
VALUES (2, 101, TRUNC(SYSDATE,'MM') + 5, 150, 'Withdrawal');
INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
VALUES (3, 102, TRUNC(SYSDATE,'MM') + 3, 300, 'Deposit');

-- A transaction from LAST month -> must NOT appear in the current month's statement
INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
VALUES (4, 102, ADD_MONTHS(TRUNC(SYSDATE,'MM'), -1) + 10, 200, 'Withdrawal');

-- Loans for interest-rate policy update
INSERT INTO Loans (LoanID, CustomerID, LoanAmount, InterestRate, StartDate, EndDate)
VALUES (1, 1, 5000, 4.5, ADD_MONTHS(SYSDATE,-12), ADD_MONTHS(SYSDATE,48));   -- below policy threshold
INSERT INTO Loans (LoanID, CustomerID, LoanAmount, InterestRate, StartDate, EndDate)
VALUES (2, 2, 8000, 12.0, ADD_MONTHS(SYSDATE,-24), ADD_MONTHS(SYSDATE,36));  -- above policy threshold

COMMIT;

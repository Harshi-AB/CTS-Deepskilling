-- =====================================================================
-- File        : 02_seed_data.sql
-- Exercise    : 01 - Control Structures
-- Purpose     : Sample data crafted so that every scenario in this
--               exercise has at least one qualifying row:
--                 - Scenario 1 needs a customer aged > 60 with a loan.
--                 - Scenario 2 needs a customer with Balance > 10000.
--                 - Scenario 3 needs a loan whose EndDate falls within
--                   the next 30 days from SYSDATE.
-- =====================================================================

-- Customer 1: age > 60 (born 1960) -> qualifies for Scenario 1
INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastModified)
VALUES (1, 'John Doe', TO_DATE('1960-05-15','YYYY-MM-DD'), 1000, SYSDATE);

-- Customer 2: young customer, high balance -> qualifies for Scenario 2
INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastModified)
VALUES (2, 'Jane Smith', TO_DATE('1990-07-20','YYYY-MM-DD'), 15000, SYSDATE);

-- Customer 3: elderly AND high balance -> qualifies for both 1 & 2
INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastModified)
VALUES (3, 'Robert King', TO_DATE('1955-01-10','YYYY-MM-DD'), 12500, SYSDATE);

-- Customer 4: neither elderly nor high-balance -> control case
INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastModified)
VALUES (4, 'Emily Clarke', TO_DATE('1998-11-02','YYYY-MM-DD'), 500, SYSDATE);

-- Accounts
INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastModified)
VALUES (1, 1, 'Savings', 1000, SYSDATE);
INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastModified)
VALUES (2, 2, 'Checking', 15000, SYSDATE);
INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastModified)
VALUES (3, 3, 'Savings', 12500, SYSDATE);

-- Loans
-- Loan for Customer 1 (elderly) -> interest rate should be discounted by Scenario 1
INSERT INTO Loans (LoanID, CustomerID, LoanAmount, InterestRate, StartDate, EndDate)
VALUES (1, 1, 5000, 8.5, ADD_MONTHS(SYSDATE,-6), ADD_MONTHS(SYSDATE,54));

-- Loan for Customer 3 (elderly) due within next 30 days -> Scenario 1 & 3
INSERT INTO Loans (LoanID, CustomerID, LoanAmount, InterestRate, StartDate, EndDate)
VALUES (2, 3, 8000, 9.0, ADD_MONTHS(SYSDATE,-60), SYSDATE + 15);

-- Loan for Customer 2 (not elderly) due within next 30 days -> Scenario 3 only
INSERT INTO Loans (LoanID, CustomerID, LoanAmount, InterestRate, StartDate, EndDate)
VALUES (3, 2, 3000, 7.25, ADD_MONTHS(SYSDATE,-24), SYSDATE + 25);

-- Loan far in the future -> should NOT be picked up by Scenario 3
INSERT INTO Loans (LoanID, CustomerID, LoanAmount, InterestRate, StartDate, EndDate)
VALUES (4, 4, 2000, 6.0, SYSDATE, ADD_MONTHS(SYSDATE,36));

COMMIT;

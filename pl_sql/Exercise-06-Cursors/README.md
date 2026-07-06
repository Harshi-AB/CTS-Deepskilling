# Exercise 06 — Cursors

Banking domain. Demonstrates **explicit cursors**, `FOR UPDATE` / `WHERE
CURRENT OF` row-level locking and update, and cursor `FOR` loops.

## Folder Structure

```
Exercise-06-Cursors/
├── 01_schema.sql                                -- Customers, Accounts, Transactions, Loans
├── 02_seed_data.sql
├── 03_scenario1_GenerateMonthlyStatements.sql   -- Scenario 1
├── 04_scenario2_ApplyAnnualFee.sql              -- Scenario 2
├── 05_scenario3_UpdateLoanInterestRates.sql     -- Scenario 3
└── run_all.sql
```

Seed transaction dates are relative to `TRUNC(SYSDATE,'MM')` so the
"current month" test data is always correct no matter when you run the
script; one transaction is deliberately placed in the *previous* month
to prove the date filter works.

## Scenario 1 — `GenerateMonthlyStatements`

**Question:** Use a cursor to fetch all transactions for the current
month and generate a statement for each customer.

### Compile & Run
```bash
sqlplus <user>/<pwd>@<db> @01_schema.sql
sqlplus <user>/<pwd>@<db> @02_seed_data.sql
sqlplus <user>/<pwd>@<db> @03_scenario1_GenerateMonthlyStatements.sql
```
### Expected Output
```
Statement for: John Doe (CustomerID 1)
  02-<CURMON>-26 | Account 101 | Deposit    | Amount: 500
  05-<CURMON>-26 | Account 101 | Withdrawal | Amount: 150
  ---> Net movement this month: 350
----------------------------------------
Statement for: Jane Smith (CustomerID 2)
  03-<CURMON>-26 | Account 102 | Deposit    | Amount: 300
  ---> Net movement this month: 300
----------------------------------------
```
(The last-month withdrawal of 200 on account 102 is correctly excluded
from Jane Smith's statement.)

## Scenario 2 — `ApplyAnnualFee`

**Question:** Use a cursor to iterate through all accounts and deduct
an annual maintenance fee from each balance.

### Run
```bash
sqlplus <user>/<pwd>@<db> @04_scenario2_ApplyAnnualFee.sql
```
### Expected Output
```
Account 101 charged annual fee of 25. New balance: 975
Account 102 charged annual fee of 25. New balance: 1475
--- Annual fee processing complete ---

ACCOUNTID    BALANCE
--------- ----------
      101        975
      102       1475
```

## Scenario 3 — `UpdateLoanInterestRates`

**Question:** Use a cursor to fetch all loans and update their
interest rates according to a new policy.

**Policy used here:** rates below 6% are raised by 0.25%, rates at or
above 10% are lowered by 0.50%, everything in between is left
unchanged (the exact thresholds are easy to change in the script).

### Run
```bash
sqlplus <user>/<pwd>@<db> @05_scenario3_UpdateLoanInterestRates.sql
```
### Expected Output
```
Loan 1 (Customer 1): 4.5% -> 4.75%
Loan 2 (Customer 2): 12% -> 11.5%
--- Loan interest rate policy update complete ---

    LOANID CUSTOMERID INTERESTRATE
---------- ---------- ------------
         1          1         4.75
         2          2         11.5
```

## Run Everything
```bash
sqlplus <user>/<pwd>@<db> @run_all.sql
```

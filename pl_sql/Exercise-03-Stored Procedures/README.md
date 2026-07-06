# Exercise 03 — Stored Procedures

Banking domain. Demonstrates parameterized stored procedures, `%TYPE`
anchoring, bulk `UPDATE`, `SQL%ROWCOUNT`, and `FOR UPDATE` row locking.

## Folder Structure

```
Exercise-03-Stored Procedures/
├── 01_schema.sql                             -- Customers, Accounts, Employees
├── 02_seed_data.sql
├── 03_scenario1_ProcessMonthlyInterest.sql   -- Scenario 1
├── 04_scenario2_UpdateEmployeeBonus.sql      -- Scenario 2
├── 05_scenario3_TransferFunds.sql            -- Scenario 3
└── run_all.sql
```

## Scenario 1 — `ProcessMonthlyInterest`

**Question:** Calculate and update the balance of all savings accounts
by applying a 1% interest rate.

**Logic:** Single set-based `UPDATE ... WHERE AccountType = 'Savings'`
(fastest and most idiomatic PL/SQL approach for a whole-table
operation); `SQL%ROWCOUNT` reports how many accounts were touched.

### Compile & Run
```bash
sqlplus <user>/<pwd>@<db> @01_schema.sql
sqlplus <user>/<pwd>@<db> @02_seed_data.sql
sqlplus <user>/<pwd>@<db> @03_scenario1_ProcessMonthlyInterest.sql
```
### Expected Output
```
--- Balances BEFORE interest ---
ACCOUNTID ACCOUNTTYPE   BALANCE
--------- ----------- ---------
      101 Savings          1000
      102 Savings          2000
      103 Checking          500

--- Running ProcessMonthlyInterest ---
Monthly interest applied to 2 savings account(s) at 1%.

--- Balances AFTER interest ---
ACCOUNTID ACCOUNTTYPE   BALANCE
--------- ----------- ---------
      101 Savings          1010
      102 Savings          2020
      103 Checking          500
```

## Scenario 2 — `UpdateEmployeeBonus`

**Question:** Update the salary of employees in a given department by
adding a bonus percentage passed as a parameter.

### Run
```bash
sqlplus <user>/<pwd>@<db> @04_scenario2_UpdateEmployeeBonus.sql
```
### Expected Output
```
--- Salaries BEFORE bonus (IT department) ---
EMPLOYEEID NAME          DEPARTMENT     SALARY
---------- ------------- ---------- ----------
         2 Bob Brown     IT              60000
         3 Carol White   IT              55000

--- Running UpdateEmployeeBonus('IT', 5) ---
Bonus of 5% applied to 2 employee(s) in department IT.

--- Salaries AFTER bonus (IT department) ---
EMPLOYEEID NAME          DEPARTMENT     SALARY
---------- ------------- ---------- ----------
         2 Bob Brown     IT              63000
         3 Carol White   IT            57750

--- Department with no employees ---
No employees found in department: Finance
```

## Scenario 3 — `TransferFunds`

**Question:** Transfer a specified amount from one account to another,
checking that the source account has sufficient balance before making
the transfer.

### Run
```bash
sqlplus <user>/<pwd>@<db> @05_scenario3_TransferFunds.sql
```
### Expected Output
```
--- Balances BEFORE transfer ---
ACCOUNTID    BALANCE
--------- ----------
      101       1000
      102       2000
      103        500

--- Test 1: Valid transfer (101 -> 102, amount 200) ---
Transferred 200 from account 101 to account 102.

--- Test 2: Insufficient balance (103 -> 101, amount 999999) ---
ERROR: Insufficient balance in account 103. Available: 500, Requested: 999999

--- Balances AFTER transfer ---
ACCOUNTID    BALANCE
--------- ----------
      101        800
      102       2200
      103        500
```

## Run Everything
```bash
sqlplus <user>/<pwd>@<db> @run_all.sql
```

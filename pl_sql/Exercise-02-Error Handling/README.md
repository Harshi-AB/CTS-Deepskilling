# Exercise 02 — Error Handling

Banking domain. Demonstrates `EXCEPTION` blocks, named exceptions
(`NO_DATA_FOUND`, `DUP_VAL_ON_INDEX`), user-defined exceptions,
`RAISE_APPLICATION_ERROR`, `ROLLBACK`, and a shared `ErrorLog` audit
table.

## Folder Structure

```
Exercise-02-Error Handling/
├── 01_schema.sql                        -- Customers, Accounts, Employees, ErrorLog
├── 02_seed_data.sql
├── 03_scenario1_SafeTransferFunds.sql   -- Scenario 1
├── 04_scenario2_UpdateSalary.sql        -- Scenario 2
├── 05_scenario3_AddNewCustomer.sql      -- Scenario 3
└── run_all.sql
```

`ErrorLog(ErrorLogID, ProcedureName, ErrorMessage, ErrorDate)` is
populated by every procedure below whenever it catches a handled
exception, so failures are auditable instead of silently disappearing.

> **Note on ErrorLogID values below:** the sample outputs assume each
> scenario file is run against a freshly-loaded schema (`01_schema.sql`
> + `02_seed_data.sql`, then that one scenario file only), so
> `ErrorLogID` restarts at 1 each time. If you run `run_all.sql`
> instead, `ErrorLogID` simply keeps incrementing across all three
> scenarios — the messages and ordering are unaffected.

## Scenario 1 — `SafeTransferFunds`

**Question:** Transfer funds between two accounts. If any error occurs
(e.g., insufficient funds), log an appropriate error message and roll
back the transaction.

**Handles:** source account missing (`NO_DATA_FOUND`), insufficient
balance (custom exception `e_insufficient_funds`), destination account
missing, and any other unexpected error (`WHEN OTHERS`) — all four
paths log to `ErrorLog` and roll back cleanly.

### Compile & Run
```bash
sqlplus <user>/<pwd>@<db> @01_schema.sql
sqlplus <user>/<pwd>@<db> @02_seed_data.sql
sqlplus <user>/<pwd>@<db> @03_scenario1_SafeTransferFunds.sql
```
### Expected Output
```
--- Test 1: Successful transfer (101 -> 102, amount 100) ---
Transfer successful: 100 moved from account 101 to account 102.

--- Test 2: Insufficient funds (102 -> 101, amount 999999) ---
ERROR: Insufficient funds. See ErrorLog.

--- Test 3: Source account does not exist (9999 -> 101, amount 50) ---
ERROR: Source account does not exist. See ErrorLog.

--- Test 4: Destination account does not exist (101 -> 8888, amount 50) ---
ERROR: Unexpected error - ORA-20001: Destination account 8888 does not exist. See ErrorLog.

--- ErrorLog contents ---
ERRORLOGID PROCEDURENAME      ERRORMESSAGE
---------- ------------------ --------------------------------------------------
         1 SafeTransferFunds  Insufficient funds in account 102 for transfer of 999999.
         2 SafeTransferFunds  Source account 9999 does not exist.
         3 SafeTransferFunds  ORA-20001: Destination account 8888 does not exist.

--- Final account balances ---
ACCOUNTID    BALANCE
--------- ----------
      101        900
      102        300
```

## Scenario 2 — `UpdateSalary`

**Question:** Increase an employee's salary by a given percentage. If
the employee ID does not exist, handle the exception and log an error.

### Run
```bash
sqlplus <user>/<pwd>@<db> @04_scenario2_UpdateSalary.sql
```
### Expected Output
```
--- Test 1: Valid employee (EmployeeID 1, +10%) ---
Employee 1 salary updated from 70000 to 77000.

--- Test 2: Non-existent employee (EmployeeID 999, +10%) ---
ERROR: Employee ID 999 does not exist. See ErrorLog.

--- ErrorLog contents ---
ERRORLOGID PROCEDURENAME  ERRORMESSAGE
---------- -------------- -----------------------------------
         1 UpdateSalary   Employee ID 999 does not exist.

--- Final employee data ---
EMPLOYEEID NAME               SALARY
---------- ------------------ -------
         1 Alice Johnson        77000
         2 Bob Brown            60000
```

## Scenario 3 — `AddNewCustomer`

**Question:** Insert a new customer. If a customer with the same ID
already exists, handle the exception, log an error, and prevent the
insertion.

### Run
```bash
sqlplus <user>/<pwd>@<db> @05_scenario3_AddNewCustomer.sql
```
### Expected Output
```
--- Test 1: New unique customer (CustomerID 3) ---
Customer 3 (Michael Lee) added successfully.

--- Test 2: Duplicate customer (CustomerID 1 already exists) ---
ERROR: Customer ID 1 already exists. See ErrorLog.

--- ErrorLog contents ---
ERRORLOGID PROCEDURENAME    ERRORMESSAGE
---------- ---------------- ---------------------------------------------
         1 AddNewCustomer   Customer ID 1 already exists. Insert prevented.

--- Final customer data ---
CUSTOMERID NAME               BALANCE
---------- ------------------ -------
         1 John Doe              1000
         2 Jane Smith            1500
         3 Michael Lee           2500
```

## Run Everything
```bash
sqlplus <user>/<pwd>@<db> @run_all.sql
```

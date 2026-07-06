# Exercise 05 — Triggers

Banking domain. Demonstrates row-level `BEFORE`/`AFTER` triggers,
`:NEW`/`:OLD` pseudo-records, and `RAISE_APPLICATION_ERROR` inside a
trigger body to enforce business rules at the database layer.

## Folder Structure

```
Exercise-05-Triggers/
├── 01_schema.sql                                 -- Customers, Accounts, Transactions, AuditLog
├── 02_seed_data.sql
├── 03_scenario1_UpdateCustomerLastModified.sql   -- Scenario 1
├── 04_scenario2_LogTransaction.sql               -- Scenario 2
├── 05_scenario3_CheckTransactionRules.sql        -- Scenario 3
└── run_all.sql
```

> **Load order matters here.** `run_all.sql` deliberately creates the
> `CheckTransactionRules` trigger (file `05`) **before** running the
> `LogTransaction` test (file `04`) so that the validation rule is
> already active by the time any test data is inserted into
> `Transactions`. If you run the scenario files manually one at a time,
> follow the same order: `03` → `05` → `04`.

## Scenario 1 — `UpdateCustomerLastModified`

**Question:** Update the `LastModified` column of `Customers` to the
current date whenever a customer's record is updated.

```sql
TRIGGER UpdateCustomerLastModified BEFORE UPDATE ON Customers FOR EACH ROW
```

### Compile & Run
```bash
sqlplus <user>/<pwd>@<db> @01_schema.sql
sqlplus <user>/<pwd>@<db> @02_seed_data.sql
sqlplus <user>/<pwd>@<db> @03_scenario1_UpdateCustomerLastModified.sql
```
### Expected Output
```
--- Customer 1 BEFORE update ---
CUSTOMERID NAME     BALANCE LASTMODIFIED
---------- -------- ------- ------------
         1 John Doe    1000 05-JUL-26   <- original SYSDATE from seed insert

--- Customer 1 AFTER update (LastModified should now be "now") ---
CUSTOMERID NAME     BALANCE LASTMODIFIED
---------- -------- ------- ------------
         1 John Doe    1250 05-JUL-26   <- refreshed to the moment of the UPDATE
```
(The date portion may look identical if both statements run within the
same second/day; what matters is `LastModified` always reflects the
moment of the last `UPDATE`, which you can confirm by adding `TO_CHAR(...,'HH24:MI:SS')`.)

## Scenario 2 — `LogTransaction`

**Question:** Insert a record into an `AuditLog` table whenever a
transaction is inserted into `Transactions`.

```sql
TRIGGER LogTransaction AFTER INSERT ON Transactions FOR EACH ROW
```

### Run
```bash
sqlplus <user>/<pwd>@<db> @05_scenario3_CheckTransactionRules.sql
sqlplus <user>/<pwd>@<db> @04_scenario2_LogTransaction.sql
```
### Expected Output
```
--- Inserting a valid deposit transaction ---
1 row created.

--- AuditLog contents after the insert ---
   AUDITID TRANSACTIONID  ACCOUNTID     AMOUNT TRANSACTIONTYPE ACTIONTIMESTAMP
---------- ------------- ---------- ---------- --------------- ---------------
         1             1        101        200 Deposit         05-JUL-26
```

## Scenario 3 — `CheckTransactionRules`

**Question:** Ensure withdrawals do not exceed the balance and
deposits are positive before inserting a record into `Transactions`.

```sql
TRIGGER CheckTransactionRules BEFORE INSERT ON Transactions FOR EACH ROW
```

### Run
```bash
sqlplus <user>/<pwd>@<db> @05_scenario3_CheckTransactionRules.sql
```
### Expected Output
*(run standalone, i.e. before `LogTransaction` exists — so `AuditLog`
is still empty; run via `run_all.sql` and `AuditLog` will show the
scenario-2 deposit too)*
```
--- Test 1: Valid withdrawal (account 102 has balance 1500, withdraw 300) ---
Test 1 passed: withdrawal accepted.

--- Test 2: Invalid withdrawal exceeding balance (should raise ORA-20051) ---
Expected error caught: ORA-20051: Withdrawal of 999999 exceeds available balance 1500 for account 102.

--- Test 3: Invalid deposit with non-positive amount (should raise ORA-20050) ---
Expected error caught: ORA-20050: Transaction amount must be positive.

--- Final Transactions & AuditLog ---
TRANSACTIONID  ACCOUNTID     AMOUNT TRANSACTIONTYPE
------------- ---------- ---------- ---------------
            2        102        300 Withdrawal

no rows selected
```

## Run Everything
```bash
sqlplus <user>/<pwd>@<db> @run_all.sql
```

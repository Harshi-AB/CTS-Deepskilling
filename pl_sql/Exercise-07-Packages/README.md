# Exercise 07 — Packages

Banking domain. Demonstrates PL/SQL **packages** (specification +
body), grouping related procedures/functions together, and calling
package members with dot notation (`PackageName.Member`).

## Folder Structure

```
Exercise-07-Packages/
├── 01_schema.sql                                 -- Customers, Accounts, Employees
├── 02_seed_data.sql
├── 03_scenario1_CustomerManagement_spec.sql      -- Scenario 1 (package spec)
├── 04_scenario1_CustomerManagement_body.sql      -- Scenario 1 (package body + tests)
├── 05_scenario2_EmployeeManagement_spec.sql      -- Scenario 2 (package spec)
├── 06_scenario2_EmployeeManagement_body.sql      -- Scenario 2 (package body + tests)
├── 07_scenario3_AccountOperations_spec.sql       -- Scenario 3 (package spec)
├── 08_scenario3_AccountOperations_body.sql       -- Scenario 3 (package body + tests)
└── run_all.sql
```

A package **must** be compiled as spec first, then body — that is why
each scenario is split into two numbered files.

## Scenario 1 — `CustomerManagement` package

**Question:** Group customer-related procedures (add, update) and a
function (get balance) into a single package.

```sql
PACKAGE CustomerManagement AS
    PROCEDURE AddCustomer(...);
    PROCEDURE UpdateCustomerDetails(...);
    FUNCTION  GetCustomerBalance(...) RETURN NUMBER;
END;
```

### Compile & Run
```bash
sqlplus <user>/<pwd>@<db> @01_schema.sql
sqlplus <user>/<pwd>@<db> @02_seed_data.sql
sqlplus <user>/<pwd>@<db> @03_scenario1_CustomerManagement_spec.sql
sqlplus <user>/<pwd>@<db> @04_scenario1_CustomerManagement_body.sql
```
### Expected Output
```
CustomerManagement.AddCustomer: Customer 3 (Michael Lee) added.
CustomerManagement.AddCustomer: ERROR - Customer ID 1 already exists.
CustomerManagement.UpdateCustomerDetails: Customer 1 updated.
Balance for Customer 1: 1800

CUSTOMERID NAME               BALANCE
---------- ------------------ -------
         1 John Doe Jr.          1800
         2 Jane Smith            1500
         3 Michael Lee           2200
```

## Scenario 2 — `EmployeeManagement` package

**Question:** Group employee-related procedures (hire, update) and a
function (calculate annual salary) into a single package.

### Run
```bash
sqlplus <user>/<pwd>@<db> @05_scenario2_EmployeeManagement_spec.sql
sqlplus <user>/<pwd>@<db> @06_scenario2_EmployeeManagement_body.sql
```
### Expected Output
```
EmployeeManagement.HireEmployee: Employee 2 (Bob Brown) hired into IT.
EmployeeManagement.HireEmployee: ERROR - Employee ID 1 already exists.
EmployeeManagement.UpdateEmployeeDetails: Employee 2 updated.
Annual salary for Employee 2: 816000

EMPLOYEEID NAME               POSITION         SALARY DEPARTMENT
---------- ------------------ --------------- ------- ----------
         1 Alice Johnson      Manager           70000 HR
         2 Bob Brown          Senior Developer  68000 IT
```

## Scenario 3 — `AccountOperations` package

**Question:** Group account-related procedures (open, close) and a
function (get total balance across a customer's accounts) into a
single package.

### Run
```bash
sqlplus <user>/<pwd>@<db> @07_scenario3_AccountOperations_spec.sql
sqlplus <user>/<pwd>@<db> @08_scenario3_AccountOperations_body.sql
```
### Expected Output
```
AccountOperations.OpenAccount: Account 104 (Savings) opened for Customer 2.
Total balance for Customer 1: 1500
AccountOperations.CloseAccount: ERROR - Account 101 still has a positive balance (1000) and cannot be closed.

1 row updated.

AccountOperations.CloseAccount: Account 101 closed.

ACCOUNTID CUSTOMERID ACCOUNTTYPE      BALANCE STATUS
--------- ---------- --------------- -------- ------
      101          1 Savings                0 CLOSED
      102          1 Checking             500 OPEN
      103          2 Savings             1500 OPEN
      104          2 Savings              750 OPEN
```

## Run Everything
```bash
sqlplus <user>/<pwd>@<db> @run_all.sql
```

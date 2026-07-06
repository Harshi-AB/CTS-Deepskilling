# PL/SQL Concepts — Cognizant Digital Nurture 5.0 Deep Skilling

Complete, independently-runnable Oracle PL/SQL projects for all 7 exercises
in the "PL/SQL Concepts" exercise sheet (banking domain: Customers,
Accounts, Transactions, Loans, Employees).

## Why Oracle (not MySQL)?

The exercise sheet asks for genuine **PL/SQL** constructs — packages,
`%ROWTYPE`/`%TYPE` anchoring, explicit cursors with `FOR UPDATE` / `WHERE
CURRENT OF`, `RAISE_APPLICATION_ERROR`, named exception handling
(`NO_DATA_FOUND`, `DUP_VAL_ON_INDEX`), and row-level triggers with
`:NEW`/`:OLD`. PL/SQL is Oracle's proprietary procedural extension to SQL;
MySQL has no PL/SQL engine and, critically, **MySQL has no equivalent to
PL/SQL packages at all** (Exercise 7 cannot be done in MySQL under any
interpretation). To keep every exercise faithful to the original PL/SQL
syntax and to keep all 7 projects consistent with each other, every
exercise here is implemented in **Oracle Database SQL/PL-SQL** (Core SQL +
PL/SQL only — no proprietary Oracle packages like `UTL_*` are required,
only the built-in `DBMS_OUTPUT` package for printing, which is part of
Core PL/SQL and ships with every edition of Oracle).

## Folder Structure

```
PLSQL_Concepts/
│
├── README.md                                  <- this file
│
├── Exercise-01-Control Structures/
│   ├── 01_schema.sql
│   ├── 02_seed_data.sql
│   ├── 03_scenario1_senior_loan_discount.sql
│   ├── 04_scenario2_vip_flag.sql
│   ├── 05_scenario3_loan_due_reminders.sql
│   ├── run_all.sql
│   └── README.md
│
├── Exercise-02-Error Handling/
│   ├── 01_schema.sql
│   ├── 02_seed_data.sql
│   ├── 03_scenario1_SafeTransferFunds.sql
│   ├── 04_scenario2_UpdateSalary.sql
│   ├── 05_scenario3_AddNewCustomer.sql
│   ├── run_all.sql
│   └── README.md
│
├── Exercise-03-Stored Procedures/
│   ├── 01_schema.sql
│   ├── 02_seed_data.sql
│   ├── 03_scenario1_ProcessMonthlyInterest.sql
│   ├── 04_scenario2_UpdateEmployeeBonus.sql
│   ├── 05_scenario3_TransferFunds.sql
│   ├── run_all.sql
│   └── README.md
│
├── Exercise-04-Functions/
│   ├── 01_schema.sql
│   ├── 02_seed_data.sql
│   ├── 03_scenario1_CalculateAge.sql
│   ├── 04_scenario2_CalculateMonthlyInstallment.sql
│   ├── 05_scenario3_HasSufficientBalance.sql
│   ├── run_all.sql
│   └── README.md
│
├── Exercise-05-Triggers/
│   ├── 01_schema.sql
│   ├── 02_seed_data.sql
│   ├── 03_scenario1_UpdateCustomerLastModified.sql
│   ├── 04_scenario2_LogTransaction.sql
│   ├── 05_scenario3_CheckTransactionRules.sql
│   ├── run_all.sql
│   └── README.md
│
├── Exercise-06-Cursors/
│   ├── 01_schema.sql
│   ├── 02_seed_data.sql
│   ├── 03_scenario1_GenerateMonthlyStatements.sql
│   ├── 04_scenario2_ApplyAnnualFee.sql
│   ├── 05_scenario3_UpdateLoanInterestRates.sql
│   ├── run_all.sql
│   └── README.md
│
└── Exercise-07-Packages/
    ├── 01_schema.sql
    ├── 02_seed_data.sql
    ├── 03_scenario1_CustomerManagement_spec.sql
    ├── 04_scenario1_CustomerManagement_body.sql
    ├── 05_scenario2_EmployeeManagement_spec.sql
    ├── 06_scenario2_EmployeeManagement_body.sql
    ├── 07_scenario3_AccountOperations_spec.sql
    ├── 08_scenario3_AccountOperations_body.sql
    ├── run_all.sql
    └── README.md
```

## Design Principles Used Across Every Exercise

1. **Independent projects.** Each `Exercise-0X-*` folder creates its own
   copy of only the tables it needs (`01_schema.sql`), so any single
   exercise folder can be zipped/copied/run completely on its own —
   nothing is shared between folders.
2. **Re-runnable schema.** `01_schema.sql` drops its own tables first
   (ignoring "table does not exist" errors) so you can run it over and
   over on the same schema/user without manual cleanup.
3. **Deterministic seed data.** `02_seed_data.sql` inserts rows that are
   specifically crafted so that every scenario in that exercise has at
   least one row that qualifies and (where relevant) one that does not,
   so the PL/SQL logic branches are all exercised.
4. **One file per scenario.** Each scenario file contains the requested
   procedure/function/trigger/package **and** a runnable test harness
   (`EXEC ...`, anonymous blocks, and verification `SELECT`s) so running
   the file alone produces visible output — no external test file needed.
5. **`run_all.sql`** in every folder chains schema → seed data → every
   scenario in the correct order, for a single-command run of the whole
   exercise.
6. **Core SQL/PL-SQL only.** Only standard SQL DDL/DML and Core PL/SQL
   (`DBMS_OUTPUT`, exception handling, cursors, `%TYPE`/`%ROWTYPE`) are
   used — no advanced/optional packages, no Java stored procedures, no
   external tables.

## Prerequisites

- Oracle Database (18c XE, 19c, 21c XE, or any edition) reachable via
  SQL*Plus or SQLcl.
- A schema/user with `CREATE TABLE`, `CREATE PROCEDURE`, `CREATE
  FUNCTION`, `CREATE TRIGGER`, and `CREATE PACKAGE` privileges (the
  default privileges of any normal Oracle user account are sufficient).

## Generic Compile & Run Commands

From inside any exercise folder:

```bash
# Compile + run everything for that exercise in one shot
sqlplus <username>/<password>@<connect_string> @run_all.sql

# Or step through it manually
sqlplus <username>/<password>@<connect_string>
SQL> @01_schema.sql
SQL> @02_seed_data.sql
SQL> @03_scenario1_....sql
SQL> @04_scenario2_....sql
SQL> @05_scenario3_....sql
```

Example using Oracle's free XE edition on localhost:

```bash
sqlplus system/YourPassword@//localhost:1521/XEPDB1 @run_all.sql
```

SQLcl (`sql` command) works identically:

```bash
sql <username>/<password>@<connect_string> @run_all.sql
```

See each exercise's own `README.md` for the scenario-by-scenario
breakdown, exact commands, and expected output.

## A Note on "Expected Output"

A few scenarios (loan-due reminders, age calculation, monthly
statements) depend on `SYSDATE` — the actual date/time the script is
run. The seed data is deliberately written relative to `SYSDATE` /
`TRUNC(SYSDATE,'MM')` so the *qualifying rows* are always the same
regardless of when you run it, but printed **ages** and **date labels**
will reflect the real current date on your machine. Each exercise
README calls this out where relevant.

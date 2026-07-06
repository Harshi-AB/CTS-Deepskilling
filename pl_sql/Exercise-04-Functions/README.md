# Exercise 04 — Functions

Banking domain. Demonstrates standalone PL/SQL functions, `RETURN`
types, using a function inline in a `SELECT`, and the fact that a pure
PL/SQL `BOOLEAN` cannot be selected or printed directly (Oracle SQL has
no native boolean column type prior to 23c), solved here with a small
`VARCHAR2` wrapper for demonstration purposes only.

## Folder Structure

```
Exercise-04-Functions/
├── 01_schema.sql                                  -- Customers, Accounts
├── 02_seed_data.sql
├── 03_scenario1_CalculateAge.sql                  -- Scenario 1
├── 04_scenario2_CalculateMonthlyInstallment.sql   -- Scenario 2
├── 05_scenario3_HasSufficientBalance.sql          -- Scenario 3
└── run_all.sql
```

## Scenario 1 — `CalculateAge`

**Question:** Takes a customer's date of birth and returns their age
in years.

```sql
FUNCTION CalculateAge(p_dob IN DATE) RETURN NUMBER
```

### Compile & Run
```bash
sqlplus <user>/<pwd>@<db> @01_schema.sql
sqlplus <user>/<pwd>@<db> @02_seed_data.sql
sqlplus <user>/<pwd>@<db> @03_scenario1_CalculateAge.sql
```
### Expected Output
*(ages below assume the script is run on 05-JUL-2026; the function
always returns the age correct for **today**, whatever "today" is)*
```
CUSTOMERID NAME          DOB              AGE
---------- ------------- --------- ----------
         1 John Doe      15-MAY-85         41
         2 Jane Smith    20-JUL-90         35

Age for DOB 1960-01-01: 66
Age for DOB 2005-12-31: 20
```

## Scenario 2 — `CalculateMonthlyInstallment`

**Question:** Takes the loan amount, interest rate, and loan duration
in years and returns the monthly installment (EMI).

**Formula:**
`EMI = P * r * (1+r)^n / ((1+r)^n - 1)`, where `r` = monthly interest
rate (annual % / 100 / 12) and `n` = number of months. When the rate is
0, `EMI = Principal / n`.

### Run
```bash
sqlplus <user>/<pwd>@<db> @04_scenario2_CalculateMonthlyInstallment.sql
```
### Expected Output
```
EMI for 500000 @ 8.5% for 5 years: 10258.27
EMI for 120000 @ 0%   for 2 years: 5000
EMI for 1000000 @ 7.25% for 20 years: 7903.76
```

## Scenario 3 — `HasSufficientBalance`

**Question:** Takes an account ID and an amount and returns a boolean
indicating whether the account has at least the specified amount.

```sql
FUNCTION HasSufficientBalance(p_account_id IN NUMBER, p_amount IN NUMBER) RETURN BOOLEAN
```

A companion `HasSufficientBalanceStr` (returns `'TRUE'`/`'FALSE'` as
`VARCHAR2`) is provided purely so the result can be shown in
`DBMS_OUTPUT`/`SELECT`, since native `BOOLEAN` can only be consumed
from PL/SQL, not SQL.

### Run
```bash
sqlplus <user>/<pwd>@<db> @05_scenario3_HasSufficientBalance.sql
```
### Expected Output
```
Account 101 has sufficient balance for 500: TRUE
Account 102 has sufficient balance for 5000: FALSE

ACCOUNTID    BALANCE SUFFICIENT_FOR_500 SUFFICIENT_FOR_5000
--------- ---------- ------------------ -------------------
      101       1000 TRUE               TRUE
      102       1500 TRUE               FALSE
```

## Run Everything
```bash
sqlplus <user>/<pwd>@<db> @run_all.sql
```

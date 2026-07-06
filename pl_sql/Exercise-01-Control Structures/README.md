# Exercise 01 — Control Structures

Banking domain. Demonstrates `IF/ELSIF`, `FOR` loops over cursors, and
conditional branching inside anonymous PL/SQL blocks.

## Folder Structure

```
Exercise-01-Control Structures/
├── 01_schema.sql                          -- Customers, Accounts, Loans tables
├── 02_seed_data.sql                       -- sample data (elderly/VIP/soon-due rows included)
├── 03_scenario1_senior_loan_discount.sql  -- Scenario 1
├── 04_scenario2_vip_flag.sql              -- Scenario 2
├── 05_scenario3_loan_due_reminders.sql    -- Scenario 3
└── run_all.sql                            -- runs everything above in order
```

## Scenario 1 — Senior loan interest discount

**Question:** Loop through all customers, check age, and if above 60,
apply a 1% discount to their current loan interest rates.

**Logic:** For each customer, age is computed with
`TRUNC(MONTHS_BETWEEN(SYSDATE, DOB) / 12)`. If `age > 60`, every loan
belonging to that customer has its `InterestRate` reduced by 1
percentage point (floored at 0 using `GREATEST`).

### Compile
```bash
sqlplus <user>/<pwd>@<db> @01_schema.sql
sqlplus <user>/<pwd>@<db> @02_seed_data.sql
sqlplus <user>/<pwd>@<db> @03_scenario1_senior_loan_discount.sql
```
### Run (single command)
```bash
sqlplus <user>/<pwd>@<db> @run_all.sql
```
### Expected Output (Scenario 1, seed data as provided)
```
Customer John Doe (Age 66) -> 1 loan(s) discounted by 1%.
Customer Jane Smith (Age 35) does not qualify for discount.
Customer Robert King (Age 71) -> 1 loan(s) discounted by 1%.
Customer Emily Clarke (Age 27) does not qualify for discount.
--- Senior loan discount processing complete ---

   LOANID NAME               DOB        INTERESTRATE
---------- ------------------ ---------- ------------
         1 John Doe           15-MAY-60          7.5
         2 Robert King        10-JAN-55          8.0
         3 Jane Smith         20-JUL-90         7.25
         4 Emily Clarke       02-NOV-98            6
```
*(Ages shown assume the script is run in 2026; your exact numbers will
shift by ±1 depending on today's date, but John Doe and Robert King will
always be the ones discounted since they are the customers born before
1966.)*

## Scenario 2 — VIP flag based on balance

**Question:** Iterate through all customers and set `IsVIP` to `TRUE`
for those with a balance over $10,000.

**Logic:** Loop through customers; `IsVIP := 'Y'` if `Balance > 10000`,
else `'N'`.

### Run
```bash
sqlplus <user>/<pwd>@<db> @04_scenario2_vip_flag.sql
```
### Expected Output
```
Customer John Doe | Balance: 1000 | IsVIP set to: N
Customer Jane Smith | Balance: 15000 | IsVIP set to: Y
Customer Robert King | Balance: 12500 | IsVIP set to: Y
Customer Emily Clarke | Balance: 500 | IsVIP set to: N
--- VIP flag processing complete ---

CUSTOMERID NAME               BALANCE I
---------- ------------------ ------- -
         1 John Doe              1000 N
         2 Jane Smith           15000 Y
         3 Robert King          12500 Y
         4 Emily Clarke           500 N
```

## Scenario 3 — Loans due within 30 days

**Question:** Fetch all loans due in the next 30 days and print a
reminder message for each customer.

**Logic:** `WHERE EndDate BETWEEN SYSDATE AND SYSDATE + 30`.

### Run
```bash
sqlplus <user>/<pwd>@<db> @05_scenario3_loan_due_reminders.sql
```
### Expected Output
```
REMINDER: Dear Robert King, your loan #2 of amount 8000 is due on <today+15> (15 day(s) remaining).
REMINDER: Dear Jane Smith, your loan #3 of amount 3000 is due on <today+25> (25 day(s) remaining).
--- Loan due reminder processing complete ---
```
(Loan #1 and #4 are more than 30 days out, so they are correctly
excluded.)

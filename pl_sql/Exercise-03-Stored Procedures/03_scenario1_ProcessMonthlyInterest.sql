-- =====================================================================
-- File     : 03_scenario1_ProcessMonthlyInterest.sql
-- Scenario : 1 - Process monthly interest for all savings accounts.
-- Procedure: ProcessMonthlyInterest
-- Logic    : Apply a 1% interest rate to the balance of every account
--            whose AccountType = 'Savings'.
-- =====================================================================

CREATE OR REPLACE PROCEDURE ProcessMonthlyInterest IS
    c_interest_rate CONSTANT NUMBER := 0.01;   -- 1%
    v_accounts_processed NUMBER := 0;
BEGIN
    UPDATE Accounts
       SET Balance = Balance + (Balance * c_interest_rate),
           LastModified = SYSDATE
     WHERE AccountType = 'Savings';

    v_accounts_processed := SQL%ROWCOUNT;

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Monthly interest applied to ' || v_accounts_processed ||
                          ' savings account(s) at ' || (c_interest_rate * 100) || '%.');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('ERROR in ProcessMonthlyInterest: ' || SQLERRM);
END ProcessMonthlyInterest;
/

-- ---------------------------------------------------------------------
-- Test harness
-- ---------------------------------------------------------------------
SET SERVEROUTPUT ON;

PROMPT --- Balances BEFORE interest ---
SELECT AccountID, AccountType, Balance FROM Accounts ORDER BY AccountID;

PROMPT --- Running ProcessMonthlyInterest ---
EXEC ProcessMonthlyInterest;

PROMPT --- Balances AFTER interest ---
SELECT AccountID, AccountType, Balance FROM Accounts ORDER BY AccountID;

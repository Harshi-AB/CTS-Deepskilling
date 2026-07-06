-- =====================================================================
-- File     : 05_scenario3_HasSufficientBalance.sql
-- Scenario : 3 - Check if a customer/account has sufficient balance
--            before making a transaction.
-- Function : HasSufficientBalance(p_account_id, p_amount) RETURN BOOLEAN
-- Note     : Pure PL/SQL BOOLEAN cannot be selected directly in SQL or
--            printed with DBMS_OUTPUT, so a small wrapper function
--            HasSufficientBalanceStr is also provided returning
--            VARCHAR2('TRUE'/'FALSE') purely for demonstration/testing.
-- =====================================================================

CREATE OR REPLACE FUNCTION HasSufficientBalance (
    p_account_id IN Accounts.AccountID%TYPE,
    p_amount     IN NUMBER
) RETURN BOOLEAN
IS
    v_balance Accounts.Balance%TYPE;
BEGIN
    SELECT Balance INTO v_balance
      FROM Accounts
     WHERE AccountID = p_account_id;

    RETURN (v_balance >= p_amount);
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN FALSE;
END HasSufficientBalance;
/

-- Wrapper so the result can be displayed/queried (BOOLEAN is PL/SQL-only)
CREATE OR REPLACE FUNCTION HasSufficientBalanceStr (
    p_account_id IN Accounts.AccountID%TYPE,
    p_amount     IN NUMBER
) RETURN VARCHAR2
IS
BEGIN
    IF HasSufficientBalance(p_account_id, p_amount) THEN
        RETURN 'TRUE';
    ELSE
        RETURN 'FALSE';
    END IF;
END HasSufficientBalanceStr;
/

-- ---------------------------------------------------------------------
-- Test harness
-- ---------------------------------------------------------------------
SET SERVEROUTPUT ON;

DECLARE
    v_result BOOLEAN;
BEGIN
    v_result := HasSufficientBalance(101, 500);   -- balance 1000 >= 500 -> TRUE
    IF v_result THEN
        DBMS_OUTPUT.PUT_LINE('Account 101 has sufficient balance for 500: TRUE');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Account 101 has sufficient balance for 500: FALSE');
    END IF;

    v_result := HasSufficientBalance(102, 5000);  -- balance 1500 < 5000 -> FALSE
    IF v_result THEN
        DBMS_OUTPUT.PUT_LINE('Account 102 has sufficient balance for 5000: TRUE');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Account 102 has sufficient balance for 5000: FALSE');
    END IF;
END;
/

PROMPT --- Queryable version via SQL ---
SELECT AccountID, Balance,
       HasSufficientBalanceStr(AccountID, 500)  AS Sufficient_For_500,
       HasSufficientBalanceStr(AccountID, 5000) AS Sufficient_For_5000
FROM   Accounts
ORDER BY AccountID;

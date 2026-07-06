-- =====================================================================
-- File     : 05_scenario3_TransferFunds.sql
-- Scenario : 3 - Customers transfer funds between their accounts.
-- Procedure: TransferFunds(p_from_account, p_to_account, p_amount)
-- Logic    : Check that the source account has sufficient balance
--            before transferring; otherwise raise a clear error.
-- =====================================================================

CREATE OR REPLACE PROCEDURE TransferFunds (
    p_from_account IN Accounts.AccountID%TYPE,
    p_to_account   IN Accounts.AccountID%TYPE,
    p_amount       IN NUMBER
) IS
    v_from_balance Accounts.Balance%TYPE;
    v_to_exists    NUMBER;
BEGIN
    IF p_amount <= 0 THEN
        RAISE_APPLICATION_ERROR(-20020, 'Transfer amount must be positive.');
    END IF;

    SELECT COUNT(*) INTO v_to_exists FROM Accounts WHERE AccountID = p_to_account;
    IF v_to_exists = 0 THEN
        RAISE_APPLICATION_ERROR(-20021, 'Destination account ' || p_to_account || ' does not exist.');
    END IF;

    SELECT Balance INTO v_from_balance
      FROM Accounts
     WHERE AccountID = p_from_account
     FOR UPDATE;

    IF v_from_balance < p_amount THEN
        RAISE_APPLICATION_ERROR(-20022, 'Insufficient balance in account ' || p_from_account ||
                                         '. Available: ' || v_from_balance || ', Requested: ' || p_amount);
    END IF;

    UPDATE Accounts SET Balance = Balance - p_amount, LastModified = SYSDATE WHERE AccountID = p_from_account;
    UPDATE Accounts SET Balance = Balance + p_amount, LastModified = SYSDATE WHERE AccountID = p_to_account;

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Transferred ' || p_amount || ' from account ' || p_from_account ||
                          ' to account ' || p_to_account || '.');
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('ERROR: Source account ' || p_from_account || ' does not exist.');
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('ERROR in TransferFunds: ' || SQLERRM);
END TransferFunds;
/

-- ---------------------------------------------------------------------
-- Test harness
-- ---------------------------------------------------------------------
SET SERVEROUTPUT ON;

PROMPT --- Balances BEFORE transfer ---
SELECT AccountID, Balance FROM Accounts ORDER BY AccountID;

PROMPT --- Test 1: Valid transfer (101 -> 102, amount 200) ---
EXEC TransferFunds(101, 102, 200);

PROMPT --- Test 2: Insufficient balance (103 -> 101, amount 999999) ---
EXEC TransferFunds(103, 101, 999999);

PROMPT --- Balances AFTER transfer ---
SELECT AccountID, Balance FROM Accounts ORDER BY AccountID;

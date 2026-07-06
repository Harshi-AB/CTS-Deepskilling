-- =====================================================================
-- File     : 03_scenario1_SafeTransferFunds.sql
-- Scenario : 1 - Handle exceptions during fund transfers.
-- Procedure: SafeTransferFunds(p_from_account, p_to_account, p_amount)
-- Logic    : Debit source account, credit destination account.
--            - If source account does not exist                -> log & raise friendly error
--            - If the balance is insufficient                  -> log & rollback, no partial update
--            - If destination account does not exist            -> log & rollback
--            - Any other unexpected error                       -> log & rollback
-- =====================================================================

CREATE OR REPLACE PROCEDURE SafeTransferFunds (
    p_from_account IN Accounts.AccountID%TYPE,
    p_to_account   IN Accounts.AccountID%TYPE,
    p_amount       IN NUMBER
) IS
    v_from_balance   Accounts.Balance%TYPE;
    e_insufficient_funds EXCEPTION;
BEGIN
    -- Lock and read the source account balance
    SELECT Balance INTO v_from_balance
      FROM Accounts
     WHERE AccountID = p_from_account
     FOR UPDATE;

    IF v_from_balance < p_amount THEN
        RAISE e_insufficient_funds;
    END IF;

    -- Debit source
    UPDATE Accounts
       SET Balance = Balance - p_amount,
           LastModified = SYSDATE
     WHERE AccountID = p_from_account;

    -- Credit destination (will raise NO_DATA_FOUND-style FK error/0 rows if it does not exist)
    UPDATE Accounts
       SET Balance = Balance + p_amount,
           LastModified = SYSDATE
     WHERE AccountID = p_to_account;

    IF SQL%ROWCOUNT = 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Destination account ' || p_to_account || ' does not exist.');
    END IF;

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Transfer successful: ' || p_amount ||
                          ' moved from account ' || p_from_account ||
                          ' to account ' || p_to_account || '.');

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        ROLLBACK;
        INSERT INTO ErrorLog (ProcedureName, ErrorMessage)
        VALUES ('SafeTransferFunds', 'Source account ' || p_from_account || ' does not exist.');
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('ERROR: Source account does not exist. See ErrorLog.');

    WHEN e_insufficient_funds THEN
        ROLLBACK;
        INSERT INTO ErrorLog (ProcedureName, ErrorMessage)
        VALUES ('SafeTransferFunds', 'Insufficient funds in account ' || p_from_account ||
                                       ' for transfer of ' || p_amount || '.');
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('ERROR: Insufficient funds. See ErrorLog.');

    WHEN OTHERS THEN
        ROLLBACK;
        INSERT INTO ErrorLog (ProcedureName, ErrorMessage)
        VALUES ('SafeTransferFunds', SQLERRM);
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('ERROR: Unexpected error - ' || SQLERRM || '. See ErrorLog.');
END SafeTransferFunds;
/

-- ---------------------------------------------------------------------
-- Test harness
-- ---------------------------------------------------------------------
SET SERVEROUTPUT ON;

PROMPT --- Test 1: Successful transfer (101 -> 102, amount 100) ---
EXEC SafeTransferFunds(101, 102, 100);

PROMPT --- Test 2: Insufficient funds (102 -> 101, amount 999999) ---
EXEC SafeTransferFunds(102, 101, 999999);

PROMPT --- Test 3: Source account does not exist (9999 -> 101, amount 50) ---
EXEC SafeTransferFunds(9999, 101, 50);

PROMPT --- Test 4: Destination account does not exist (101 -> 8888, amount 50) ---
EXEC SafeTransferFunds(101, 8888, 50);

PROMPT --- ErrorLog contents ---
SELECT ErrorLogID, ProcedureName, ErrorMessage, ErrorDate FROM ErrorLog ORDER BY ErrorLogID;

PROMPT --- Final account balances ---
SELECT AccountID, Balance FROM Accounts ORDER BY AccountID;

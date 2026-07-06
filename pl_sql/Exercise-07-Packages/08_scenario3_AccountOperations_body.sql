-- =====================================================================
-- File     : 08_scenario3_AccountOperations_body.sql
-- Package  : AccountOperations (body)
-- =====================================================================

CREATE OR REPLACE PACKAGE BODY AccountOperations AS

    PROCEDURE OpenAccount (
        p_account_id     IN Accounts.AccountID%TYPE,
        p_customer_id    IN Accounts.CustomerID%TYPE,
        p_account_type   IN Accounts.AccountType%TYPE,
        p_initial_balance IN Accounts.Balance%TYPE DEFAULT 0
    ) IS
        v_customer_exists NUMBER;
    BEGIN
        SELECT COUNT(*) INTO v_customer_exists FROM Customers WHERE CustomerID = p_customer_id;
        IF v_customer_exists = 0 THEN
            RAISE_APPLICATION_ERROR(-20060, 'Customer ID ' || p_customer_id || ' does not exist.');
        END IF;

        INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, Status, LastModified)
        VALUES (p_account_id, p_customer_id, p_account_type, p_initial_balance, 'OPEN', SYSDATE);

        COMMIT;
        DBMS_OUTPUT.PUT_LINE('AccountOperations.OpenAccount: Account ' || p_account_id ||
                              ' (' || p_account_type || ') opened for Customer ' || p_customer_id || '.');
    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            ROLLBACK;
            DBMS_OUTPUT.PUT_LINE('AccountOperations.OpenAccount: ERROR - Account ID ' ||
                                  p_account_id || ' already exists.');
        WHEN OTHERS THEN
            ROLLBACK;
            DBMS_OUTPUT.PUT_LINE('AccountOperations.OpenAccount: ERROR - ' || SQLERRM);
    END OpenAccount;


    PROCEDURE CloseAccount (
        p_account_id IN Accounts.AccountID%TYPE
    ) IS
        v_balance Accounts.Balance%TYPE;
    BEGIN
        SELECT Balance INTO v_balance
          FROM Accounts
         WHERE AccountID = p_account_id;

        IF v_balance > 0 THEN
            RAISE_APPLICATION_ERROR(-20061,
                'Account ' || p_account_id || ' still has a positive balance (' ||
                v_balance || ') and cannot be closed.');
        END IF;

        UPDATE Accounts
           SET Status = 'CLOSED', LastModified = SYSDATE
         WHERE AccountID = p_account_id;

        COMMIT;
        DBMS_OUTPUT.PUT_LINE('AccountOperations.CloseAccount: Account ' || p_account_id || ' closed.');
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            DBMS_OUTPUT.PUT_LINE('AccountOperations.CloseAccount: ERROR - Account ID ' ||
                                  p_account_id || ' does not exist.');
        WHEN OTHERS THEN
            ROLLBACK;
            DBMS_OUTPUT.PUT_LINE('AccountOperations.CloseAccount: ERROR - ' || SQLERRM);
    END CloseAccount;


    FUNCTION GetTotalBalance (
        p_customer_id IN Accounts.CustomerID%TYPE
    ) RETURN NUMBER IS
        v_total NUMBER;
    BEGIN
        SELECT NVL(SUM(Balance), 0) INTO v_total
          FROM Accounts
         WHERE CustomerID = p_customer_id
           AND Status = 'OPEN';

        RETURN v_total;
    END GetTotalBalance;

END AccountOperations;
/

-- ---------------------------------------------------------------------
-- Test harness
-- ---------------------------------------------------------------------
SET SERVEROUTPUT ON;

PROMPT --- Test: OpenAccount (new) ---
EXEC AccountOperations.OpenAccount(104, 2, 'Savings', 750);

PROMPT --- Test: GetTotalBalance for Customer 1 (accounts 101 + 102) ---
DECLARE
    v_total NUMBER;
BEGIN
    v_total := AccountOperations.GetTotalBalance(1);
    DBMS_OUTPUT.PUT_LINE('Total balance for Customer 1: ' || v_total);
END;
/

PROMPT --- Test: CloseAccount on an account with a positive balance (should fail) ---
EXEC AccountOperations.CloseAccount(101);

PROMPT --- Test: Zero out balance, then close successfully ---
UPDATE Accounts SET Balance = 0 WHERE AccountID = 101;
COMMIT;
EXEC AccountOperations.CloseAccount(101);

PROMPT --- Final Accounts table ---
SELECT AccountID, CustomerID, AccountType, Balance, Status FROM Accounts ORDER BY AccountID;

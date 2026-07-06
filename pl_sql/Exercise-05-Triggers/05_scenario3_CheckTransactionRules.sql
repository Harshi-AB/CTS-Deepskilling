-- =====================================================================
-- File     : 05_scenario3_CheckTransactionRules.sql
-- Scenario : 3 - Enforce business rules on deposits and withdrawals.
-- Trigger  : CheckTransactionRules (BEFORE INSERT ON Transactions)
-- Rules    : - Deposits must have a positive Amount.
--            - Withdrawals must have a positive Amount that does not
--              exceed the current balance of the related account.
-- =====================================================================

CREATE OR REPLACE TRIGGER CheckTransactionRules
BEFORE INSERT ON Transactions
FOR EACH ROW
DECLARE
    v_current_balance Accounts.Balance%TYPE;
BEGIN
    IF :NEW.Amount IS NULL OR :NEW.Amount <= 0 THEN
        RAISE_APPLICATION_ERROR(-20050, 'Transaction amount must be positive.');
    END IF;

    IF :NEW.TransactionType = 'Withdrawal' THEN
        SELECT Balance INTO v_current_balance
          FROM Accounts
         WHERE AccountID = :NEW.AccountID;

        IF :NEW.Amount > v_current_balance THEN
            RAISE_APPLICATION_ERROR(-20051,
                'Withdrawal of ' || :NEW.Amount || ' exceeds available balance ' ||
                v_current_balance || ' for account ' || :NEW.AccountID || '.');
        END IF;
    END IF;
END CheckTransactionRules;
/

-- ---------------------------------------------------------------------
-- Test harness
-- ---------------------------------------------------------------------
SET SERVEROUTPUT ON;

PROMPT --- Test 1: Valid withdrawal (account 102 has balance 1500, withdraw 300) ---
INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
VALUES (2, 102, SYSDATE, 300, 'Withdrawal');
COMMIT;
DBMS_OUTPUT.PUT_LINE('Test 1 passed: withdrawal accepted.');

PROMPT --- Test 2: Invalid withdrawal exceeding balance (should raise ORA-20051) ---
BEGIN
    INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
    VALUES (3, 102, SYSDATE, 999999, 'Withdrawal');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Expected error caught: ' || SQLERRM);
        ROLLBACK;
END;
/

PROMPT --- Test 3: Invalid deposit with non-positive amount (should raise ORA-20050) ---
BEGIN
    INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
    VALUES (4, 101, SYSDATE, -50, 'Deposit');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Expected error caught: ' || SQLERRM);
        ROLLBACK;
END;
/

PROMPT --- Final Transactions & AuditLog ---
SELECT TransactionID, AccountID, Amount, TransactionType FROM Transactions ORDER BY TransactionID;
SELECT AuditID, TransactionID, AccountID, Amount, TransactionType FROM AuditLog ORDER BY AuditID;

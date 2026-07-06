-- =====================================================================
-- File     : 04_scenario2_LogTransaction.sql
-- Scenario : 2 - Maintain an audit log for all transactions.
-- Trigger  : LogTransaction (AFTER INSERT ON Transactions)
-- =====================================================================

CREATE OR REPLACE TRIGGER LogTransaction
AFTER INSERT ON Transactions
FOR EACH ROW
BEGIN
    INSERT INTO AuditLog (TransactionID, AccountID, Amount, TransactionType, ActionTimestamp)
    VALUES (:NEW.TransactionID, :NEW.AccountID, :NEW.Amount, :NEW.TransactionType, SYSDATE);
END LogTransaction;
/

-- ---------------------------------------------------------------------
-- Test harness
-- (relies on 05_scenario3_CheckTransactionRules.sql also being loaded;
--  run_all.sql loads scenarios in the correct order)
-- ---------------------------------------------------------------------
SET SERVEROUTPUT ON;

PROMPT --- Inserting a valid deposit transaction ---
INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
VALUES (1, 101, SYSDATE, 200, 'Deposit');
COMMIT;

PROMPT --- AuditLog contents after the insert ---
SELECT AuditID, TransactionID, AccountID, Amount, TransactionType, ActionTimestamp
FROM   AuditLog
ORDER BY AuditID;

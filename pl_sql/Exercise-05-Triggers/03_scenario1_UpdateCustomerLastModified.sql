-- =====================================================================
-- File     : 03_scenario1_UpdateCustomerLastModified.sql
-- Scenario : 1 - Automatically update the last modified date when a
--            customer's record is updated.
-- Trigger  : UpdateCustomerLastModified (BEFORE UPDATE ON Customers)
-- =====================================================================

CREATE OR REPLACE TRIGGER UpdateCustomerLastModified
BEFORE UPDATE ON Customers
FOR EACH ROW
BEGIN
    :NEW.LastModified := SYSDATE;
END UpdateCustomerLastModified;
/

-- ---------------------------------------------------------------------
-- Test harness
-- ---------------------------------------------------------------------
SET SERVEROUTPUT ON;

PROMPT --- Customer 1 BEFORE update ---
SELECT CustomerID, Name, Balance, LastModified FROM Customers WHERE CustomerID = 1;

-- Simulate a delay so LastModified visibly changes, then update the balance
UPDATE Customers SET Balance = Balance + 250 WHERE CustomerID = 1;
COMMIT;

PROMPT --- Customer 1 AFTER update (LastModified should now be "now") ---
SELECT CustomerID, Name, Balance, LastModified FROM Customers WHERE CustomerID = 1;

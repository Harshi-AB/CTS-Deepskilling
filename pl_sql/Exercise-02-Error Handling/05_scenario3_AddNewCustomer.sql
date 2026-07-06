-- =====================================================================
-- File     : 05_scenario3_AddNewCustomer.sql
-- Scenario : 3 - Ensure data integrity when adding a new customer.
-- Procedure: AddNewCustomer(p_customer_id, p_name, p_dob, p_balance)
-- Logic    : Insert a new customer. If a customer with the same ID
--            already exists (DUP_VAL_ON_INDEX), log the error and
--            prevent the insertion instead of raising to the caller.
-- =====================================================================

CREATE OR REPLACE PROCEDURE AddNewCustomer (
    p_customer_id IN Customers.CustomerID%TYPE,
    p_name        IN Customers.Name%TYPE,
    p_dob         IN Customers.DOB%TYPE,
    p_balance     IN Customers.Balance%TYPE DEFAULT 0
) IS
BEGIN
    INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastModified)
    VALUES (p_customer_id, p_name, p_dob, p_balance, SYSDATE);

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Customer ' || p_customer_id || ' (' || p_name || ') added successfully.');

EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
        ROLLBACK;
        INSERT INTO ErrorLog (ProcedureName, ErrorMessage)
        VALUES ('AddNewCustomer', 'Customer ID ' || p_customer_id || ' already exists. Insert prevented.');
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('ERROR: Customer ID ' || p_customer_id ||
                              ' already exists. See ErrorLog.');

    WHEN OTHERS THEN
        ROLLBACK;
        INSERT INTO ErrorLog (ProcedureName, ErrorMessage)
        VALUES ('AddNewCustomer', SQLERRM);
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('ERROR: Unexpected error - ' || SQLERRM || '. See ErrorLog.');
END AddNewCustomer;
/

-- ---------------------------------------------------------------------
-- Test harness
-- ---------------------------------------------------------------------
SET SERVEROUTPUT ON;

PROMPT --- Test 1: New unique customer (CustomerID 3) ---
EXEC AddNewCustomer(3, 'Michael Lee', TO_DATE('1992-02-02','YYYY-MM-DD'), 2500);

PROMPT --- Test 2: Duplicate customer (CustomerID 1 already exists) ---
EXEC AddNewCustomer(1, 'Duplicate John', TO_DATE('1980-01-01','YYYY-MM-DD'), 100);

PROMPT --- ErrorLog contents ---
SELECT ErrorLogID, ProcedureName, ErrorMessage, ErrorDate FROM ErrorLog ORDER BY ErrorLogID;

PROMPT --- Final customer data ---
SELECT CustomerID, Name, Balance FROM Customers ORDER BY CustomerID;

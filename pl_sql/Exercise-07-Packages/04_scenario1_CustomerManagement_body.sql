-- =====================================================================
-- File     : 04_scenario1_CustomerManagement_body.sql
-- Package  : CustomerManagement (body)
-- =====================================================================

CREATE OR REPLACE PACKAGE BODY CustomerManagement AS

    PROCEDURE AddCustomer (
        p_customer_id IN Customers.CustomerID%TYPE,
        p_name        IN Customers.Name%TYPE,
        p_dob         IN Customers.DOB%TYPE,
        p_balance     IN Customers.Balance%TYPE DEFAULT 0
    ) IS
    BEGIN
        INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastModified)
        VALUES (p_customer_id, p_name, p_dob, p_balance, SYSDATE);

        COMMIT;
        DBMS_OUTPUT.PUT_LINE('CustomerManagement.AddCustomer: Customer ' ||
                              p_customer_id || ' (' || p_name || ') added.');
    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            ROLLBACK;
            DBMS_OUTPUT.PUT_LINE('CustomerManagement.AddCustomer: ERROR - Customer ID ' ||
                                  p_customer_id || ' already exists.');
        WHEN OTHERS THEN
            ROLLBACK;
            DBMS_OUTPUT.PUT_LINE('CustomerManagement.AddCustomer: ERROR - ' || SQLERRM);
    END AddCustomer;


    PROCEDURE UpdateCustomerDetails (
        p_customer_id IN Customers.CustomerID%TYPE,
        p_name        IN Customers.Name%TYPE,
        p_balance     IN Customers.Balance%TYPE
    ) IS
    BEGIN
        UPDATE Customers
           SET Name = p_name,
               Balance = p_balance,
               LastModified = SYSDATE
         WHERE CustomerID = p_customer_id;

        IF SQL%ROWCOUNT = 0 THEN
            DBMS_OUTPUT.PUT_LINE('CustomerManagement.UpdateCustomerDetails: ERROR - Customer ID ' ||
                                  p_customer_id || ' not found.');
        ELSE
            COMMIT;
            DBMS_OUTPUT.PUT_LINE('CustomerManagement.UpdateCustomerDetails: Customer ' ||
                                  p_customer_id || ' updated.');
        END IF;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            DBMS_OUTPUT.PUT_LINE('CustomerManagement.UpdateCustomerDetails: ERROR - ' || SQLERRM);
    END UpdateCustomerDetails;


    FUNCTION GetCustomerBalance (
        p_customer_id IN Customers.CustomerID%TYPE
    ) RETURN NUMBER IS
        v_balance Customers.Balance%TYPE;
    BEGIN
        SELECT Balance INTO v_balance
          FROM Customers
         WHERE CustomerID = p_customer_id;

        RETURN v_balance;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RETURN NULL;
    END GetCustomerBalance;

END CustomerManagement;
/

-- ---------------------------------------------------------------------
-- Test harness
-- ---------------------------------------------------------------------
SET SERVEROUTPUT ON;

PROMPT --- Test: AddCustomer (new) ---
EXEC CustomerManagement.AddCustomer(3, 'Michael Lee', TO_DATE('1992-02-02','YYYY-MM-DD'), 2200);

PROMPT --- Test: AddCustomer (duplicate) ---
EXEC CustomerManagement.AddCustomer(1, 'Duplicate John', TO_DATE('1980-01-01','YYYY-MM-DD'), 100);

PROMPT --- Test: UpdateCustomerDetails ---
EXEC CustomerManagement.UpdateCustomerDetails(1, 'John Doe Jr.', 1800);

PROMPT --- Test: GetCustomerBalance ---
DECLARE
    v_bal NUMBER;
BEGIN
    v_bal := CustomerManagement.GetCustomerBalance(1);
    DBMS_OUTPUT.PUT_LINE('Balance for Customer 1: ' || v_bal);
END;
/

PROMPT --- Final Customers table ---
SELECT CustomerID, Name, Balance FROM Customers ORDER BY CustomerID;

-- =====================================================================
-- File     : 04_scenario2_vip_flag.sql
-- Scenario : 2 - Promote a customer to VIP status based on balance.
-- Logic    : Loop through all customers and set IsVIP = 'Y' (acting as
--            TRUE) for those with Balance > 10000, otherwise 'N'.
-- =====================================================================

SET SERVEROUTPUT ON;

DECLARE
    v_new_flag Customers.IsVIP%TYPE;
BEGIN
    FOR cust_rec IN (SELECT CustomerID, Name, Balance FROM Customers ORDER BY CustomerID) LOOP

        IF cust_rec.Balance > 10000 THEN
            v_new_flag := 'Y';
        ELSE
            v_new_flag := 'N';
        END IF;

        UPDATE Customers
           SET IsVIP = v_new_flag
         WHERE CustomerID = cust_rec.CustomerID;

        DBMS_OUTPUT.PUT_LINE('Customer ' || cust_rec.Name ||
                              ' | Balance: ' || cust_rec.Balance ||
                              ' | IsVIP set to: ' || v_new_flag);
    END LOOP;

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('--- VIP flag processing complete ---');
END;
/

-- Verification query
SELECT CustomerID, Name, Balance, IsVIP FROM Customers ORDER BY CustomerID;

-- =====================================================================
-- File     : 03_scenario1_senior_loan_discount.sql
-- Scenario : 1 - Apply a 1% discount on loan interest rates for every
--            customer whose age is above 60.
-- Logic    : Loop through all customers, compute age from DOB using
--            MONTHS_BETWEEN, and for those aged > 60 update every
--            loan belonging to them by reducing InterestRate by 1
--            percentage point (minimum 0).
-- =====================================================================

SET SERVEROUTPUT ON;

DECLARE
    v_age            NUMBER;
    v_loans_updated  NUMBER;
BEGIN
    FOR cust_rec IN (SELECT CustomerID, Name, DOB FROM Customers ORDER BY CustomerID) LOOP

        -- Calculate age in completed years
        v_age := TRUNC(MONTHS_BETWEEN(SYSDATE, cust_rec.DOB) / 12);

        IF v_age > 60 THEN
            UPDATE Loans
               SET InterestRate = GREATEST(InterestRate - 1, 0)
             WHERE CustomerID = cust_rec.CustomerID;

            v_loans_updated := SQL%ROWCOUNT;

            IF v_loans_updated > 0 THEN
                DBMS_OUTPUT.PUT_LINE('Customer ' || cust_rec.Name ||
                                      ' (Age ' || v_age || ') -> ' ||
                                      v_loans_updated || ' loan(s) discounted by 1%.');
            ELSE
                DBMS_OUTPUT.PUT_LINE('Customer ' || cust_rec.Name ||
                                      ' (Age ' || v_age || ') qualifies but has no loans.');
            END IF;
        ELSE
            DBMS_OUTPUT.PUT_LINE('Customer ' || cust_rec.Name ||
                                  ' (Age ' || v_age || ') does not qualify for discount.');
        END IF;
    END LOOP;

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('--- Senior loan discount processing complete ---');
END;
/

-- Verification query
SELECT l.LoanID, c.Name, c.DOB, l.InterestRate
FROM   Loans l
JOIN   Customers c ON c.CustomerID = l.CustomerID
ORDER BY l.LoanID;

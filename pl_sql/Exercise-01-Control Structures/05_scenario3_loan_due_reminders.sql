-- =====================================================================
-- File     : 05_scenario3_loan_due_reminders.sql
-- Scenario : 3 - Send reminders to customers whose loans are due
--            within the next 30 days.
-- Logic    : Fetch all loans whose EndDate lies between SYSDATE and
--            SYSDATE + 30, then print a reminder message per customer.
-- =====================================================================

SET SERVEROUTPUT ON;

DECLARE
    v_days_left NUMBER;
BEGIN
    FOR loan_rec IN (
        SELECT l.LoanID, l.EndDate, l.LoanAmount, c.Name, c.CustomerID
        FROM   Loans l
        JOIN   Customers c ON c.CustomerID = l.CustomerID
        WHERE  l.EndDate BETWEEN SYSDATE AND SYSDATE + 30
        ORDER BY l.EndDate
    ) LOOP
        v_days_left := TRUNC(loan_rec.EndDate - SYSDATE);

        DBMS_OUTPUT.PUT_LINE('REMINDER: Dear ' || loan_rec.Name ||
                              ', your loan #' || loan_rec.LoanID ||
                              ' of amount ' || loan_rec.LoanAmount ||
                              ' is due on ' || TO_CHAR(loan_rec.EndDate,'DD-MON-YYYY') ||
                              ' (' || v_days_left || ' day(s) remaining).');
    END LOOP;

    DBMS_OUTPUT.PUT_LINE('--- Loan due reminder processing complete ---');
END;
/

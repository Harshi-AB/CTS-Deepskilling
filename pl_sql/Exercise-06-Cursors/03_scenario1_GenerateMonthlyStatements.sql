-- =====================================================================
-- File     : 03_scenario1_GenerateMonthlyStatements.sql
-- Scenario : 1 - Generate monthly statements for all customers.
-- Logic    : Use an EXPLICIT cursor to retrieve every transaction that
--            occurred in the current calendar month, joined with
--            customer/account details, and print a statement line
--            per transaction, with a running total per customer.
-- =====================================================================

SET SERVEROUTPUT ON;

DECLARE
    -- Explicit cursor: all transactions for the current month
    CURSOR GenerateMonthlyStatements IS
        SELECT c.CustomerID, c.Name, a.AccountID, t.TransactionID,
               t.TransactionDate, t.Amount, t.TransactionType
        FROM   Transactions t
        JOIN   Accounts a  ON a.AccountID  = t.AccountID
        JOIN   Customers c ON c.CustomerID = a.CustomerID
        WHERE  t.TransactionDate >= TRUNC(SYSDATE,'MM')
          AND  t.TransactionDate <  ADD_MONTHS(TRUNC(SYSDATE,'MM'), 1)
        ORDER BY c.CustomerID, t.TransactionDate;

    v_current_customer  Customers.CustomerID%TYPE := -1;
    v_running_total     NUMBER;
BEGIN
    FOR stmt_rec IN GenerateMonthlyStatements LOOP

        IF stmt_rec.CustomerID != v_current_customer THEN
            IF v_current_customer != -1 THEN
                DBMS_OUTPUT.PUT_LINE('  ---> Net movement this month: ' || v_running_total);
                DBMS_OUTPUT.PUT_LINE('----------------------------------------');
            END IF;
            DBMS_OUTPUT.PUT_LINE('Statement for: ' || stmt_rec.Name || ' (CustomerID ' || stmt_rec.CustomerID || ')');
            v_current_customer := stmt_rec.CustomerID;
            v_running_total := 0;
        END IF;

        DBMS_OUTPUT.PUT_LINE('  ' || TO_CHAR(stmt_rec.TransactionDate,'DD-MON-YYYY') ||
                              ' | Account ' || stmt_rec.AccountID ||
                              ' | ' || RPAD(stmt_rec.TransactionType,10) ||
                              ' | Amount: ' || stmt_rec.Amount);

        IF stmt_rec.TransactionType = 'Deposit' THEN
            v_running_total := v_running_total + stmt_rec.Amount;
        ELSE
            v_running_total := v_running_total - stmt_rec.Amount;
        END IF;
    END LOOP;

    IF v_current_customer != -1 THEN
        DBMS_OUTPUT.PUT_LINE('  ---> Net movement this month: ' || v_running_total);
        DBMS_OUTPUT.PUT_LINE('----------------------------------------');
    ELSE
        DBMS_OUTPUT.PUT_LINE('No transactions found for the current month.');
    END IF;
END;
/

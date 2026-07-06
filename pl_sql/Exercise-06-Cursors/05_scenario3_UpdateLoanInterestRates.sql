-- =====================================================================
-- File     : 05_scenario3_UpdateLoanInterestRates.sql
-- Scenario : 3 - Update the interest rate for all loans based on a
--            new bank policy.
-- Logic    : Use an EXPLICIT cursor named UpdateLoanInterestRates to
--            fetch every loan and apply the new policy:
--              - Loans with InterestRate <  6%  -> increase by 0.25%
--              - Loans with InterestRate >= 10% -> decrease by 0.50%
--              - All other loans are left unchanged
-- =====================================================================

SET SERVEROUTPUT ON;

DECLARE
    CURSOR UpdateLoanInterestRates IS
        SELECT LoanID, CustomerID, InterestRate FROM Loans FOR UPDATE OF InterestRate;

    v_new_rate Loans.InterestRate%TYPE;
BEGIN
    FOR loan_rec IN UpdateLoanInterestRates LOOP

        IF loan_rec.InterestRate < 6 THEN
            v_new_rate := loan_rec.InterestRate + 0.25;
        ELSIF loan_rec.InterestRate >= 10 THEN
            v_new_rate := loan_rec.InterestRate - 0.50;
        ELSE
            v_new_rate := loan_rec.InterestRate;  -- no change
        END IF;

        UPDATE Loans
           SET InterestRate = v_new_rate
         WHERE CURRENT OF UpdateLoanInterestRates;

        DBMS_OUTPUT.PUT_LINE('Loan ' || loan_rec.LoanID ||
                              ' (Customer ' || loan_rec.CustomerID || '): ' ||
                              loan_rec.InterestRate || '% -> ' || v_new_rate || '%');
    END LOOP;

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('--- Loan interest rate policy update complete ---');
END;
/

-- Verification query
SELECT LoanID, CustomerID, InterestRate FROM Loans ORDER BY LoanID;

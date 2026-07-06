-- =====================================================================
-- File     : 04_scenario2_CalculateMonthlyInstallment.sql
-- Scenario : 2 - Compute the monthly installment (EMI) for a loan.
-- Function : CalculateMonthlyInstallment(p_amount, p_annual_rate, p_years)
--            RETURN NUMBER
-- Formula  : EMI = P * r * (1+r)^n / ((1+r)^n - 1)
--            where r = monthly interest rate, n = number of months.
--            When the rate is 0, EMI is simply Principal / n.
-- =====================================================================

CREATE OR REPLACE FUNCTION CalculateMonthlyInstallment (
    p_loan_amount     IN NUMBER,
    p_annual_rate_pct IN NUMBER,   -- e.g. 8.5 for 8.5% per annum
    p_years           IN NUMBER
) RETURN NUMBER
IS
    v_monthly_rate NUMBER;
    v_num_months   NUMBER;
    v_emi          NUMBER;
BEGIN
    IF p_loan_amount IS NULL OR p_loan_amount <= 0 THEN
        RAISE_APPLICATION_ERROR(-20040, 'Loan amount must be positive.');
    END IF;
    IF p_years IS NULL OR p_years <= 0 THEN
        RAISE_APPLICATION_ERROR(-20041, 'Loan duration must be positive.');
    END IF;

    v_num_months := p_years * 12;

    IF p_annual_rate_pct IS NULL OR p_annual_rate_pct = 0 THEN
        v_emi := p_loan_amount / v_num_months;
    ELSE
        v_monthly_rate := (p_annual_rate_pct / 100) / 12;
        v_emi := p_loan_amount * v_monthly_rate * POWER(1 + v_monthly_rate, v_num_months)
                 / (POWER(1 + v_monthly_rate, v_num_months) - 1);
    END IF;

    RETURN ROUND(v_emi, 2);
END CalculateMonthlyInstallment;
/

-- ---------------------------------------------------------------------
-- Test harness
-- ---------------------------------------------------------------------
SET SERVEROUTPUT ON;

BEGIN
    DBMS_OUTPUT.PUT_LINE('EMI for 500000 @ 8.5% for 5 years: ' ||
                          CalculateMonthlyInstallment(500000, 8.5, 5));
    DBMS_OUTPUT.PUT_LINE('EMI for 120000 @ 0%   for 2 years: ' ||
                          CalculateMonthlyInstallment(120000, 0, 2));
    DBMS_OUTPUT.PUT_LINE('EMI for 1000000 @ 7.25% for 20 years: ' ||
                          CalculateMonthlyInstallment(1000000, 7.25, 20));
END;
/

-- =====================================================================
-- File     : 03_scenario1_CalculateAge.sql
-- Scenario : 1 - Calculate the age of customers for eligibility checks.
-- Function : CalculateAge(p_dob) RETURN NUMBER
-- =====================================================================

CREATE OR REPLACE FUNCTION CalculateAge (
    p_dob IN DATE
) RETURN NUMBER
IS
    v_age NUMBER;
BEGIN
    IF p_dob IS NULL THEN
        RAISE_APPLICATION_ERROR(-20030, 'Date of birth cannot be NULL.');
    END IF;

    IF p_dob > SYSDATE THEN
        RAISE_APPLICATION_ERROR(-20031, 'Date of birth cannot be in the future.');
    END IF;

    v_age := TRUNC(MONTHS_BETWEEN(SYSDATE, p_dob) / 12);
    RETURN v_age;
END CalculateAge;
/

-- ---------------------------------------------------------------------
-- Test harness
-- ---------------------------------------------------------------------
SET SERVEROUTPUT ON;

PROMPT --- Age for every customer ---
SELECT CustomerID, Name, DOB, CalculateAge(DOB) AS Age
FROM   Customers
ORDER BY CustomerID;

PROMPT --- Direct function calls ---
BEGIN
    DBMS_OUTPUT.PUT_LINE('Age for DOB 1960-01-01: ' || CalculateAge(TO_DATE('1960-01-01','YYYY-MM-DD')));
    DBMS_OUTPUT.PUT_LINE('Age for DOB 2005-12-31: ' || CalculateAge(TO_DATE('2005-12-31','YYYY-MM-DD')));
END;
/

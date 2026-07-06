-- =====================================================================
-- File     : 04_scenario2_UpdateEmployeeBonus.sql
-- Scenario : 2 - Implement a bonus scheme for employees based on
--            performance, per department.
-- Procedure: UpdateEmployeeBonus(p_department, p_bonus_percentage)
-- Logic    : Add the bonus percentage to the salary of every employee
--            belonging to the given department.
-- =====================================================================

CREATE OR REPLACE PROCEDURE UpdateEmployeeBonus (
    p_department        IN Employees.Department%TYPE,
    p_bonus_percentage  IN NUMBER
) IS
    v_rows_updated NUMBER := 0;
BEGIN
    IF p_bonus_percentage IS NULL OR p_bonus_percentage < 0 THEN
        RAISE_APPLICATION_ERROR(-20010, 'Bonus percentage must be a non-negative number.');
    END IF;

    UPDATE Employees
       SET Salary = Salary + (Salary * p_bonus_percentage / 100)
     WHERE Department = p_department;

    v_rows_updated := SQL%ROWCOUNT;

    IF v_rows_updated = 0 THEN
        DBMS_OUTPUT.PUT_LINE('No employees found in department: ' || p_department);
    ELSE
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('Bonus of ' || p_bonus_percentage || '% applied to ' ||
                              v_rows_updated || ' employee(s) in department ' || p_department || '.');
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('ERROR in UpdateEmployeeBonus: ' || SQLERRM);
END UpdateEmployeeBonus;
/

-- ---------------------------------------------------------------------
-- Test harness
-- ---------------------------------------------------------------------
SET SERVEROUTPUT ON;

PROMPT --- Salaries BEFORE bonus (IT department) ---
SELECT EmployeeID, Name, Department, Salary FROM Employees WHERE Department = 'IT' ORDER BY EmployeeID;

PROMPT --- Running UpdateEmployeeBonus('IT', 5) ---
EXEC UpdateEmployeeBonus('IT', 5);

PROMPT --- Salaries AFTER bonus (IT department) ---
SELECT EmployeeID, Name, Department, Salary FROM Employees WHERE Department = 'IT' ORDER BY EmployeeID;

PROMPT --- Department with no employees ---
EXEC UpdateEmployeeBonus('Finance', 5);

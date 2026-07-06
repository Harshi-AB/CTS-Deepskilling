-- =====================================================================
-- File     : 04_scenario2_UpdateSalary.sql
-- Scenario : 2 - Manage errors when updating employee salaries.
-- Procedure: UpdateSalary(p_employee_id, p_percentage)
-- Logic    : Increase the salary of the given employee by the given
--            percentage. If the employee does not exist, catch the
--            exception, log it, and inform the caller instead of
--            letting the error propagate.
-- =====================================================================

CREATE OR REPLACE PROCEDURE UpdateSalary (
    p_employee_id IN Employees.EmployeeID%TYPE,
    p_percentage  IN NUMBER
) IS
    v_old_salary Employees.Salary%TYPE;
    v_new_salary Employees.Salary%TYPE;
BEGIN
    -- This raises NO_DATA_FOUND automatically if the employee does not exist
    SELECT Salary INTO v_old_salary
      FROM Employees
     WHERE EmployeeID = p_employee_id;

    v_new_salary := v_old_salary * (1 + p_percentage / 100);

    UPDATE Employees
       SET Salary = v_new_salary
     WHERE EmployeeID = p_employee_id;

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Employee ' || p_employee_id || ' salary updated from ' ||
                          v_old_salary || ' to ' || v_new_salary || '.');

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        INSERT INTO ErrorLog (ProcedureName, ErrorMessage)
        VALUES ('UpdateSalary', 'Employee ID ' || p_employee_id || ' does not exist.');
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('ERROR: Employee ID ' || p_employee_id ||
                              ' does not exist. See ErrorLog.');

    WHEN OTHERS THEN
        ROLLBACK;
        INSERT INTO ErrorLog (ProcedureName, ErrorMessage)
        VALUES ('UpdateSalary', SQLERRM);
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('ERROR: Unexpected error - ' || SQLERRM || '. See ErrorLog.');
END UpdateSalary;
/

-- ---------------------------------------------------------------------
-- Test harness
-- ---------------------------------------------------------------------
SET SERVEROUTPUT ON;

PROMPT --- Test 1: Valid employee (EmployeeID 1, +10%) ---
EXEC UpdateSalary(1, 10);

PROMPT --- Test 2: Non-existent employee (EmployeeID 999, +10%) ---
EXEC UpdateSalary(999, 10);

PROMPT --- ErrorLog contents ---
SELECT ErrorLogID, ProcedureName, ErrorMessage, ErrorDate FROM ErrorLog ORDER BY ErrorLogID;

PROMPT --- Final employee data ---
SELECT EmployeeID, Name, Salary FROM Employees ORDER BY EmployeeID;

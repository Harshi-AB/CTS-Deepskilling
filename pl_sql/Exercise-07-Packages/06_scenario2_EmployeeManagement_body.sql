-- =====================================================================
-- File     : 06_scenario2_EmployeeManagement_body.sql
-- Package  : EmployeeManagement (body)
-- =====================================================================

CREATE OR REPLACE PACKAGE BODY EmployeeManagement AS

    PROCEDURE HireEmployee (
        p_employee_id IN Employees.EmployeeID%TYPE,
        p_name        IN Employees.Name%TYPE,
        p_position    IN Employees.Position%TYPE,
        p_salary      IN Employees.Salary%TYPE,
        p_department  IN Employees.Department%TYPE
    ) IS
    BEGIN
        INSERT INTO Employees (EmployeeID, Name, Position, Salary, Department, HireDate)
        VALUES (p_employee_id, p_name, p_position, p_salary, p_department, SYSDATE);

        COMMIT;
        DBMS_OUTPUT.PUT_LINE('EmployeeManagement.HireEmployee: Employee ' ||
                              p_employee_id || ' (' || p_name || ') hired into ' || p_department || '.');
    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            ROLLBACK;
            DBMS_OUTPUT.PUT_LINE('EmployeeManagement.HireEmployee: ERROR - Employee ID ' ||
                                  p_employee_id || ' already exists.');
        WHEN OTHERS THEN
            ROLLBACK;
            DBMS_OUTPUT.PUT_LINE('EmployeeManagement.HireEmployee: ERROR - ' || SQLERRM);
    END HireEmployee;


    PROCEDURE UpdateEmployeeDetails (
        p_employee_id IN Employees.EmployeeID%TYPE,
        p_position    IN Employees.Position%TYPE,
        p_salary      IN Employees.Salary%TYPE,
        p_department  IN Employees.Department%TYPE
    ) IS
    BEGIN
        UPDATE Employees
           SET Position = p_position,
               Salary = p_salary,
               Department = p_department
         WHERE EmployeeID = p_employee_id;

        IF SQL%ROWCOUNT = 0 THEN
            DBMS_OUTPUT.PUT_LINE('EmployeeManagement.UpdateEmployeeDetails: ERROR - Employee ID ' ||
                                  p_employee_id || ' not found.');
        ELSE
            COMMIT;
            DBMS_OUTPUT.PUT_LINE('EmployeeManagement.UpdateEmployeeDetails: Employee ' ||
                                  p_employee_id || ' updated.');
        END IF;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            DBMS_OUTPUT.PUT_LINE('EmployeeManagement.UpdateEmployeeDetails: ERROR - ' || SQLERRM);
    END UpdateEmployeeDetails;


    FUNCTION CalculateAnnualSalary (
        p_employee_id IN Employees.EmployeeID%TYPE
    ) RETURN NUMBER IS
        v_monthly_salary Employees.Salary%TYPE;
    BEGIN
        SELECT Salary INTO v_monthly_salary
          FROM Employees
         WHERE EmployeeID = p_employee_id;

        RETURN v_monthly_salary * 12;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RETURN NULL;
    END CalculateAnnualSalary;

END EmployeeManagement;
/

-- ---------------------------------------------------------------------
-- Test harness
-- ---------------------------------------------------------------------
SET SERVEROUTPUT ON;

PROMPT --- Test: HireEmployee (new) ---
EXEC EmployeeManagement.HireEmployee(2, 'Bob Brown', 'Developer', 60000, 'IT');

PROMPT --- Test: HireEmployee (duplicate) ---
EXEC EmployeeManagement.HireEmployee(1, 'Duplicate Alice', 'Manager', 1, 'HR');

PROMPT --- Test: UpdateEmployeeDetails ---
EXEC EmployeeManagement.UpdateEmployeeDetails(2, 'Senior Developer', 68000, 'IT');

PROMPT --- Test: CalculateAnnualSalary ---
DECLARE
    v_annual NUMBER;
BEGIN
    v_annual := EmployeeManagement.CalculateAnnualSalary(2);
    DBMS_OUTPUT.PUT_LINE('Annual salary for Employee 2: ' || v_annual);
END;
/

PROMPT --- Final Employees table ---
SELECT EmployeeID, Name, Position, Salary, Department FROM Employees ORDER BY EmployeeID;

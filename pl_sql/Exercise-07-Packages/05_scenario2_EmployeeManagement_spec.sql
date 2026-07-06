-- =====================================================================
-- File     : 05_scenario2_EmployeeManagement_spec.sql
-- Scenario : 2 - Create a package to manage employee data.
-- Package  : EmployeeManagement (specification)
-- =====================================================================

CREATE OR REPLACE PACKAGE EmployeeManagement AS

    -- Hires a new employee. Raises a friendly error on duplicate ID.
    PROCEDURE HireEmployee (
        p_employee_id IN Employees.EmployeeID%TYPE,
        p_name        IN Employees.Name%TYPE,
        p_position    IN Employees.Position%TYPE,
        p_salary      IN Employees.Salary%TYPE,
        p_department  IN Employees.Department%TYPE
    );

    -- Updates position, salary and department for an existing employee.
    PROCEDURE UpdateEmployeeDetails (
        p_employee_id IN Employees.EmployeeID%TYPE,
        p_position    IN Employees.Position%TYPE,
        p_salary      IN Employees.Salary%TYPE,
        p_department  IN Employees.Department%TYPE
    );

    -- Returns the projected annual salary (monthly salary * 12).
    FUNCTION CalculateAnnualSalary (
        p_employee_id IN Employees.EmployeeID%TYPE
    ) RETURN NUMBER;

END EmployeeManagement;
/

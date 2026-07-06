-- =====================================================================
-- File     : 02_seed_data.sql
-- Exercise : 03 - Stored Procedures
-- =====================================================================

INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastModified)
VALUES (1, 'John Doe', TO_DATE('1985-05-15','YYYY-MM-DD'), 1000, SYSDATE);
INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastModified)
VALUES (2, 'Jane Smith', TO_DATE('1990-07-20','YYYY-MM-DD'), 1500, SYSDATE);

INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastModified)
VALUES (101, 1, 'Savings', 1000, SYSDATE);
INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastModified)
VALUES (102, 2, 'Savings', 2000, SYSDATE);
INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastModified)
VALUES (103, 1, 'Checking', 500, SYSDATE);   -- should NOT be touched by interest processing

INSERT INTO Employees (EmployeeID, Name, Position, Salary, Department, HireDate)
VALUES (1, 'Alice Johnson', 'Manager', 70000, 'HR', TO_DATE('2015-06-15','YYYY-MM-DD'));
INSERT INTO Employees (EmployeeID, Name, Position, Salary, Department, HireDate)
VALUES (2, 'Bob Brown', 'Developer', 60000, 'IT', TO_DATE('2017-03-20','YYYY-MM-DD'));
INSERT INTO Employees (EmployeeID, Name, Position, Salary, Department, HireDate)
VALUES (3, 'Carol White', 'Analyst', 55000, 'IT', TO_DATE('2019-09-01','YYYY-MM-DD'));

COMMIT;

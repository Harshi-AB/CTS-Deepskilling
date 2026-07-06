-- =====================================================================
-- File     : 02_seed_data.sql
-- Exercise : 07 - Packages
-- =====================================================================

INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastModified)
VALUES (1, 'John Doe', TO_DATE('1985-05-15','YYYY-MM-DD'), 1000, SYSDATE);
INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastModified)
VALUES (2, 'Jane Smith', TO_DATE('1990-07-20','YYYY-MM-DD'), 1500, SYSDATE);

INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, Status, LastModified)
VALUES (101, 1, 'Savings', 1000, 'OPEN', SYSDATE);
INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, Status, LastModified)
VALUES (102, 1, 'Checking', 500, 'OPEN', SYSDATE);
INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, Status, LastModified)
VALUES (103, 2, 'Savings', 1500, 'OPEN', SYSDATE);

INSERT INTO Employees (EmployeeID, Name, Position, Salary, Department, HireDate)
VALUES (1, 'Alice Johnson', 'Manager', 70000, 'HR', TO_DATE('2015-06-15','YYYY-MM-DD'));

COMMIT;

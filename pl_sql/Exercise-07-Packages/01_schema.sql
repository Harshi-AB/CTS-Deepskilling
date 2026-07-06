-- =====================================================================
-- File     : 01_schema.sql
-- Exercise : 07 - Packages
-- =====================================================================

BEGIN EXECUTE IMMEDIATE 'DROP TABLE Accounts CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE Employees CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE Customers CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/

CREATE TABLE Customers (
    CustomerID    NUMBER PRIMARY KEY,
    Name          VARCHAR2(100) NOT NULL,
    DOB           DATE,
    Balance       NUMBER(12,2) DEFAULT 0,
    LastModified  DATE DEFAULT SYSDATE
);

CREATE TABLE Accounts (
    AccountID     NUMBER PRIMARY KEY,
    CustomerID    NUMBER NOT NULL,
    AccountType   VARCHAR2(20),
    Balance       NUMBER(12,2) DEFAULT 0,
    Status        VARCHAR2(10) DEFAULT 'OPEN' CHECK (Status IN ('OPEN','CLOSED')),
    LastModified  DATE DEFAULT SYSDATE,
    CONSTRAINT fk_acc_cust FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID)
);

CREATE TABLE Employees (
    EmployeeID   NUMBER PRIMARY KEY,
    Name         VARCHAR2(100) NOT NULL,
    Position     VARCHAR2(50),
    Salary       NUMBER(12,2),
    Department   VARCHAR2(50),
    HireDate     DATE DEFAULT SYSDATE
);

COMMIT;

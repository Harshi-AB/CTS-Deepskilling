-- =====================================================================
-- File        : 01_schema.sql
-- Exercise    : 01 - Control Structures
-- Purpose     : Creates all tables required for Exercise 1.
-- Database    : Oracle Database (Core PL/SQL / SQL only)
-- Notes       : Safe to re-run. Existing objects are dropped first
--               (errors are swallowed if the object does not exist).
-- =====================================================================

-- ---------------------------------------------------------------------
-- Drop existing objects (child -> parent order) so the script is
-- re-runnable without manual cleanup.
-- ---------------------------------------------------------------------
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE Loans CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN RAISE; END IF;   -- -942 = table does not exist
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE Accounts CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN RAISE; END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE Customers CASCADE CONSTRAINTS';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN RAISE; END IF;
END;
/

-- ---------------------------------------------------------------------
-- Table: Customers
--   IsVIP column added (needed for Scenario 2 of this exercise).
-- ---------------------------------------------------------------------
CREATE TABLE Customers (
    CustomerID    NUMBER PRIMARY KEY,
    Name          VARCHAR2(100)  NOT NULL,
    DOB           DATE           NOT NULL,
    Balance       NUMBER(12,2)   DEFAULT 0,
    LastModified  DATE           DEFAULT SYSDATE,
    IsVIP         CHAR(1)        DEFAULT 'N' CHECK (IsVIP IN ('Y','N'))
);

-- ---------------------------------------------------------------------
-- Table: Accounts
-- ---------------------------------------------------------------------
CREATE TABLE Accounts (
    AccountID     NUMBER PRIMARY KEY,
    CustomerID    NUMBER NOT NULL,
    AccountType   VARCHAR2(20),
    Balance       NUMBER(12,2) DEFAULT 0,
    LastModified  DATE DEFAULT SYSDATE,
    CONSTRAINT fk_acc_cust FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID)
);

-- ---------------------------------------------------------------------
-- Table: Loans
-- ---------------------------------------------------------------------
CREATE TABLE Loans (
    LoanID        NUMBER PRIMARY KEY,
    CustomerID    NUMBER NOT NULL,
    LoanAmount    NUMBER(12,2),
    InterestRate  NUMBER(5,2),
    StartDate     DATE,
    EndDate       DATE,
    CONSTRAINT fk_loan_cust FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID)
);

COMMIT;

-- =====================================================================
-- File     : 01_schema.sql
-- Exercise : 05 - Triggers
-- =====================================================================

BEGIN EXECUTE IMMEDIATE 'DROP TABLE AuditLog CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE Transactions CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE Accounts CASCADE CONSTRAINTS';
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
    LastModified  DATE DEFAULT SYSDATE,
    CONSTRAINT fk_acc_cust FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID)
);

CREATE TABLE Transactions (
    TransactionID     NUMBER PRIMARY KEY,
    AccountID         NUMBER NOT NULL,
    TransactionDate   DATE DEFAULT SYSDATE,
    Amount            NUMBER(12,2),
    TransactionType   VARCHAR2(10) CHECK (TransactionType IN ('Deposit','Withdrawal')),
    CONSTRAINT fk_txn_acc FOREIGN KEY (AccountID) REFERENCES Accounts(AccountID)
);

-- Audit table populated by the LogTransaction trigger
CREATE TABLE AuditLog (
    AuditID           NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    TransactionID     NUMBER,
    AccountID         NUMBER,
    Amount            NUMBER(12,2),
    TransactionType   VARCHAR2(10),
    ActionTimestamp   DATE DEFAULT SYSDATE
);

COMMIT;

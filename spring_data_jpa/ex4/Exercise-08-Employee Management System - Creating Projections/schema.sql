-- =====================================================================
-- Exercise 07 - Enabling Entity Auditing
-- (adds created_at / updated_at columns for @CreatedDate / @LastModifiedDate)
-- =====================================================================
CREATE DATABASE IF NOT EXISTS employee_db;
USE employee_db;

CREATE TABLE IF NOT EXISTS department (
    dept_id   INT AUTO_INCREMENT PRIMARY KEY,
    dept_name VARCHAR(100) NOT NULL UNIQUE,
    location  VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS employee (
    emp_id          INT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    email           VARCHAR(150) UNIQUE,
    salary          DECIMAL(10,2),
    date_of_joining DATE,
    active          BOOLEAN DEFAULT TRUE,
    dept_id         INT,
    created_at      DATETIME,
    updated_at      DATETIME,
    CONSTRAINT fk_employee_department FOREIGN KEY (dept_id) REFERENCES department(dept_id)
);

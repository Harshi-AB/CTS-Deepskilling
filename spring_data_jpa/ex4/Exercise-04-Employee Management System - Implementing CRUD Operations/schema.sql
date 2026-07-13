-- =====================================================================
-- Exercise 02 - Creating Entities
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
    CONSTRAINT fk_employee_department FOREIGN KEY (dept_id) REFERENCES department(dept_id)
);

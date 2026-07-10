-- ============================================================
-- schema.sql
-- Core MySQL schema + sample data for the Employee / Department
-- / Skill exercises (Hands-on 1, 2, 4 and 5).
-- Run this file once in MySQL before running any of these projects:
--     mysql -u root -p < schema.sql
-- ============================================================

CREATE DATABASE IF NOT EXISTS ormlearn_criteria;
USE ormlearn_criteria;

-- Drop tables if they already exist (clean re-run)
DROP TABLE IF EXISTS employee_skill;
DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS department;
DROP TABLE IF EXISTS skill;

-- ---------------------------------------------------------
-- department: master data
-- ---------------------------------------------------------
CREATE TABLE department (
    dp_id   INT PRIMARY KEY AUTO_INCREMENT,
    dp_name VARCHAR(100) NOT NULL
);

-- ---------------------------------------------------------
-- employee: em_permanent = 1 -> permanent employee, 0 -> contract employee
-- ---------------------------------------------------------
CREATE TABLE employee (
    em_id             INT PRIMARY KEY AUTO_INCREMENT,
    em_name           VARCHAR(100) NOT NULL,
    em_date_of_birth  DATE,
    em_salary         DOUBLE,
    em_permanent      TINYINT(1) DEFAULT 0,
    em_dp_id          INT,
    CONSTRAINT fk_employee_department FOREIGN KEY (em_dp_id) REFERENCES department(dp_id)
);

-- ---------------------------------------------------------
-- skill: master data
-- ---------------------------------------------------------
CREATE TABLE skill (
    sk_id   INT PRIMARY KEY AUTO_INCREMENT,
    sk_name VARCHAR(100) NOT NULL
);

-- ---------------------------------------------------------
-- employee_skill: many-to-many join table between employee and skill
-- ---------------------------------------------------------
CREATE TABLE employee_skill (
    es_em_id INT NOT NULL,
    es_sk_id INT NOT NULL,
    PRIMARY KEY (es_em_id, es_sk_id),
    CONSTRAINT fk_es_employee FOREIGN KEY (es_em_id) REFERENCES employee(em_id),
    CONSTRAINT fk_es_skill    FOREIGN KEY (es_sk_id) REFERENCES skill(sk_id)
);

-- ---------------------------------------------------------
-- Sample data
-- ---------------------------------------------------------
INSERT INTO department (dp_id, dp_name) VALUES
    (1, 'Engineering'),
    (2, 'Human Resources'),
    (3, 'Finance');

INSERT INTO employee (em_id, em_name, em_date_of_birth, em_salary, em_permanent, em_dp_id) VALUES
    (1, 'Arun Kumar',     '1990-05-14', 65000.00, 1, 1),
    (2, 'Bhavana Reddy',  '1988-11-02', 72000.00, 1, 1),
    (3, 'Charles Mathew', '1995-02-20', 45000.00, 0, 3),
    (4, 'Divya Nair',     '1992-07-09', 58000.00, 1, 2),
    (5, 'Eshan Verma',    '1997-09-30', 40000.00, 0, 1),
    (6, 'Farida Sheikh',  '1991-01-17', 61000.00, 1, 3);

INSERT INTO skill (sk_id, sk_name) VALUES
    (1, 'Java'),
    (2, 'SQL'),
    (3, 'Spring Data JPA'),
    (4, 'Hibernate'),
    (5, 'MySQL Administration');

INSERT INTO employee_skill (es_em_id, es_sk_id) VALUES
    (1, 1), (1, 3), (1, 4),
    (2, 2), (2, 5),
    (4, 2),
    (6, 1), (6, 2);

-- ============================================================
-- Product table + sample data for the retail "laptop search" Criteria
-- Query scenario (Hands-on 6).
-- ============================================================

DROP TABLE IF EXISTS product;

CREATE TABLE product (
    product_id        INT PRIMARY KEY AUTO_INCREMENT,
    product_name      VARCHAR(150) NOT NULL,
    customer_review   INT,
    hard_disk_size_gb INT,
    ram_size_gb       INT,
    cpu_speed_ghz     DOUBLE,
    operating_system  VARCHAR(50),
    weight_kg         DOUBLE,
    cpu               VARCHAR(50),
    price             DOUBLE
);

INSERT INTO product
    (product_id, product_name, customer_review, hard_disk_size_gb, ram_size_gb,
     cpu_speed_ghz, operating_system, weight_kg, cpu, price) VALUES
    (1, 'AlphaBook Air 14',   4, 512,  8, 2.4, 'Windows 11', 1.30, 'Intel i5', 62999.00),
    (2, 'ProNote X1',         5, 1024, 16, 3.0, 'Windows 11', 1.60, 'Intel i7', 94999.00),
    (3, 'BudgetBook Lite',    3, 256,  4, 1.8, 'Windows 10', 1.80, 'Intel i3', 34999.00),
    (4, 'MacStyle Pro 13',    5, 512,  8, 2.6, 'macOS',      1.40, 'Apple M2', 118999.00),
    (5, 'PenguinBook Dev',    4, 512, 16, 2.8, 'Ubuntu',     1.50, 'AMD Ryzen 7', 79999.00);

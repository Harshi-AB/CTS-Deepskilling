-- Exercise 03: payroll schema (employee, department, skill, employee_skill).
-- Run with:  mysql -u root -p ormlearn < payroll.sql
--
-- This sets up the schema shared by Hands-on 3, 4, 5 and 6: many-to-one
-- (Employee -> Department), one-to-many (Department -> Employees) and
-- many-to-many (Employee <-> Skill).

CREATE DATABASE IF NOT EXISTS ormlearn;
USE ormlearn;

DROP TABLE IF EXISTS employee_skill;
DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS skill;
DROP TABLE IF EXISTS department;

CREATE TABLE department (
  dp_id   INT NOT NULL AUTO_INCREMENT,
  dp_name VARCHAR(50),
  PRIMARY KEY (dp_id)
);

CREATE TABLE skill (
  sk_id   INT NOT NULL AUTO_INCREMENT,
  sk_name VARCHAR(50),
  PRIMARY KEY (sk_id)
);

CREATE TABLE employee (
  em_id             INT NOT NULL AUTO_INCREMENT,
  em_name           VARCHAR(50),
  em_salary         DOUBLE,
  em_permanent      BOOLEAN,
  em_date_of_birth  DATE,
  em_dp_id          INT,
  PRIMARY KEY (em_id),
  CONSTRAINT fk_employee_department FOREIGN KEY (em_dp_id) REFERENCES department(dp_id)
);

CREATE TABLE employee_skill (
  es_em_id INT NOT NULL,
  es_sk_id INT NOT NULL,
  PRIMARY KEY (es_em_id, es_sk_id),
  CONSTRAINT fk_es_employee FOREIGN KEY (es_em_id) REFERENCES employee(em_id),
  CONSTRAINT fk_es_skill    FOREIGN KEY (es_sk_id) REFERENCES skill(sk_id)
);

-- sample seed data
INSERT INTO department (dp_name) VALUES ('Engineering'), ('Human Resources'), ('Finance');

INSERT INTO skill (sk_name) VALUES ('Java'), ('SQL'), ('Communication'), ('Project Management');

INSERT INTO employee (em_name, em_salary, em_permanent, em_date_of_birth, em_dp_id) VALUES
('Alice Johnson', 75000.00, TRUE,  '1990-05-10', 1),
('Bob Smith',     65000.00, FALSE, '1992-08-21', 1),
('Carol White',   58000.00, TRUE,  '1988-02-14', 2),
('David Brown',   72000.00, TRUE,  '1985-11-30', 3);

INSERT INTO employee_skill (es_em_id, es_sk_id) VALUES
(1,1),(1,2),(2,1),(3,3),(4,2),(4,4);

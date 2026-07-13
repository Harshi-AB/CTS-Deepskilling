-- =====================================================================
-- Exercise 01 - Overview and Setup
-- Run this once in MySQL before starting the application.
-- =====================================================================
CREATE DATABASE IF NOT EXISTS employee_db;
USE employee_db;

-- A minimal table just to prove end-to-end connectivity.
-- Later exercises will replace/extend this with the full Employee entity.
CREATE TABLE IF NOT EXISTS connection_test (
    id INT PRIMARY KEY AUTO_INCREMENT,
    message VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

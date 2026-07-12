-- Exercise 01: country table schema.
-- Run with:  mysql -u root -p ormlearn < schema.sql

CREATE DATABASE IF NOT EXISTS ormlearn;
USE ormlearn;

DROP TABLE IF EXISTS country;

CREATE TABLE country (
  co_code VARCHAR(2)   NOT NULL,
  co_name VARCHAR(100) NOT NULL,
  PRIMARY KEY (co_code)
);

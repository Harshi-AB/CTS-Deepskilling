-- Exercise 02: stock table schema.
-- Run with:  mysql -u root -p ormlearn < schema.sql

CREATE DATABASE IF NOT EXISTS ormlearn;
USE ormlearn;

DROP TABLE IF EXISTS stock;

CREATE TABLE IF NOT EXISTS `ormlearn`.`stock` (
  `st_id` INT NOT NULL AUTO_INCREMENT,
  `st_code` varchar(10),
  `st_date` date,
  `st_open` numeric(10,2),
  `st_close` numeric(10,2),
  `st_volume` numeric,
  PRIMARY KEY (`st_id`)
);

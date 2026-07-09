-- ============================================================
-- Exercise 1: Spring Data JPA - Quick Example
-- Run this in MySQL Workbench / mysql client against a real MySQL 8.0
-- instance to reproduce the schema + data this project simulates in memory.
-- ============================================================

create schema if not exists ormlearn;
use ormlearn;

drop table if exists country;

create table country (
    co_code varchar(2) primary key,
    co_name varchar(50)
);

insert into country values ('IN', 'India');
insert into country values ('US', 'United States of America');

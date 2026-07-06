-- =====================================================================
-- File   : run_all.sql
-- Purpose: Master script - builds schema, loads data and runs every
--          scenario for Exercise 01 in one shot.
-- Usage  : sqlplus user/password@db @run_all.sql
-- =====================================================================
SET SERVEROUTPUT ON SIZE UNLIMITED;
SET ECHO ON;

@@01_schema.sql
@@02_seed_data.sql
@@03_scenario1_senior_loan_discount.sql
@@04_scenario2_vip_flag.sql
@@05_scenario3_loan_due_reminders.sql

PROMPT ================ EXERCISE 01 COMPLETE ================

-- =====================================================================
-- File   : run_all.sql
-- Purpose: Builds schema, loads data and runs every scenario for
--          Exercise 02 in one shot.
-- Usage  : sqlplus user/password@db @run_all.sql
-- =====================================================================
SET SERVEROUTPUT ON SIZE UNLIMITED;
SET ECHO ON;

@@01_schema.sql
@@02_seed_data.sql
@@03_scenario1_SafeTransferFunds.sql
@@04_scenario2_UpdateSalary.sql
@@05_scenario3_AddNewCustomer.sql

PROMPT ================ EXERCISE 02 COMPLETE ================

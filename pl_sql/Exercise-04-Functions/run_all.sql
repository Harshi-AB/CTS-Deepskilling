-- =====================================================================
-- File   : run_all.sql
-- Usage  : sqlplus user/password@db @run_all.sql
-- =====================================================================
SET SERVEROUTPUT ON SIZE UNLIMITED;
SET ECHO ON;

@@01_schema.sql
@@02_seed_data.sql
@@03_scenario1_CalculateAge.sql
@@04_scenario2_CalculateMonthlyInstallment.sql
@@05_scenario3_HasSufficientBalance.sql

PROMPT ================ EXERCISE 04 COMPLETE ================

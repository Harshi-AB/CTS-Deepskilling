-- =====================================================================
-- File   : run_all.sql
-- Usage  : sqlplus user/password@db @run_all.sql
-- =====================================================================
SET SERVEROUTPUT ON SIZE UNLIMITED;
SET ECHO ON;

@@01_schema.sql
@@02_seed_data.sql
@@03_scenario1_CustomerManagement_spec.sql
@@04_scenario1_CustomerManagement_body.sql
@@05_scenario2_EmployeeManagement_spec.sql
@@06_scenario2_EmployeeManagement_body.sql
@@07_scenario3_AccountOperations_spec.sql
@@08_scenario3_AccountOperations_body.sql

PROMPT ================ EXERCISE 07 COMPLETE ================

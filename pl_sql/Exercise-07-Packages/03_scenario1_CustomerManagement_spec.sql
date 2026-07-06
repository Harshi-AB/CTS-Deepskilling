-- =====================================================================
-- File     : 03_scenario1_CustomerManagement_spec.sql
-- Scenario : 1 - Group all customer-related procedures/functions into
--            a package.
-- Package  : CustomerManagement (specification)
-- =====================================================================

CREATE OR REPLACE PACKAGE CustomerManagement AS

    -- Adds a brand new customer. Raises a friendly error if the
    -- CustomerID already exists.
    PROCEDURE AddCustomer (
        p_customer_id IN Customers.CustomerID%TYPE,
        p_name        IN Customers.Name%TYPE,
        p_dob         IN Customers.DOB%TYPE,
        p_balance     IN Customers.Balance%TYPE DEFAULT 0
    );

    -- Updates the mutable details of an existing customer.
    PROCEDURE UpdateCustomerDetails (
        p_customer_id IN Customers.CustomerID%TYPE,
        p_name        IN Customers.Name%TYPE,
        p_balance     IN Customers.Balance%TYPE
    );

    -- Returns the current balance for a customer.
    FUNCTION GetCustomerBalance (
        p_customer_id IN Customers.CustomerID%TYPE
    ) RETURN NUMBER;

END CustomerManagement;
/

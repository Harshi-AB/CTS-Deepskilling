-- =====================================================================
-- File     : 07_scenario3_AccountOperations_spec.sql
-- Scenario : 3 - Group all account-related operations into a package.
-- Package  : AccountOperations (specification)
-- =====================================================================

CREATE OR REPLACE PACKAGE AccountOperations AS

    -- Opens a new account for an existing customer.
    PROCEDURE OpenAccount (
        p_account_id     IN Accounts.AccountID%TYPE,
        p_customer_id    IN Accounts.CustomerID%TYPE,
        p_account_type   IN Accounts.AccountType%TYPE,
        p_initial_balance IN Accounts.Balance%TYPE DEFAULT 0
    );

    -- Closes an account (marks Status = 'CLOSED'); refuses to close an
    -- account that still holds a positive balance.
    PROCEDURE CloseAccount (
        p_account_id IN Accounts.AccountID%TYPE
    );

    -- Returns the sum of balances across every OPEN account owned by
    -- the given customer.
    FUNCTION GetTotalBalance (
        p_customer_id IN Accounts.CustomerID%TYPE
    ) RETURN NUMBER;

END AccountOperations;
/

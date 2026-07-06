-- =====================================================================
-- File     : 04_scenario2_ApplyAnnualFee.sql
-- Scenario : 2 - Apply annual maintenance fee to all accounts.
-- Logic    : Use an EXPLICIT cursor named ApplyAnnualFee to iterate
--            over every account and deduct a flat annual fee from its
--            balance. If a balance would go negative, the fee is
--            capped so the balance floors at zero and a warning is
--            printed.
-- =====================================================================

SET SERVEROUTPUT ON;

DECLARE
    c_annual_fee CONSTANT NUMBER := 25;

    CURSOR ApplyAnnualFee IS
        SELECT AccountID, Balance FROM Accounts FOR UPDATE OF Balance;

    v_fee_to_apply NUMBER;
BEGIN
    FOR acct_rec IN ApplyAnnualFee LOOP

        IF acct_rec.Balance >= c_annual_fee THEN
            v_fee_to_apply := c_annual_fee;
        ELSE
            v_fee_to_apply := acct_rec.Balance;  -- cap the fee so balance floors at 0
            DBMS_OUTPUT.PUT_LINE('WARNING: Account ' || acct_rec.AccountID ||
                                  ' balance too low for full fee; fee capped at ' || v_fee_to_apply);
        END IF;

        UPDATE Accounts
           SET Balance = Balance - v_fee_to_apply,
               LastModified = SYSDATE
         WHERE CURRENT OF ApplyAnnualFee;

        DBMS_OUTPUT.PUT_LINE('Account ' || acct_rec.AccountID ||
                              ' charged annual fee of ' || v_fee_to_apply ||
                              '. New balance: ' || (acct_rec.Balance - v_fee_to_apply));
    END LOOP;

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('--- Annual fee processing complete ---');
END;
/

-- Verification query
SELECT AccountID, Balance FROM Accounts ORDER BY AccountID;

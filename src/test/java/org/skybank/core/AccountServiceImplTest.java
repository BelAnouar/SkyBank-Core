package org.skybank.core;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.skybank.core.domain.context.AccountContext;
import org.skybank.core.domain.exception.AuthenticationException;
import org.skybank.core.domain.exception.InsufficientFundsException;
import org.skybank.core.domain.exception.InvalidAmountException;
import org.skybank.core.domain.model.Account;
import org.skybank.core.domain.model.Transaction;
import org.skybank.core.domain.service.implemantation.AccountServiceImpl;

import java.util.ArrayList;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceImplTest {

    private AccountServiceImpl accountService;
    private Account testAccount;

    @BeforeEach
    void setUp() {
        accountService = new AccountServiceImpl();
        testAccount = new Account();
        testAccount.setAccountNumber("ACC123");
        testAccount.setBalance(0);
        testAccount.setTransactions(new ArrayList<>());

        AccountContext.setCurrentAccount(testAccount);
    }

    @AfterEach
    void tearDown() {
        AccountContext.clear();
    }



    @Test
    @DisplayName("Should successfully deposit positive amount")
    void testDeposit_ValidAmount_Success() {

        int depositAmount = 1000;
        int initialBalance = testAccount.getBalance();


        accountService.deposit(depositAmount);


        assertEquals(initialBalance + depositAmount, testAccount.getBalance());
        assertEquals(1, testAccount.getTransactions().size());
    }

    @Test
    @DisplayName("Should handle multiple deposits correctly")
    void testDeposit_MultipleDeposits_Success() {

        accountService.deposit(1000);
        accountService.deposit(2000);
        accountService.deposit(500);


        assertEquals(3500, testAccount.getBalance());
        assertEquals(3, testAccount.getTransactions().size());
    }

    @Test
    @DisplayName("Should throw InvalidAmountException for zero deposit")
    void testDeposit_ZeroAmount_ThrowsException() {

        InvalidAmountException exception = assertThrows(
                InvalidAmountException.class,
                () -> accountService.deposit(0)
        );
        assertTrue(exception.getMessage().contains("must be positive"));
    }

    @Test
    @DisplayName("Should throw InvalidAmountException for negative deposit")
    void testDeposit_NegativeAmount_ThrowsException() {

        InvalidAmountException exception = assertThrows(
                InvalidAmountException.class,
                () -> accountService.deposit(-100)
        );
        assertTrue(exception.getMessage().contains("must be positive"));
    }

    @Test
    @DisplayName("Should throw AuthenticationException when no account is signed in")
    void testDeposit_NoAccountSignedIn_ThrowsException() {

        AccountContext.clear();


        assertThrows(AuthenticationException.class, () -> accountService.deposit(100));
    }


    @Test
    @DisplayName("Should successfully withdraw when sufficient funds available")
    void testWithdraw_SufficientFunds_Success() {

        accountService.deposit(1000);


        accountService.withdraw(500);


        assertEquals(500, testAccount.getBalance());
        assertEquals(2, testAccount.getTransactions().size());
    }

    @Test
    @DisplayName("Should throw InsufficientFundsException when balance is insufficient")
    void testWithdraw_InsufficientFunds_ThrowsException() {

        accountService.deposit(100);


        InsufficientFundsException exception = assertThrows(
                InsufficientFundsException.class,
                () -> accountService.withdraw(500)
        );
        assertTrue(exception.getMessage().contains("Insufficient funds"));
        assertTrue(exception.getMessage().contains("500"));
        assertTrue(exception.getMessage().contains("100"));
    }

    @Test
    @DisplayName("Should throw InvalidAmountException for zero withdrawal")
    void testWithdraw_ZeroAmount_ThrowsException() {

        accountService.deposit(1000);


        InvalidAmountException exception = assertThrows(
                InvalidAmountException.class,
                () -> accountService.withdraw(0)
        );
        assertTrue(exception.getMessage().contains("must be positive"));
    }

    @Test
    @DisplayName("Should throw InvalidAmountException for negative withdrawal")
    void testWithdraw_NegativeAmount_ThrowsException() {

        accountService.deposit(1000);


        InvalidAmountException exception = assertThrows(
                InvalidAmountException.class,
                () -> accountService.withdraw(-100)
        );
        assertTrue(exception.getMessage().contains("must be positive"));
    }

    @Test
    @DisplayName("Should throw AuthenticationException when no account is signed in")
    void testWithdraw_NoAccountSignedIn_ThrowsException() {

        AccountContext.clear();


        assertThrows(AuthenticationException.class, () -> accountService.withdraw(100));
    }

    @Test
    @DisplayName("Should allow withdrawal of entire balance")
    void testWithdraw_EntireBalance_Success() {

        accountService.deposit(1000);


        accountService.withdraw(1000);

        assertEquals(0, testAccount.getBalance());
    }


    @Test
    @DisplayName("Acceptance Test: Deposit 1000, Deposit 2000, Withdraw 500")
    void testAcceptanceScenario() {

        accountService.deposit(1000);


        accountService.deposit(2000);


        accountService.withdraw(500);


        assertEquals(2500, testAccount.getBalance());

        List<Transaction> transactions = testAccount.getTransactions();
        assertEquals(3, transactions.size());


        assertEquals(1000, transactions.get(0).getAmount());
        assertEquals(2000, transactions.get(1).getAmount());
        assertEquals(-500, transactions.get(2).getAmount());


        assertEquals(1000, transactions.get(0).getBalance());
        assertEquals(3000, transactions.get(1).getBalance());
        assertEquals(2500, transactions.get(2).getBalance());
    }



    @Test
    @DisplayName("Should print statement successfully when transactions exist")
    void testPrintStatement_WithTransactions_Success() {
         accountService.deposit(1000);
        accountService.withdraw(500);


        assertDoesNotThrow(() -> accountService.printStatement());
    }

    @Test
    @DisplayName("Should handle empty transaction list gracefully")
    void testPrintStatement_NoTransactions_Success() {

        assertDoesNotThrow(() -> accountService.printStatement());
    }

    @Test
    @DisplayName("Should throw AuthenticationException when no account is signed in")
    void testPrintStatement_NoAccountSignedIn_ThrowsException() {

        AccountContext.clear();


        assertThrows(AuthenticationException.class, () -> accountService.printStatement());
    }


    @Test
    @DisplayName("Should handle large deposit amounts")
    void testDeposit_LargeAmount_Success() {

        int largeAmount = Integer.MAX_VALUE / 2;

        accountService.deposit(largeAmount);

        assertEquals(largeAmount, testAccount.getBalance());
    }

    @Test
    @DisplayName("Should handle many transactions efficiently")
    void testMultipleTransactions_Performance() {

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            accountService.deposit(10);
        }

        long endTime = System.currentTimeMillis();


        assertEquals(10000, testAccount.getBalance());
        assertEquals(1000, testAccount.getTransactions().size());


        assertTrue((endTime - startTime) < 5000, "1000 transactions should complete in under 5 seconds");
    }

    @Test
    @DisplayName("Should maintain transaction order correctly")
    void testTransactionOrder() {

        accountService.deposit(100);
        accountService.deposit(200);
        accountService.withdraw(50);
        accountService.deposit(300);


        List<Transaction> transactions = testAccount.getTransactions();
        assertEquals(4, transactions.size());
        assertEquals(100, transactions.get(0).getAmount());
        assertEquals(200, transactions.get(1).getAmount());
        assertEquals(-50, transactions.get(2).getAmount());
        assertEquals(300, transactions.get(3).getAmount());
    }

    @Test
    @DisplayName("Should handle sequential deposits and withdrawals")
    void testSequentialOperations() {

        accountService.deposit(1000);
        accountService.withdraw(200);
        accountService.deposit(500);
        accountService.withdraw(300);


        assertEquals(1000, testAccount.getBalance());
        assertEquals(4, testAccount.getTransactions().size());
    }

    @Test
    @DisplayName("Should not allow withdrawal immediately after clearing balance")
    void testWithdrawAfterClearingBalance() {

        accountService.deposit(1000);
        accountService.withdraw(1000);


        assertThrows(InsufficientFundsException.class, () -> accountService.withdraw(1));
    }

    @Test
    @DisplayName("Should maintain correct balance after multiple operations")
    void testBalanceConsistency() {

        accountService.deposit(5000);
        accountService.withdraw(1000);
        accountService.withdraw(500);
        accountService.deposit(2000);
        accountService.withdraw(1500);


        assertEquals(4000, testAccount.getBalance());


        List<Transaction> transactions = testAccount.getTransactions();
        assertEquals(4000, transactions.get(transactions.size() - 1).getBalance());
    }
}
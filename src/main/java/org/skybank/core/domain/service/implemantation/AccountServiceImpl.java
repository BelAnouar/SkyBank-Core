package org.skybank.core.domain.service.implemantation;

import org.skybank.core.domain.context.AccountContext;
import org.skybank.core.domain.exception.AuthenticationException;
import org.skybank.core.domain.exception.InsufficientFundsException;
import org.skybank.core.domain.exception.InvalidAmountException;
import org.skybank.core.domain.model.Account;
import org.skybank.core.domain.model.Transaction;
import org.skybank.core.domain.service.AccountService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AccountServiceImpl implements AccountService {
    private final SimpleDateFormat dateFormat;
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    public AccountServiceImpl() {
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    }

    @Override
    public void deposit(int amount) {
        Account currentAccount = getCurrentAccountOrThrow();

        try {
            if (amount <= 0) {
                throw new InvalidAmountException("Deposit amount must be positive. Attempted amount: " + amount);
            }

            currentAccount.deposit(amount, new Date());

        } catch (InvalidAmountException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new InvalidAmountException("Invalid deposit amount: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during deposit: " + e.getMessage(), e);
        }
    }

    @Override
    public void withdraw(int amount) {
        Account currentAccount = getCurrentAccountOrThrow();

        try {
            if (amount <= 0) {
                throw new InvalidAmountException("Withdrawal amount must be positive. Attempted amount: " + amount);
            }

            if (amount > currentAccount.getBalance()) {
                throw new InsufficientFundsException(
                        String.format("Insufficient funds. Attempted withdrawal: %d, Available balance: %d",
                                amount, currentAccount.getBalance())
                );
            }

            currentAccount.withdraw(amount, new Date());

        } catch (InvalidAmountException | InsufficientFundsException e) {
            throw e;
        } catch (IllegalArgumentException e) {

            if (e.getMessage().contains("Insufficient funds")) {
                throw new InsufficientFundsException("Insufficient funds: " + e.getMessage());
            } else {
                throw new InvalidAmountException("Invalid withdrawal amount: " + e.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during withdrawal: " + e.getMessage(), e);
        }
    }

    @Override
    public void printStatement() {
        Account currentAccount = getCurrentAccountOrThrow();

        try {
            List<Transaction> transactions = currentAccount.getTransactions();

            if (transactions == null || transactions.isEmpty()) {
                logger.info("No transactions found for this account.");
                return;
            }

            logger.info("\n=== Account Statement ===");
            logger.info("Account Number: " + currentAccount.getAccountNumber());
            logger.info("Current Balance: " + currentAccount.getBalance());
            logger.info("\nDate       || Amount  || Balance");
            logger.info("--------------------------------");


            for (int i = transactions.size() - 1; i >= 0; i--) {
                Transaction t = transactions.get(i);
                String dateStr = dateFormat.format(t.getDate());
                String amountStr = String.format("%+d", t.getAmount());
                String balanceStr = String.format("%d", t.getBalance());

                System.out.printf("%-10s || %-7s || %s%n", dateStr, amountStr, balanceStr);
            }
            logger.info("================================\n");

        } catch (Exception e) {
            throw new RuntimeException("Error printing statement: " + e.getMessage(), e);
        }
    }


    private Account getCurrentAccountOrThrow() {
        Account currentAccount = AccountContext.getCurrentAccount();
        if (currentAccount == null) {
            throw new AuthenticationException("No account is currently signed in. Please sign in first.");
        }
        return currentAccount;
    }
}
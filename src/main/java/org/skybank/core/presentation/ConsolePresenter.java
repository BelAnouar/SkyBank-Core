package org.skybank.core.presentation;

import org.skybank.core.application.dto.request.SignInRequest;
import org.skybank.core.application.dto.response.CreateAccountResponse;
import org.skybank.core.application.dto.response.SignInResponse;
import org.skybank.core.application.dto.response.SignOutResponse;

import org.skybank.core.domain.service.AccountService;
import java.util.Scanner;
import org.skybank.core.domain.model.Account;
import org.skybank.core.domain.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class ConsolePresenter {
    private static final Logger logger = LoggerFactory.getLogger(ConsolePresenter.class);
    private static final String SUCCESS_SYMBOL = "✓";
    private static final String ERROR_SYMBOL = "✗";

    private final AuthService authService;
    private final AccountService accountService;
    private final Scanner scanner;
    private boolean running;

    public ConsolePresenter(AuthService authService, AccountService accountService) {
        this.authService = authService;
        this.accountService = accountService;
        this.scanner = new Scanner(System.in);
        this.running = true;
        logger.info("ConsolePresenter initialized");
    }

    public void start() {
        logger.info("Starting SkyBank Console");
        logger.info("Welcome to SkyBank Console");
        logger.info("-------------------------");

        while (running) {
            if (authService.getCurrentAccount() == null) {
                handleAccountSelection();
            } else {
                handleAccountOperations();
            }
        }

        scanner.close();
    }

    private void handleAccountSelection() {
        logger.debug("Displaying account selection menu");
        logger.info("\nAccount Selection");
        logger.info("1. Create new account");
        logger.info("2. Sign in with existing account");
        logger.info("3. Exit");
        logger.info("Your choice: ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                handleCreateAccount();
                break;
            case "2":
                handleSignIn();
                break;
            case "3":
                exitApplication();
                break;
            default:
                logger.warn("Invalid menu choice entered: {}", choice);
                logger.info("Invalid choice. Please try again.");
        }
    }

    private void handleCreateAccount() {
        logger.debug("Creating new account");

        CreateAccountResponse response = authService.createAccount();

        if (response.success()) {
            logger.info("Account created successfully: {}", response.accountNumber());
            logger.info("\n{} {}", SUCCESS_SYMBOL, response.message());
            logger.info("Account Number: {}", response.accountNumber());
            logger.info("Initial Balance: {}", response.balance());
        } else {
            logger.error("Account creation failed: {}", response.message());
            logger.info("\n{} {}", ERROR_SYMBOL, response.message());
        }
    }

    private void handleSignIn() {
        logger.info("Enter account number: ");
        String accountNumber = scanner.nextLine();

        try {
            logger.debug("Attempting to sign in with account number: {}", accountNumber);

            SignInRequest request = new SignInRequest(accountNumber);
            SignInResponse response = authService.signIn(request);

            if (response.success()) {
                logger.info("Sign in successful: {}", response.account().accountNumber());
                logger.info("\n{} {}", SUCCESS_SYMBOL, response.message());
                logger.info("Account Number: {}", response.account().accountNumber());
                logger.info("Current Balance: {}", response.account().balance());
                logger.info("Total Transactions: {}", response.account().transactionCount());
            } else {
                logger.warn("Sign in failed: {}", response.message());
                logger.info("\n{} {}", ERROR_SYMBOL, response.message());
            }
        } catch (IllegalArgumentException e) {
            logger.error("Invalid sign in request: {}", e.getMessage());
            logger.info("\n{} Error: {}", ERROR_SYMBOL, e.getMessage());
        }
    }

    private void handleAccountOperations() {
        logger.debug("Displaying account operations menu");
        logger.info("\nAccount Operations");
        logger.info("1. Deposit money");
        logger.info("2. Withdraw money");
        logger.info("3. Print statement");
        logger.info("4. Sign out");
        logger.info("5. Exit");
        logger.info("Your choice: ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                handleDeposit();
                break;
            case "2":
                handleWithdrawal();
                break;
            case "3":
                logger.debug("Printing statement");
                accountService.printStatement();
                break;
            case "4":
                handleSignOut();
                break;
            case "5":
                exitApplication();
                break;
            default:
                logger.warn("Invalid menu choice entered: {}", choice);
                logger.info("Invalid choice. Please try again.");
        }
    }

    private void handleDeposit() {
        logger.info("Enter amount to deposit: ");
        try {
            int amount = Integer.parseInt(scanner.nextLine());
            logger.debug("Attempting to deposit amount: {}", amount);
            accountService.deposit(amount);
            logger.info("Deposit successful for amount: {}", amount);
            logger.info("\n{} Deposit successful.", SUCCESS_SYMBOL);


            Account currentAccount = authService.getCurrentAccount();
            if (currentAccount != null) {
                logger.info("New Balance: {}", currentAccount.getBalance());
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid amount entered for deposit", e);
            logger.info("\n{} Invalid amount. Please enter a valid number.", ERROR_SYMBOL);
        } catch (IllegalArgumentException e) {
            logger.error("Deposit failed: {}", e.getMessage());
            logger.info("\n{} Error: {}", ERROR_SYMBOL, e.getMessage());
        }
    }

    private void handleWithdrawal() {
        logger.info("Enter amount to withdraw: ");
        try {
            int amount = Integer.parseInt(scanner.nextLine());
            logger.debug("Attempting to withdraw amount: {}", amount);
            accountService.withdraw(amount);
            logger.info("Withdrawal successful for amount: {}", amount);
            logger.info("\n{} Withdrawal successful.", SUCCESS_SYMBOL);


            Account currentAccount = authService.getCurrentAccount();
            if (currentAccount != null) {
                logger.info("New Balance: {}", currentAccount.getBalance());
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid amount entered for withdrawal", e);
            logger.info("\n{} Invalid amount. Please enter a valid number.", ERROR_SYMBOL);
        } catch (IllegalArgumentException e) {
            logger.error("Withdrawal failed: {}", e.getMessage());
            logger.info("\n{} Error: {}", ERROR_SYMBOL, e.getMessage());
        }
    }

    private void handleSignOut() {
        logger.debug("Signing out user");

        SignOutResponse response = authService.signOut();

        if (response.success()) {
            logger.info("User signed out successfully");
            logger.info("\n{} {}", SUCCESS_SYMBOL, response.message());
        } else {
            logger.error("Sign out failed: {}", response.message());
            logger.info("\n{} {}", ERROR_SYMBOL, response.message());
        }
    }

    private void exitApplication() {
        logger.info("Exiting application - Thank you for using SkyBank. Goodbye!");
        running = false;
        scanner.close();
        System.exit(0);
    }
}
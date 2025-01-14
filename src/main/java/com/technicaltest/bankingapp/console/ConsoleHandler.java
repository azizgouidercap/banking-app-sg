package com.technicaltest.bankingapp.console;

import com.technicaltest.bankingapp.dto.AccountDTO;
import com.technicaltest.bankingapp.dto.OperationDTO;
import com.technicaltest.bankingapp.exception.BusinessException;
import com.technicaltest.bankingapp.service.AccountService;
import com.technicaltest.bankingapp.service.OperationService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import static com.technicaltest.bankingapp.utils.ValidationUtils.requireNonNull;
import static com.technicaltest.bankingapp.utils.ValidationUtils.requirePositiveNumber;


public class ConsoleHandler {

    private final AccountService accountService = new AccountService();
    private final OperationService operationService = new OperationService();
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Entry point of the console-based application.
     * Continuously displays a menu, handles user input, and invokes corresponding operations
     * until the user chooses to quit.
     */
    public void start() {
        boolean running = true;

        while (running) {
            try {
                displayMenu();

                int choice = getValidatedUserChoice();
                running = handleUserChoice(choice);

            } catch (BusinessException e) {
                System.err.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
            }
        }
        scanner.close();
    }

    /**
     * Validates and retrieves the user's menu choice.
     *
     * @return the user's menu choice as an integer
     */
    private int getValidatedUserChoice() {
        if (!scanner.hasNextInt()) {
            System.out.println("Invalid choice. Please try again.");
            scanner.next();
            return -1;
        }
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    /**
     * Handles the user's menu choice by delegating to the appropriate method.
     *
     * @param choice the user's menu choice
     * @return false if the user chooses to quit, true otherwise
     */
    private boolean handleUserChoice(int choice) {
        switch (choice) {
            case 1 -> createAccount();
            case 2 -> depositMoney();
            case 3 -> withdrawMoney();
            case 4 -> displayBalance();
            case 5 -> displayOperations();
            case 6 -> {
                System.out.println("Goodbye!");
                return false;
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
        return true;
    }

    /**
     * Displays the main menu with available options for the user.
     */
    private void displayMenu() {
        System.out.println("1. Create Account");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Display Balance");
        System.out.println("5. Display Operations");
        System.out.println("6. Quit");
        System.out.print("Enter your choice: ");
    }

    /**
     * Handles the creation of a new account.
     * Prompts the user to enter their name and initial balance.
     */
    private void createAccount() {
        System.out.print("Enter your name: ");
        String ownerName = scanner.nextLine();
        requireNonNull(ownerName, "Owner Name");

        System.out.print("Enter initial balance: ");
        var balance = scanner.nextBigDecimal();
        requirePositiveNumber(balance, "Initial Balance");

        AccountDTO createdAccount = accountService.createAccount(ownerName, balance);

        System.out.printf("Account created successfully. Account ID: %d%n", createdAccount.getId());
    }

    /**
     * Handles depositing money into an account.
     * Prompts the user to enter the account ID and the deposit amount.
     */
    private void depositMoney() {
        System.out.print("Enter account ID: ");
        long accountId = scanner.nextLong();
        requirePositiveNumber(accountId, "Account ID");

        System.out.print("Enter amount to deposit: ");
        var amount = scanner.nextBigDecimal();
        requirePositiveNumber(amount, "Amount to Withdraw");

        accountService.depositMoney(accountId, amount);
        System.out.println(amount + " deposited successfully.");
    }

    /**
     * Handles withdrawing money from an account.
     * Prompts the user to enter the account ID and the withdrawal amount.
     */
    private void withdrawMoney() {
        System.out.print("Enter account ID: ");
        long accountId = scanner.nextLong();
        System.out.print("Enter amount to withdraw: ");
        var amount = scanner.nextBigDecimal();

        accountService.withdrawMoney(accountId, amount);
        System.out.println(amount + " withdrawn successfully.");
    }

    /**
     * Displays the current balance of an account.
     * Prompts the user to enter the account ID.
     */
    private void displayBalance() {
        System.out.print("Enter account ID: ");
        long accountId = scanner.nextLong();
        requirePositiveNumber(accountId, "Account ID");

        BigDecimal accountBalance = accountService.getBalance(accountId);
        System.out.println("Account balance: " + accountBalance);
    }

    /**
     * Displays all operations performed on an account.
     * Prompts the user to enter the account ID and lists all related operations.
     */
    private void displayOperations() {
        System.out.print("Enter account ID: ");
        long accountId = scanner.nextLong();
        requirePositiveNumber(accountId, "Account ID");

        List<OperationDTO> operations = operationService.getOperationsByAccountId(accountId);
        System.out.println("Operations for Account ID : " + accountId);

        if (operations.isEmpty()) {
            System.out.println("No operations found.");
        }
        operations.forEach(operationDTO -> System.out.println(operationDTO.toString()));
    }
}
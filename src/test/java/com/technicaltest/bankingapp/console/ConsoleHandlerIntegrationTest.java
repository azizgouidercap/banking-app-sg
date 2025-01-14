package com.technicaltest.bankingapp.console;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

class ConsoleHandlerIntegrationTest {

    @Test
    void start_shouldNotAcceptCommand_whenItIsNotANumber() {
        // Given
        String simulatedInput = """
                X
                6
                """;
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);

        System.setIn(inputStream);
        System.setOut(printStream);

        ConsoleHandler consoleHandler = new ConsoleHandler();

        // When
        consoleHandler.start();

        // Then
        String output = outputStream.toString();

        assertThat(output)
                .contains("Invalid choice. Please try again.")
                .contains("Goodbye!");
    }

    @Test
    void start_shouldNotAcceptCommand_whenItIsANumber() {
        // Given
        String simulatedInput = """
                9
                6
                """;
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);

        System.setIn(inputStream);
        System.setOut(printStream);

        ConsoleHandler consoleHandler = new ConsoleHandler();

        // When
        consoleHandler.start();

        // Then
        String output = outputStream.toString();

        assertThat(output)
                .contains("Invalid choice. Please try again.")
                .contains("Goodbye!");
    }

    @Test
    void start_shouldCreateAccount() {
        // Given
        String simulatedInput = """
                1
                John Doe
                1000
                6
                """;
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);

        System.setIn(inputStream);
        System.setOut(printStream);

        ConsoleHandler consoleHandler = new ConsoleHandler();

        // When
        consoleHandler.start();

        // Then
        String output = outputStream.toString();

        assertThat(output)
                .contains("Enter your choice:")
                .contains("Enter your name:")
                .contains("Enter initial balance:")
                .contains("created successfully")
                .contains("Goodbye!");
    }

    @Test
    void start_shouldDepositAmount() {
        // Given
        String simulatedInput = """
                 1
                John Doe
                1000
                2
                1
                500
                4
                1
                6
                """;
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);

        System.setIn(inputStream);
        System.setOut(printStream);

        ConsoleHandler consoleHandler = new ConsoleHandler();

        // When
        consoleHandler.start();

        // Then
        String output = outputStream.toString();

        assertThat(output)
                .contains("Enter account ID:")
                .contains("Enter amount to deposit:")
                .contains("500 deposited successfully")
                .contains("Account balance: 1500")
                .contains("Goodbye!");
    }

    @Test
    void start_shouldWithdrawAmount() {
        // Given
        String simulatedInput = """
                 1
                John Doe
                1000
                3
                1
                500
                4
                1
                6
                """;
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);

        System.setIn(inputStream);
        System.setOut(printStream);

        ConsoleHandler consoleHandler = new ConsoleHandler();

        // When
        consoleHandler.start();

        // Then
        String output = outputStream.toString();

        assertThat(output)
                .contains("Enter account ID:")
                .contains("Enter amount to withdraw:")
                .contains("500 withdrawn successfully")
                .contains("Account balance: 500")
                .contains("Goodbye!");
    }

    @Test
    void start_shouldShowOperations() {
        // Given
        String simulatedInput = """
                 1
                John Doe
                1000
                3
                1
                500
                2
                1
                235
                5
                1
                6
                """;
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);

        System.setIn(inputStream);
        System.setOut(printStream);

        ConsoleHandler consoleHandler = new ConsoleHandler();

        // When
        consoleHandler.start();

        // Then
        String output = outputStream.toString();

        assertThat(output)
                .contains("Enter account ID:")
                .contains("Enter amount to withdraw:")
                .contains("500 withdrawn successfully")
                .contains("Enter amount to deposit:")
                .contains("235 deposited successfully")
                .contains("OperationDTO(amount=1000.00, balance=1000.00,")
                .contains("OperationDTO(amount=500.00, balance=500.00,")
                .contains("OperationDTO(amount=235.00, balance=735.00,")
                .contains("Goodbye!");
    }
}

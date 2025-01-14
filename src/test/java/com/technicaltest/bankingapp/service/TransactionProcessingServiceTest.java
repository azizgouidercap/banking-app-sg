package com.technicaltest.bankingapp.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionProcessingServiceTest {

    private final TransactionProcessingService transactionProcessingService = new TransactionProcessingService();

    @Test
    void addAmount_shouldSucceed() {
        // Given
        BigDecimal balance = BigDecimal.valueOf(1000);
        BigDecimal amount = BigDecimal.valueOf(500);

        // When
        BigDecimal result = transactionProcessingService.addAmount(balance, amount);

        // Then
        assertThat(result).isEqualTo(BigDecimal.valueOf(1500).setScale(2, RoundingMode.HALF_EVEN));
    }

    @Test
    void subtractAmount_shouldSucceed() {
        // Given
        BigDecimal balance = BigDecimal.valueOf(1000);
        BigDecimal amount = BigDecimal.valueOf(200);

        // When
        BigDecimal result = transactionProcessingService.subtractAmount(balance, amount);

        // Then
        assertThat(result).isEqualTo(BigDecimal.valueOf(800).setScale(2, RoundingMode.HALF_EVEN));
    }
}


package com.technicaltest.bankingapp.utils;

import com.technicaltest.bankingapp.exception.InvalidOperationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BigDecimalUtilsTest {

    @Test
    void normalize_shouldThrowException_whenValueIsNull() {
        // Given
        BigDecimal value = null;

        // When & Then
        InvalidOperationException exception = assertThrows(
                InvalidOperationException.class,
                () -> BigDecimalUtils.normalize(value)
        );
        assertEquals("Value must not be null", exception.getMessage());
    }

    @Test
    void normalize_shouldReturnSameValue_whenAlreadyNormalized() {
        // Given
        BigDecimal value = new BigDecimal("123.45");

        // When
        BigDecimal result = BigDecimalUtils.normalize(value);

        // Then
        assertEquals(new BigDecimal("123.45"), result);
    }

    @Test
    void normalize_shouldAdjustScale_whenScaleIsTooLow() {
        // Given
        BigDecimal value = new BigDecimal("123");

        // When
        BigDecimal result = BigDecimalUtils.normalize(value);

        // Then
        assertEquals(new BigDecimal("123.00"), result);
    }

    @Test
    void normalize_shouldRoundProperly_whenScaleIsTooHigh() {
        // Given
        BigDecimal value = new BigDecimal("123.456");

        // When
        BigDecimal result = BigDecimalUtils.normalize(value);

        // Then
        assertEquals(new BigDecimal("123.46"), result);
    }

    @Test
    void normalize_shouldRoundDown_whenRoundingIsNeeded() {
        // Given
        BigDecimal value = new BigDecimal("123.444");

        // When
        BigDecimal result = BigDecimalUtils.normalize(value);

        // Then
        assertEquals(new BigDecimal("123.44"), result);
    }
}

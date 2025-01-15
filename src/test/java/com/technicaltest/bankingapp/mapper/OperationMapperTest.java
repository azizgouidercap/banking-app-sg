package com.technicaltest.bankingapp.mapper;

import com.technicaltest.bankingapp.dto.OperationDTO;
import com.technicaltest.bankingapp.model.Operation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.math.BigDecimal;
import java.util.List;

import static com.technicaltest.bankingapp.builder.OperationBuilderFactory.buildOperation;
import static org.assertj.core.api.Assertions.assertThat;

class OperationMapperTest {


    @Test
    public void toDto_shouldReturnNull_WhenOperationIsNull() {
        // Given
        Operation Operation = null;

        // When
        OperationDTO operationDTO = OperationMapper.toDTO(Operation);

        // Then
        assertThat(operationDTO).isNull();
    }

    @Test
    public void toDto_shouldOperationMapToDto_WhenOperationIsNotNull() {
        // Given
        Operation operation = buildOperation(BigDecimal.valueOf(10.12), BigDecimal.TEN);

        // When
        OperationDTO operationDTO = OperationMapper.toDTO(operation);

        // Then
        assertThat(operationDTO).isNotNull();
        assertThat(operationDTO.getAccountId()).isEqualTo(1L);
        assertThat(operationDTO.getBalance()).isEqualTo(BigDecimal.valueOf(10.12));
        assertThat(operationDTO.getAmount()).isEqualTo(BigDecimal.TEN);
        assertThat(operationDTO.getType()).isEqualTo(operation.getType());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void toDtoList_shoudReturnEmptyList_whenEntryIsEmptyOrNull(List<Operation> operations) {
        // Given
        // When
        List<OperationDTO> result = OperationMapper.toListDTO(operations);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    public void toDtoList_shouldSucceed_WhenOperationsAreNotEmpty() {
        // Given
        List<Operation> operations = List.of(
                buildOperation(BigDecimal.valueOf(10.12), BigDecimal.TEN),
                buildOperation(BigDecimal.TEN, BigDecimal.ONE)
        );

        // When
        List<OperationDTO> result = OperationMapper.toListDTO(operations);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getAccountId()).isEqualTo(1L);
        assertThat(result.get(0).getBalance()).isEqualTo(BigDecimal.valueOf(10.12));
        assertThat(result.get(0).getAmount()).isEqualTo(BigDecimal.TEN);
        assertThat(result.get(1).getAccountId()).isEqualTo(1L);
        assertThat(result.get(1).getBalance()).isEqualTo(BigDecimal.TEN);
        assertThat(result.get(1).getAmount()).isEqualTo(BigDecimal.ONE);
    }
}

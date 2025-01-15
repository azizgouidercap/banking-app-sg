package com.technicaltest.bankingapp.repository;

import com.technicaltest.bankingapp.model.Operation;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static com.technicaltest.bankingapp.builder.OperationBuilderFactory.buildOperation;
import static org.assertj.core.api.Assertions.assertThat;

class OperationRepositoryTest {

    private final OperationRepository operationRepository = new OperationRepository();

    @Test
    void save_shouldSaveNewOperation() {
        // Given
        Operation operation = buildOperation(BigDecimal.TEN, BigDecimal.ONE).toBuilder()
                .id(null).createdAt(null).updatedAt(null)
                .build();

        // When
        Operation savedOperation = operationRepository.save(operation);

        // Then
        assertThat(savedOperation.getId()).isNotNull();
        assertThat(savedOperation.getCreatedAt()).isNotNull();
        assertThat(savedOperation.getUpdatedAt()).isNotNull();
        assertThat(savedOperation.getAccountId()).isEqualTo(1L);
        assertThat(savedOperation.getBalance()).isEqualTo(BigDecimal.TEN);
        assertThat(savedOperation.getAmount()).isEqualTo(BigDecimal.ONE);
        assertThat(savedOperation.getType()).isEqualTo(operation.getType());
        assertThat(savedOperation.getTimestamp()).isEqualTo(operation.getTimestamp());
    }

    @Test
    void save_shouldUpdateOperation() {
        // Given
        Operation operation = buildOperation(BigDecimal.TEN, BigDecimal.ONE);
        Operation savedOperation = operationRepository.save(operation);
        Instant originalUpdatedAt = savedOperation.getUpdatedAt();

        // When
        savedOperation.setBalance(BigDecimal.valueOf(2000));
        Operation updatedOperation = operationRepository.save(savedOperation);

        // Then
        assertThat(updatedOperation.getId()).isEqualTo(savedOperation.getId());
        assertThat(updatedOperation.getCreatedAt()).isEqualTo(savedOperation.getCreatedAt());
        assertThat(updatedOperation.getUpdatedAt()).isAfter(originalUpdatedAt);
        assertThat(updatedOperation.getBalance()).isEqualTo(BigDecimal.valueOf(2000));
    }

    @Test
    void findById_shouldFindOperation() {
        // Given
        Operation operation = buildOperation(BigDecimal.TEN, BigDecimal.ONE);
        Operation savedOperation = operationRepository.save(operation);

        // When
        Optional<Operation> retrievedOperation = operationRepository.findById(savedOperation.getId());

        // Then
        assertThat(retrievedOperation).isPresent();
        assertThat(retrievedOperation.get().getId()).isEqualTo(savedOperation.getId());
        assertThat(retrievedOperation.get().getTimestamp()).isEqualTo(operation.getTimestamp());
        assertThat(retrievedOperation.get().getBalance()).isEqualTo(BigDecimal.TEN);
        assertThat(retrievedOperation.get().getAmount()).isEqualTo(BigDecimal.ONE);
        assertThat(retrievedOperation.get().getType()).isEqualTo(operation.getType());
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenOperationDoesNotExist() {
        // Given
        long nonExistentId = 999;

        // When
        Optional<Operation> retrievedOperation = operationRepository.findById(nonExistentId);

        // Then
        assertThat(retrievedOperation).isEmpty();
    }
}

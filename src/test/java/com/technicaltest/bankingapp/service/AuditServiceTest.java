package com.technicaltest.bankingapp.service;

import com.technicaltest.bankingapp.enumeration.OperationType;
import com.technicaltest.bankingapp.model.Operation;
import com.technicaltest.bankingapp.repository.OperationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.technicaltest.bankingapp.builder.OperationFactoryBuilder.DEFAULT_ID;
import static com.technicaltest.bankingapp.builder.OperationFactoryBuilder.buildOperation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditServiceTest {

    @Mock
    private OperationRepository operationRepository;

    @InjectMocks
    private AuditService auditService;

    @Test
    void logOperation_shouldSucceed() {
        // Given
        Operation savedOperation = buildOperation(BigDecimal.TEN, BigDecimal.ONE);
        Operation operationToSave = buildOperation(BigDecimal.TEN.setScale(2, RoundingMode.HALF_EVEN), BigDecimal.ONE.setScale(2, RoundingMode.HALF_EVEN)).toBuilder()
                .createdAt(null).updatedAt(null).build();
        when(operationRepository.save(any())).thenReturn(savedOperation);

        // When
        auditService.logOperation(DEFAULT_ID, OperationType.DEPOSIT, BigDecimal.TEN, BigDecimal.ONE);

        // Then
        ArgumentCaptor<Operation> operationCaptor = ArgumentCaptor.forClass(Operation.class);
        verify(operationRepository).save(operationCaptor.capture());
        Operation capturedOperation = operationCaptor.getValue();
        assertThat(capturedOperation)
                .usingRecursiveComparison()
                .ignoringFields("id", "timestamp")
                .isEqualTo(operationToSave);
        assertThat(capturedOperation.getId()).isNull();
        assertThat(capturedOperation.getTimestamp()).isNotNull();
    }
}

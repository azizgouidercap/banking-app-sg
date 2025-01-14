package com.technicaltest.bankingapp.service;

import com.technicaltest.bankingapp.enumeration.OperationType;
import com.technicaltest.bankingapp.model.Operation;
import com.technicaltest.bankingapp.repository.OperationRepository;

import java.math.BigDecimal;
import java.time.Instant;

import static com.technicaltest.bankingapp.utils.BigDecimalUtils.normalize;

// This can be replaced by Annotation with Spring AOP in Spring Apps
public class AuditService {

    private final OperationRepository operationRepository;

    public AuditService() {
        this.operationRepository = new OperationRepository();
    }

    // Used for unit test injections
    public AuditService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public void logOperation(Long accountId, OperationType operationType, BigDecimal balance, BigDecimal amount) {
        Operation operation = Operation.builder()
                .balance(normalize(balance))
                .accountId(accountId)
                .type(operationType)
                .amount(normalize(amount))
                .timestamp(Instant.now())
                .build();
        operationRepository.save(operation);
    }
}

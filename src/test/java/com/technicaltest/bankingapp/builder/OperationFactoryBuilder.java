package com.technicaltest.bankingapp.builder;

import com.technicaltest.bankingapp.enumeration.OperationType;
import com.technicaltest.bankingapp.model.Account;
import com.technicaltest.bankingapp.model.Operation;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.Instant;

@UtilityClass
public class OperationFactoryBuilder {

    public static final Long DEFAULT_ID = 1L;

    public static Operation buildOperation(BigDecimal balance, BigDecimal amount) {
        return Operation
                .builder()
                .id(DEFAULT_ID)
                .balance(balance)
                .accountId(DEFAULT_ID)
                .amount(amount)
                .type(OperationType.DEPOSIT)
                .timestamp(Instant.now())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }
}

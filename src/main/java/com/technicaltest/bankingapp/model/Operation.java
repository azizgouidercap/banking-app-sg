package com.technicaltest.bankingapp.model;

import com.technicaltest.bankingapp.enumeration.OperationType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Operation extends Entity {
    private Long accountId;
    private BigDecimal amount;
    private BigDecimal balance;
    private Instant timestamp;
    private OperationType type;
}


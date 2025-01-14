package com.technicaltest.bankingapp.dto;

import com.technicaltest.bankingapp.enumeration.OperationType;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

@Value
@Builder
public class OperationDTO {
    BigDecimal amount;
    BigDecimal balance;
    Instant timestamp;
    OperationType type;
    Long accountId;
}

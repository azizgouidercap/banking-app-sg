package com.technicaltest.bankingapp.service;

import java.math.BigDecimal;

import static com.technicaltest.bankingapp.utils.BigDecimalUtils.normalize;

// This service was created to focus on processing deposits, withdrawals and potentially applying rates.
public class TransactionProcessingService {

    public BigDecimal addAmount(BigDecimal balance, BigDecimal amount) {
        return normalize(balance.add(amount));
    }

    public BigDecimal subtractAmount(BigDecimal balance, BigDecimal amount) {
        return normalize(balance.subtract(amount));
    }
}

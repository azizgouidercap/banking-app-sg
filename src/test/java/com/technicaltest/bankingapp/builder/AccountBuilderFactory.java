package com.technicaltest.bankingapp.builder;

import com.technicaltest.bankingapp.model.Account;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.Instant;

@UtilityClass
public class AccountBuilderFactory {

    public static final String OWNER_NAME = "John Doe";
    public static final Long DEFAULT_ID = 1L;

    public static Account buildAccount(BigDecimal balance) {
        return Account
                .builder()
                .ownerName(OWNER_NAME)
                .id(DEFAULT_ID)
                .balance(balance)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }
}

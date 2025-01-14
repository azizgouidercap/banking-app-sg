package com.technicaltest.bankingapp.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Account extends Entity {
    private String ownerName;
    private BigDecimal balance;

}

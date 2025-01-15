package com.technicaltest.bankingapp.mapper;

import com.technicaltest.bankingapp.dto.AccountDTO;
import com.technicaltest.bankingapp.model.Account;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.technicaltest.bankingapp.builder.AccountBuilderFactory.buildAccount;
import static org.assertj.core.api.Assertions.assertThat;

class AccountMapperTest {

    @Test
    public void toDto_shouldReturnNull_WhenAccountIsNull() {
        // Given
        Account account = null;

        // When
        AccountDTO accountDTO = AccountMapper.toDTO(account);

        // Then
        assertThat(accountDTO).isNull();
    }

    @Test
    public void toDto_shouldAccountMapToDto_WhenAccountIsNotNull() {
        // Given
        Account savingsAccount = buildAccount(BigDecimal.valueOf(10.12));

        // When
        AccountDTO accountDTO = AccountMapper.toDTO(savingsAccount);

        // Then
        assertThat(accountDTO).isNotNull();
        assertThat(accountDTO.getId()).isEqualTo(1L);
        assertThat(accountDTO.getBalance()).isEqualTo(BigDecimal.valueOf(10.12));
        assertThat(accountDTO.getOwnerName()).isEqualTo("John Doe");
    }
}

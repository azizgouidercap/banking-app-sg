package com.technicaltest.bankingapp.service;

import com.technicaltest.bankingapp.dto.AccountDTO;
import com.technicaltest.bankingapp.enumeration.OperationType;
import com.technicaltest.bankingapp.exception.ResourceNotFoundException;
import com.technicaltest.bankingapp.model.Account;
import com.technicaltest.bankingapp.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static com.technicaltest.bankingapp.builder.AccountBuilderFactory.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.math.RoundingMode;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccount_shouldCreateAccount() {
        // Given
        BigDecimal balance = BigDecimal.valueOf(1000).setScale(2, RoundingMode.HALF_EVEN);
        Account mockAccount = buildAccount(balance);
        when(accountRepository.save(any(Account.class))).thenReturn(mockAccount);
        doNothing().when(auditService).logOperation(anyLong(), any(), any(), any());

        // When
        AccountDTO result = accountService.createAccount(OWNER_NAME, balance);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getOwnerName()).isEqualTo(OWNER_NAME);
        assertThat(result.getBalance()).isEqualTo(balance);
        assertThat(result.getId()).isEqualTo(DEFAULT_ID);
        ArgumentCaptor<Account> checkingAccountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(checkingAccountArgumentCaptor.capture());
        Account checkingAccount = checkingAccountArgumentCaptor.getValue();
        assertThat(checkingAccount.getBalance()).isEqualTo(balance);
        assertThat(checkingAccount.getId()).isNull();
        assertThat(checkingAccount.getOwnerName()).isEqualTo(OWNER_NAME);
        verifyLogOperation(BigDecimal.valueOf(1000), BigDecimal.valueOf(1000));
    }

    @Test
    void findById_shouldThrowException_whenAccountIsNotFound() {
        // Given
        when(accountRepository.findById(DEFAULT_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> accountService.findById(DEFAULT_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Account with ID 1 not found.");
        verify(accountRepository).findById(any());
    }

    @Test
    void findById_shouldReturnAccount() {
        // Given
        Account mockAccount = buildAccount(BigDecimal.valueOf(1000));
        when(accountRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(mockAccount));

        // When
        Account result = accountService.findById(DEFAULT_ID);

        // Then
        assertThat(result).isEqualTo(mockAccount);
        verify(accountRepository).findById(any());
    }

    @Test
    void save_shouldSucceed() {
        // Given
        Account account = buildAccount(BigDecimal.valueOf(1000));
        Account mockAccount = buildAccount(BigDecimal.valueOf(1000));
        when(accountRepository.save(any())).thenReturn(mockAccount);

        // When
        Account result = accountService.save(account);

        // Then
        assertThat(result).isEqualTo(mockAccount);
        verify(accountRepository).save(any());
    }

    private void verifyLogOperation(BigDecimal balance, BigDecimal amount) {
        ArgumentCaptor<OperationType> operationTypeArgumentCaptor = ArgumentCaptor.forClass(OperationType.class);
        ArgumentCaptor<Long> acountIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<BigDecimal> amountArgumentCaptor = ArgumentCaptor.forClass(BigDecimal.class);
        ArgumentCaptor<BigDecimal> balanceArgumentCaptor = ArgumentCaptor.forClass(BigDecimal.class);

        verify(auditService).logOperation(
                acountIdArgumentCaptor.capture(),
                operationTypeArgumentCaptor.capture(),
                balanceArgumentCaptor.capture(),
                amountArgumentCaptor.capture());
        assertThat(operationTypeArgumentCaptor.getValue()).isEqualTo(OperationType.DEPOSIT);
        assertThat(acountIdArgumentCaptor.getValue()).isEqualTo(DEFAULT_ID);
        assertThat(amountArgumentCaptor.getValue()).isEqualByComparingTo(amount);
        assertThat(balanceArgumentCaptor.getValue()).isEqualByComparingTo(balance);
    }
}

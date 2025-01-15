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
    private TransactionProcessingService transactionProcessingService;

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
        verifyLogOperation(OperationType.DEPOSIT, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000));
    }

    @Test
    void depositMoney_shouldSucceed() {
        // Given
        BigDecimal amount = BigDecimal.valueOf(500);
        Account mockAccount = buildAccount(BigDecimal.valueOf(1000));
        when(accountRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(mockAccount));
        when(transactionProcessingService.addAmount(any(), any())).thenReturn(BigDecimal.valueOf(1500));

        // When
        accountService.depositMoney(DEFAULT_ID, amount);

        // Then
        assertThat(mockAccount.getBalance()).isEqualTo(BigDecimal.valueOf(1500));
        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository, times(1)).save(accountArgumentCaptor.capture());
        Account account = accountArgumentCaptor.getValue();
        assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(1500));
        verify(transactionProcessingService).addAmount(any(), any());
        verifyLogOperation(OperationType.DEPOSIT, BigDecimal.valueOf(1500), BigDecimal.valueOf(500));
    }

    @Test
    void depositMoney_shouldThrowException_whenAccountIsNotFound() {
        // Given
        BigDecimal amount = BigDecimal.valueOf(500);
        when(accountRepository.findById(DEFAULT_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> accountService.depositMoney(DEFAULT_ID, amount))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Account with ID 1 not found.");
        verify(accountRepository, never()).save(any(Account.class));
        verify(auditService, never()).logOperation(anyLong(), any(), any(), any());
    }

    @Test
    void withdrawMoney_shouldSucceed_whenAccountTypeIsChecking() {
        // Given
        long accountId = 1L;
        BigDecimal amount = BigDecimal.valueOf(200);
        Account mockAccount = buildAccount(BigDecimal.valueOf(1000));
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));
        when(transactionProcessingService.subtractAmount(any(), any())).thenReturn(BigDecimal.valueOf(800));

        // Act
        accountService.withdrawMoney(accountId, amount);

        // Assert
        assertThat(mockAccount.getBalance()).isEqualTo(BigDecimal.valueOf(800));
        verify(accountRepository, times(1)).save(mockAccount);
        verifyLogOperation(OperationType.WITHDRAWAL, BigDecimal.valueOf(800), BigDecimal.valueOf(200));
    }

    private void verifyLogOperation(OperationType operationType, BigDecimal balance, BigDecimal amount) {
        ArgumentCaptor<OperationType> operationTypeArgumentCaptor = ArgumentCaptor.forClass(OperationType.class);
        ArgumentCaptor<Long> acountIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<BigDecimal> amountArgumentCaptor = ArgumentCaptor.forClass(BigDecimal.class);
        ArgumentCaptor<BigDecimal> balanceArgumentCaptor = ArgumentCaptor.forClass(BigDecimal.class);

        verify(auditService).logOperation(
                acountIdArgumentCaptor.capture(),
                operationTypeArgumentCaptor.capture(),
                balanceArgumentCaptor.capture(),
                amountArgumentCaptor.capture());
        assertThat(operationTypeArgumentCaptor.getValue()).isEqualTo(operationType);
        assertThat(acountIdArgumentCaptor.getValue()).isEqualTo(DEFAULT_ID);
        assertThat(amountArgumentCaptor.getValue()).isEqualByComparingTo(amount);
        assertThat(balanceArgumentCaptor.getValue()).isEqualByComparingTo(balance);
    }
}

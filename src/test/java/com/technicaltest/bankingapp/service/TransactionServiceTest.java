package com.technicaltest.bankingapp.service;

import com.technicaltest.bankingapp.enumeration.OperationType;
import com.technicaltest.bankingapp.exception.ResourceNotFoundException;
import com.technicaltest.bankingapp.model.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.technicaltest.bankingapp.builder.AccountBuilderFactory.DEFAULT_ID;
import static com.technicaltest.bankingapp.builder.AccountBuilderFactory.buildAccount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private AuditService auditService;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void depositMoney_shouldSucceed() {
        // Given
        BigDecimal amount = BigDecimal.valueOf(500);
        Account mockAccount = buildAccount(BigDecimal.valueOf(1000));
        when(accountService.findById(DEFAULT_ID)).thenReturn(mockAccount);

        // When
        transactionService.depositMoney(DEFAULT_ID, amount);

        // Then
        assertThat(mockAccount.getBalance()).isEqualTo(BigDecimal.valueOf(1500).setScale(2, RoundingMode.HALF_EVEN));
        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountService, times(1)).save(accountArgumentCaptor.capture());
        Account account = accountArgumentCaptor.getValue();
        assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(1500).setScale(2, RoundingMode.HALF_EVEN));
        verifyLogOperation(OperationType.DEPOSIT, BigDecimal.valueOf(1500), BigDecimal.valueOf(500));
    }

    @Test
    void depositMoney_shouldFail_whenAccountNotFound() {
        // Given
        BigDecimal amount = BigDecimal.valueOf(500);
        when(accountService.findById(DEFAULT_ID)).thenThrow(new ResourceNotFoundException("Account", DEFAULT_ID));

        // When & Then
        assertThatThrownBy(() -> transactionService.depositMoney(DEFAULT_ID, amount))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Account with ID 1 not found.");
        verify(accountService, never()).save(any());
        verify(auditService, never()).logOperation(any(), any(), any(), any());
    }

    @Test
    void withdrawMoney_shouldSucceed_whenAccountTypeIsChecking() {
        // Given
        long accountId = 1L;
        BigDecimal amount = BigDecimal.valueOf(200);
        Account mockAccount = buildAccount(BigDecimal.valueOf(1000));
        when(accountService.findById(accountId)).thenReturn(mockAccount);

        // Act
        transactionService.withdrawMoney(accountId, amount);

        // Assert
        assertThat(mockAccount.getBalance()).isEqualTo(BigDecimal.valueOf(800).setScale(2, RoundingMode.HALF_EVEN));
        verify(accountService, times(1)).save(mockAccount);
        verifyLogOperation(OperationType.WITHDRAWAL, BigDecimal.valueOf(800), BigDecimal.valueOf(200));
    }

    @Test
    void withdrawMoney_shouldFail_whenAccountNotFound() {
        // Given
        BigDecimal amount = BigDecimal.valueOf(500);
        when(accountService.findById(DEFAULT_ID)).thenThrow(new ResourceNotFoundException("Account", DEFAULT_ID));

        // When & Then
        assertThatThrownBy(() -> transactionService.withdrawMoney(DEFAULT_ID, amount))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Account with ID 1 not found.");
        verify(accountService, never()).save(any());
        verify(auditService, never()).logOperation(any(), any(), any(), any());
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

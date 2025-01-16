package com.technicaltest.bankingapp.service;

import com.technicaltest.bankingapp.enumeration.OperationType;
import com.technicaltest.bankingapp.model.Account;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

import static com.technicaltest.bankingapp.utils.BigDecimalUtils.normalize;

@Slf4j
public class TransactionService {

    private final AccountService accountService;
    private final AuditService auditService;

    public TransactionService() {
        this.auditService = new AuditService();
        this.accountService = new AccountService();
    }

    // Used for unit test injections
    public TransactionService(AuditService auditService, AccountService accountService) {
        this.auditService = auditService;
        this.accountService = accountService;
    }

    public void depositMoney(long accountId, BigDecimal amount) {
        log.debug("AccountService - Initiating deposit.");
        Account account = accountService.findById(accountId);
        account.setBalance(normalize(account.getBalance().add(amount)));

        auditService.logOperation(account.getId(), OperationType.DEPOSIT, account.getBalance(), amount);
        accountService.save(account);
        log.debug("AccountService - Account deposit successfully.");
    }

    public void withdrawMoney(long accountId, BigDecimal amount) {
        log.debug("AccountService - Initiating withdraw.");
        Account account = accountService.findById(accountId);

        account.setBalance(normalize(account.getBalance().subtract(amount)));

        auditService.logOperation(account.getId(), OperationType.WITHDRAWAL, account.getBalance(), amount);
        accountService.save(account);
        log.debug("AccountService - Account withdraw successfully.");
    }

    public BigDecimal getBalance(long accountId) {
        return accountService.findById(accountId).getBalance();
    }

}

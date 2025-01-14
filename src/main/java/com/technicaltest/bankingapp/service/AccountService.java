package com.technicaltest.bankingapp.service;

import com.technicaltest.bankingapp.dto.AccountDTO;
import com.technicaltest.bankingapp.enumeration.OperationType;
import com.technicaltest.bankingapp.exception.ResourceNotFoundException;
import com.technicaltest.bankingapp.mapper.AccountMapper;
import com.technicaltest.bankingapp.model.Account;
import com.technicaltest.bankingapp.repository.AccountRepository;

import java.math.BigDecimal;

import static com.technicaltest.bankingapp.utils.BigDecimalUtils.normalize;

public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionProcessingService transactionProcessingService;
    private final AuditService auditService;

    public AccountService() {
        this.accountRepository = new AccountRepository();
        this.transactionProcessingService = new TransactionProcessingService();
        this.auditService = new AuditService();
    }

    // Used for unit test injections
    public AccountService(AccountRepository accountRepository, TransactionProcessingService transactionProcessingService, AuditService auditService) {
        this.accountRepository = accountRepository;
        this.transactionProcessingService = transactionProcessingService;
        this.auditService = auditService;
    }

    public AccountDTO createAccount(String ownerName, BigDecimal balance) {
        Account account = Account.builder()
                .balance(normalize(balance))
                .ownerName(ownerName)
                .build();

        Account createdAccount = accountRepository.save(account);
        auditService.logOperation(createdAccount.getId(), OperationType.DEPOSIT, balance, balance);
        return AccountMapper.toDTO(createdAccount);
    }

    public void depositMoney(long accountId, BigDecimal amount) {
        Account account = findById(accountId);
        account.setBalance(transactionProcessingService.addAmount(account.getBalance(), amount));

        auditService.logOperation(account.getId(), OperationType.DEPOSIT, account.getBalance(), amount);
        accountRepository.save(account);
    }

    public void withdrawMoney(long accountId, BigDecimal amount) {
        Account account = findById(accountId);

        account.setBalance(transactionProcessingService.subtractAmount(account.getBalance(), amount));

        auditService.logOperation(account.getId(), OperationType.WITHDRAWAL, account.getBalance(), amount);
        accountRepository.save(account);
    }

    public BigDecimal getBalance(long accountId) {
        return findById(accountId).getBalance();
    }

    private Account findById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", accountId));
    }

}

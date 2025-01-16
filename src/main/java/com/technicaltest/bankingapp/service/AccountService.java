package com.technicaltest.bankingapp.service;

import com.technicaltest.bankingapp.dto.AccountDTO;
import com.technicaltest.bankingapp.enumeration.OperationType;
import com.technicaltest.bankingapp.exception.ResourceNotFoundException;
import com.technicaltest.bankingapp.mapper.AccountMapper;
import com.technicaltest.bankingapp.model.Account;
import com.technicaltest.bankingapp.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

import static com.technicaltest.bankingapp.utils.BigDecimalUtils.normalize;
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final AuditService auditService;

    public AccountService() {
        this.accountRepository = new AccountRepository();
        this.auditService = new AuditService();
    }

    // Used for unit test injections
    public AccountService(AccountRepository accountRepository, AuditService auditService) {
        this.accountRepository = accountRepository;
        this.auditService = auditService;
    }

    public AccountDTO createAccount(String ownerName, BigDecimal balance) {
        log.debug("AccountService - Attempting to create account.");
        Account account = Account.builder()
                .balance(normalize(balance))
                .ownerName(ownerName)
                .build();

        Account createdAccount = accountRepository.save(account);
        auditService.logOperation(createdAccount.getId(), OperationType.DEPOSIT, balance, balance);
        log.debug("AccountService - Account created successfully.");
        return AccountMapper.toDTO(createdAccount);
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public Account findById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", accountId));
    }

}

package com.technicaltest.bankingapp.repository;

import com.technicaltest.bankingapp.model.Operation;

import java.util.List;

public class OperationRepository extends AbstractRepository<Operation> {

    public List<Operation> findByAccountId(Long accountId) {
        return collection.values()
                .stream()
                .filter(operation -> operation.getAccountId().equals(accountId))
                .toList();
    }
}

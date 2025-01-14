package com.technicaltest.bankingapp.service;

import com.technicaltest.bankingapp.dto.OperationDTO;
import com.technicaltest.bankingapp.repository.OperationRepository;

import java.util.List;

import static com.technicaltest.bankingapp.mapper.OperationMapper.toListDTO;

public class OperationService {

    private final OperationRepository operationRepository;

    public OperationService() {
        operationRepository = new OperationRepository();
    }

    // Used for unit test injections
    public OperationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public List<OperationDTO> getOperationsByAccountId(Long accountId) {
        return toListDTO(operationRepository.findByAccountId(accountId));
    }

}

package com.technicaltest.bankingapp.mapper;

import com.technicaltest.bankingapp.dto.OperationDTO;
import com.technicaltest.bankingapp.model.Operation;
import lombok.experimental.UtilityClass;

import java.util.List;

import static java.util.Collections.emptyList;

@UtilityClass
public class OperationMapper {

    public static OperationDTO toDTO(Operation operation) {
        if (operation == null) {
            return null;
        }

        return OperationDTO
                .builder()
                .accountId(operation.getAccountId())
                .amount(operation.getAmount())
                .timestamp(operation.getTimestamp())
                .type(operation.getType())
                .balance(operation.getBalance())
                .build();
    }

    public static List<OperationDTO> toListDTO(List<Operation> operations) {
        if (operations == null || operations.isEmpty()) {
            return emptyList();
        }

        return operations.stream().map(OperationMapper::toDTO).toList();
    }
}

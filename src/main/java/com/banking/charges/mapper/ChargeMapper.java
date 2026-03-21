package com.banking.charges.mapper;

import com.banking.charges.domain.entity.ChargeDefinition;
import com.banking.charges.domain.enums.ChargeType;
import com.banking.charges.dto.request.CreateChargeDefinitionRequest;
import com.banking.charges.dto.response.ChargeDefinitionResponse;
import com.banking.charges.exception.InvalidChargeTypeException;
import org.springframework.stereotype.Component;

@Component
public class ChargeMapper {

    public ChargeDefinition toEntity(CreateChargeDefinitionRequest request) {
        ChargeType chargeType = parseChargeType(request.getChargeType());
        return new ChargeDefinition(
            request.getName(),
            chargeType,
            request.getCurrency()
        );
    }

    public ChargeDefinitionResponse toResponse(ChargeDefinition entity) {
        return ChargeDefinitionResponse.fromEntity(entity);
    }

    private ChargeType parseChargeType(String type) {
        try {
            return ChargeType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new InvalidChargeTypeException(type);
        }
    }
}
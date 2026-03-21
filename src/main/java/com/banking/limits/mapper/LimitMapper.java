package com.banking.limits.mapper;

import com.banking.limits.domain.entity.LimitDefinition;
import com.banking.limits.domain.enums.LimitType;
import com.banking.limits.dto.request.CreateLimitDefinitionRequest;
import com.banking.limits.dto.response.LimitDefinitionResponse;
import com.banking.limits.exception.InvalidLimitTypeException;
import org.springframework.stereotype.Component;

@Component
public class LimitMapper {

    public LimitDefinition toEntity(CreateLimitDefinitionRequest request) {
        LimitType limitType = parseLimitType(request.getLimitType());
        return new LimitDefinition(
            request.getName(),
            limitType,
            request.getAmount(),
            request.getCurrency()
        );
    }

    public LimitDefinitionResponse toResponse(LimitDefinition entity) {
        return LimitDefinitionResponse.fromEntity(entity);
    }

    private LimitType parseLimitType(String type) {
        try {
            return LimitType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new InvalidLimitTypeException(type);
        }
    }
}

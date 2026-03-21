package com.banking.charges.service;

import com.banking.charges.domain.entity.ChargeDefinition;
import com.banking.charges.domain.enums.ChargeStatus;
import com.banking.charges.domain.enums.ChargeType;
import com.banking.charges.dto.request.CreateChargeDefinitionRequest;
import com.banking.charges.dto.request.UpdateChargeDefinitionRequest;
import com.banking.charges.dto.response.ChargeDefinitionResponse;
import com.banking.charges.exception.ChargeAlreadyExistsException;
import com.banking.charges.exception.ChargeNotFoundException;
import com.banking.charges.exception.InvalidChargeTypeException;
import com.banking.charges.mapper.ChargeMapper;
import com.banking.charges.repository.ChargeDefinitionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ChargeDefinitionService {

    private final ChargeDefinitionRepository repository;
    private final ChargeMapper mapper;

    public ChargeDefinitionService(ChargeDefinitionRepository repository, ChargeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ChargeDefinitionResponse createCharge(CreateChargeDefinitionRequest request) {
        if (repository.existsByName(request.getName())) {
            throw new ChargeAlreadyExistsException(request.getName());
        }
        ChargeDefinition entity = mapper.toEntity(request);
        ChargeDefinition saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public ChargeDefinitionResponse updateCharge(Long id, UpdateChargeDefinitionRequest request) {
        ChargeDefinition entity = repository.findById(id)
                .orElseThrow(() -> new ChargeNotFoundException(id));

        if (request.getName() != null) {
            if (!entity.getName().equals(request.getName()) && repository.existsByName(request.getName())) {
                throw new ChargeAlreadyExistsException(request.getName());
            }
            entity.setName(request.getName());
        }
        if (request.getChargeType() != null) {
            try {
                entity.setChargeType(ChargeType.valueOf(request.getChargeType()));
            } catch (IllegalArgumentException e) {
                throw new InvalidChargeTypeException(request.getChargeType());
            }
        }
        if (request.getCurrency() != null) {
            entity.setCurrency(request.getCurrency());
        }

        ChargeDefinition saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public ChargeDefinitionResponse activateCharge(Long id) {
        ChargeDefinition entity = repository.findById(id)
                .orElseThrow(() -> new ChargeNotFoundException(id));
        entity.setStatus(ChargeStatus.ACTIVE);
        ChargeDefinition saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public ChargeDefinitionResponse deactivateCharge(Long id) {
        ChargeDefinition entity = repository.findById(id)
                .orElseThrow(() -> new ChargeNotFoundException(id));
        entity.setStatus(ChargeStatus.INACTIVE);
        ChargeDefinition saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ChargeDefinitionResponse getCharge(Long id) {
        ChargeDefinition entity = repository.findById(id)
                .orElseThrow(() -> new ChargeNotFoundException(id));
        return mapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<ChargeDefinitionResponse> getAllCharges(String chargeType, String status) {
        List<ChargeDefinition> charges;
        if (chargeType != null && status != null) {
            try {
                ChargeType ct = ChargeType.valueOf(chargeType);
                ChargeStatus s = ChargeStatus.valueOf(status);
                charges = repository.findByChargeTypeAndStatus(ct, s);
            } catch (IllegalArgumentException e) {
                charges = repository.findAll();
            }
        } else if (chargeType != null) {
            try {
                ChargeType ct = ChargeType.valueOf(chargeType);
                charges = repository.findByChargeType(ct);
            } catch (IllegalArgumentException e) {
                charges = repository.findAll();
            }
        } else if (status != null) {
            try {
                ChargeStatus s = ChargeStatus.valueOf(status);
                charges = repository.findByStatus(s);
            } catch (IllegalArgumentException e) {
                charges = repository.findAll();
            }
        } else {
            charges = repository.findAll();
        }
        return charges.stream()
                .map(mapper::toResponse)
                .toList();
    }
}
package com.banking.limits.service;

import com.banking.limits.domain.entity.LimitDefinition;
import com.banking.limits.domain.enums.LimitStatus;
import com.banking.limits.dto.request.CreateLimitDefinitionRequest;
import com.banking.limits.dto.response.LimitDefinitionResponse;
import com.banking.limits.exception.LimitNotFoundException;
import com.banking.limits.mapper.LimitMapper;
import com.banking.limits.repository.LimitDefinitionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LimitDefinitionService {

    private final LimitDefinitionRepository limitDefinitionRepository;
    private final LimitMapper limitMapper;

    public LimitDefinitionService(LimitDefinitionRepository limitDefinitionRepository, LimitMapper limitMapper) {
        this.limitDefinitionRepository = limitDefinitionRepository;
        this.limitMapper = limitMapper;
    }

    public LimitDefinitionResponse createLimit(CreateLimitDefinitionRequest request) {
        LimitDefinition entity = limitMapper.toEntity(request);
        LimitDefinition saved = limitDefinitionRepository.save(entity);
        return limitMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public LimitDefinitionResponse getLimit(Long id) {
        LimitDefinition entity = limitDefinitionRepository.findById(id)
                .orElseThrow(() -> new LimitNotFoundException(id));
        return limitMapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<LimitDefinitionResponse> getAllLimits(LimitStatus status) {
        List<LimitDefinition> limits;
        if (status != null) {
            limits = limitDefinitionRepository.findByStatus(status);
        } else {
            limits = limitDefinitionRepository.findAll();
        }
        return limits.stream()
                .map(limitMapper::toResponse)
                .toList();
    }

    public LimitDefinitionResponse updateLimit(Long id, CreateLimitDefinitionRequest request) {
        LimitDefinition entity = limitDefinitionRepository.findById(id)
                .orElseThrow(() -> new LimitNotFoundException(id));

        entity.setName(request.getName());
        entity.setLimitType(limitMapper.toEntity(request).getLimitType());
        entity.setAmount(request.getAmount());
        entity.setCurrency(request.getCurrency());

        LimitDefinition saved = limitDefinitionRepository.save(entity);
        return limitMapper.toResponse(saved);
    }

    public LimitDefinitionResponse activateLimit(Long id) {
        LimitDefinition entity = limitDefinitionRepository.findById(id)
                .orElseThrow(() -> new LimitNotFoundException(id));
        entity.setStatus(LimitStatus.ACTIVE);
        LimitDefinition saved = limitDefinitionRepository.save(entity);
        return limitMapper.toResponse(saved);
    }

    public LimitDefinitionResponse deactivateLimit(Long id) {
        LimitDefinition entity = limitDefinitionRepository.findById(id)
                .orElseThrow(() -> new LimitNotFoundException(id));
        entity.setStatus(LimitStatus.INACTIVE);
        LimitDefinition saved = limitDefinitionRepository.save(entity);
        return limitMapper.toResponse(saved);
    }
}

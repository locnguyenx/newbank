package com.banking.masterdata.service;

import com.banking.masterdata.domain.entity.Industry;
import com.banking.masterdata.dto.request.CreateIndustryRequest;
import com.banking.masterdata.dto.response.IndustryResponse;
import com.banking.masterdata.exception.IndustryNotFoundException;
import com.banking.masterdata.mapper.MasterDataMapper;
import com.banking.masterdata.repository.IndustryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class IndustryService {

    private final IndustryRepository industryRepository;
    private final MasterDataMapper masterDataMapper;

    public IndustryService(IndustryRepository industryRepository, MasterDataMapper masterDataMapper) {
        this.industryRepository = industryRepository;
        this.masterDataMapper = masterDataMapper;
    }

    public IndustryResponse createIndustry(CreateIndustryRequest request) {
        if (request.getParentCode() != null && !request.getParentCode().isEmpty()) {
            if (!industryRepository.existsById(request.getParentCode())) {
                throw new IndustryNotFoundException(request.getParentCode());
            }
        }

        Industry industry = masterDataMapper.toEntity(request);
        Industry savedIndustry = industryRepository.save(industry);

        return IndustryResponse.fromEntity(savedIndustry);
    }

    @Transactional(readOnly = true)
    public IndustryResponse getIndustry(String code) {
        Industry industry = industryRepository.findById(code)
                .orElseThrow(() -> new IndustryNotFoundException(code));
        return IndustryResponse.fromEntity(industry);
    }

    @Transactional(readOnly = true)
    public List<IndustryResponse> getAllIndustries(boolean activeOnly) {
        List<Industry> industries;
        if (activeOnly) {
            industries = industryRepository.findByIsActiveTrue();
        } else {
            industries = industryRepository.findAll();
        }
        return industries.stream()
                .map(IndustryResponse::fromEntity)
                .toList();
    }
}

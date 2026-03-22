package com.banking.masterdata.service;

import com.banking.masterdata.domain.entity.Branch;
import com.banking.masterdata.domain.entity.Country;
import com.banking.masterdata.dto.request.CreateBranchRequest;
import com.banking.masterdata.dto.request.UpdateBranchRequest;
import com.banking.masterdata.dto.response.BranchResponse;
import com.banking.masterdata.exception.BranchAlreadyExistsException;
import com.banking.masterdata.exception.BranchNotFoundException;
import com.banking.masterdata.exception.CountryNotFoundException;
import com.banking.masterdata.mapper.MasterDataMapper;
import com.banking.masterdata.repository.BranchRepository;
import com.banking.masterdata.repository.CountryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BranchService {

    private final BranchRepository branchRepository;
    private final CountryRepository countryRepository;
    private final MasterDataMapper masterDataMapper;

    public BranchService(BranchRepository branchRepository, CountryRepository countryRepository, MasterDataMapper masterDataMapper) {
        this.branchRepository = branchRepository;
        this.countryRepository = countryRepository;
        this.masterDataMapper = masterDataMapper;
    }

    public BranchResponse createBranch(CreateBranchRequest request) {
        if (branchRepository.existsById(request.getCode())) {
            throw new BranchAlreadyExistsException(request.getCode());
        }

        Country country = countryRepository.findById(request.getCountryCode())
                .orElseThrow(() -> new CountryNotFoundException(request.getCountryCode()));

        Branch branch = new Branch(request.getCode(), request.getName(), country, request.getAddress());
        Branch savedBranch = branchRepository.save(branch);

        return masterDataMapper.toResponse(savedBranch);
    }

    @Transactional(readOnly = true)
    public BranchResponse getBranch(String code) {
        Branch branch = branchRepository.findById(code)
                .orElseThrow(() -> new BranchNotFoundException(code));
        return masterDataMapper.toResponse(branch);
    }

    @Transactional(readOnly = true)
    public List<BranchResponse> getActiveBranches(String countryCode) {
        List<Branch> branches;
        if (countryCode != null && !countryCode.isBlank()) {
            branches = branchRepository.findByIsActiveTrueAndCountryIsoCode(countryCode);
        } else {
            branches = branchRepository.findByIsActiveTrue();
        }
        return branches.stream()
                .map(masterDataMapper::toResponse)
                .toList();
    }

    public BranchResponse deactivateBranch(String code) {
        Branch branch = branchRepository.findById(code)
                .orElseThrow(() -> new BranchNotFoundException(code));

        branch.setActive(false);
        Branch savedBranch = branchRepository.save(branch);
        return masterDataMapper.toResponse(savedBranch);
    }

    public BranchResponse updateBranch(String code, UpdateBranchRequest request) {
        Branch branch = branchRepository.findById(code)
                .orElseThrow(() -> new BranchNotFoundException(code));

        if (request.getName() != null) {
            branch.setName(request.getName());
        }
        if (request.getAddress() != null) {
            branch.setAddress(request.getAddress());
        }

        Branch savedBranch = branchRepository.save(branch);
        return masterDataMapper.toResponse(savedBranch);
    }
}

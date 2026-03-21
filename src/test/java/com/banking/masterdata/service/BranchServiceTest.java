package com.banking.masterdata.service;

import com.banking.masterdata.domain.entity.Branch;
import com.banking.masterdata.domain.entity.Country;
import com.banking.masterdata.dto.request.CreateBranchRequest;
import com.banking.masterdata.dto.response.BranchResponse;
import com.banking.masterdata.exception.BranchAlreadyExistsException;
import com.banking.masterdata.exception.BranchNotFoundException;
import com.banking.masterdata.exception.CountryNotFoundException;
import com.banking.masterdata.mapper.MasterDataMapper;
import com.banking.masterdata.repository.BranchRepository;
import com.banking.masterdata.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BranchServiceTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private MasterDataMapper masterDataMapper;

    private BranchService branchService;

    @BeforeEach
    void setUp() {
        branchService = new BranchService(branchRepository, countryRepository, masterDataMapper);
    }

    private CreateBranchRequest createRequest(String code, String name, String countryCode, String address) {
        CreateBranchRequest request = new CreateBranchRequest();
        request.setCode(code);
        request.setName(name);
        request.setCountryCode(countryCode);
        request.setAddress(address);
        return request;
    }

    @Test
    void createBranch_success() {
        CreateBranchRequest request = createRequest("NYC-001", "New York Main", "US", "123 Wall St");
        Country country = new Country("US", "United States", "North America");
        Branch branch = new Branch("NYC-001", "New York Main", country, "123 Wall St");

        when(branchRepository.existsById("NYC-001")).thenReturn(false);
        when(countryRepository.findById("US")).thenReturn(Optional.of(country));
        when(branchRepository.save(any())).thenReturn(branch);
        when(masterDataMapper.toResponse(any(Branch.class))).thenReturn(BranchResponse.fromEntity(branch));

        BranchResponse response = branchService.createBranch(request);

        assertEquals("NYC-001", response.getCode());
        assertEquals("New York Main", response.getName());
        assertEquals("US", response.getCountryCode());
        assertTrue(response.isActive());

        verify(branchRepository).save(any());
    }

    @Test
    void createBranch_duplicateCode_throws() {
        CreateBranchRequest request = createRequest("NYC-001", "New York Main", "US", "123 Wall St");

        when(branchRepository.existsById("NYC-001")).thenReturn(true);

        assertThrows(BranchAlreadyExistsException.class, () ->
                branchService.createBranch(request));

        verify(branchRepository, never()).save(any());
    }

    @Test
    void createBranch_countryNotFound_throws() {
        CreateBranchRequest request = createRequest("NYC-001", "New York Main", "INVALID", "123 Wall St");

        when(branchRepository.existsById("NYC-001")).thenReturn(false);
        when(countryRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(CountryNotFoundException.class, () ->
                branchService.createBranch(request));

        verify(branchRepository, never()).save(any());
    }

    @Test
    void getBranch_success() {
        Country country = new Country("US", "United States", "North America");
        Branch branch = new Branch("NYC-001", "New York Main", country, "123 Wall St");

        when(branchRepository.findById("NYC-001")).thenReturn(Optional.of(branch));
        when(masterDataMapper.toResponse(any(Branch.class))).thenReturn(BranchResponse.fromEntity(branch));

        BranchResponse response = branchService.getBranch("NYC-001");

        assertEquals("NYC-001", response.getCode());
        assertEquals("New York Main", response.getName());
    }

    @Test
    void getBranch_notFound_throws() {
        when(branchRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(BranchNotFoundException.class, () ->
                branchService.getBranch("INVALID"));
    }

    @Test
    void getActiveBranches_filtersInactive() {
        Country country = new Country("US", "United States", "North America");
        Branch activeBranch = new Branch("NYC-001", "New York Main", country, "123 Wall St");
        Branch inactiveBranch = new Branch("NYC-002", "New York East", country, "456 Broadway");
        inactiveBranch.setActive(false);

        when(branchRepository.findByIsActiveTrue()).thenReturn(Arrays.asList(activeBranch));
        when(masterDataMapper.toResponse(activeBranch)).thenReturn(BranchResponse.fromEntity(activeBranch));

        List<BranchResponse> responses = branchService.getActiveBranches(null);

        assertEquals(1, responses.size());
        assertEquals("NYC-001", responses.get(0).getCode());
        verify(branchRepository).findByIsActiveTrue();
    }

    @Test
    void getActiveBranches_filtersByCountry() {
        Country country = new Country("US", "United States", "North America");
        Branch branch = new Branch("NYC-001", "New York Main", country, "123 Wall St");

        when(branchRepository.findByIsActiveTrueAndCountryCode("US")).thenReturn(Arrays.asList(branch));
        when(masterDataMapper.toResponse(branch)).thenReturn(BranchResponse.fromEntity(branch));

        List<BranchResponse> responses = branchService.getActiveBranches("US");

        assertEquals(1, responses.size());
        verify(branchRepository).findByIsActiveTrueAndCountryCode("US");
        verify(branchRepository, never()).findByIsActiveTrue();
    }

    @Test
    void deactivateBranch_success() {
        Country country = new Country("US", "United States", "North America");
        Branch activeBranch = new Branch("NYC-001", "New York Main", country, "123 Wall St");
        Branch deactivatedBranch = new Branch("NYC-001", "New York Main", country, "123 Wall St");
        deactivatedBranch.setActive(false);

        when(branchRepository.findById("NYC-001")).thenReturn(Optional.of(activeBranch));
        when(branchRepository.save(any())).thenReturn(deactivatedBranch);
        when(masterDataMapper.toResponse(deactivatedBranch)).thenReturn(BranchResponse.fromEntity(deactivatedBranch));

        BranchResponse response = branchService.deactivateBranch("NYC-001");

        assertEquals("NYC-001", response.getCode());
        assertFalse(response.isActive());
    }

    @Test
    void deactivateBranch_notFound_throws() {
        when(branchRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(BranchNotFoundException.class, () ->
                branchService.deactivateBranch("INVALID"));
    }
}

package com.banking.masterdata.service;

import com.banking.masterdata.domain.entity.Industry;
import com.banking.masterdata.dto.request.CreateIndustryRequest;
import com.banking.masterdata.dto.response.IndustryResponse;
import com.banking.masterdata.exception.IndustryNotFoundException;
import com.banking.masterdata.mapper.MasterDataMapper;
import com.banking.masterdata.repository.IndustryRepository;
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
class IndustryServiceTest {

    @Mock
    private IndustryRepository industryRepository;

    @Mock
    private MasterDataMapper masterDataMapper;

    private IndustryService industryService;

    @BeforeEach
    void setUp() {
        industryService = new IndustryService(industryRepository, masterDataMapper);
    }

    private CreateIndustryRequest createRequest(String code, String name, String parentCode) {
        CreateIndustryRequest request = new CreateIndustryRequest();
        request.setCode(code);
        request.setName(name);
        request.setParentCode(parentCode);
        return request;
    }

    @Test
    void createIndustry_success() {
        CreateIndustryRequest request = createRequest("64", "Financial Services", null);
        Industry industry = new Industry("64", "Financial Services", null);

        when(masterDataMapper.toEntity(any(CreateIndustryRequest.class))).thenReturn(industry);
        when(industryRepository.save(any(Industry.class))).thenReturn(industry);

        IndustryResponse response = industryService.createIndustry(request);

        assertEquals("64", response.getCode());
        assertEquals("Financial Services", response.getName());
        assertNull(response.getParentCode());
        assertTrue(response.isActive());

        verify(industryRepository).save(any(Industry.class));
    }

    @Test
    void createIndustry_withParent_success() {
        CreateIndustryRequest request = createRequest("641", "Banking", "64");
        Industry industry = new Industry("641", "Banking", "64");

        when(industryRepository.existsById("64")).thenReturn(true);
        when(masterDataMapper.toEntity(any(CreateIndustryRequest.class))).thenReturn(industry);
        when(industryRepository.save(any(Industry.class))).thenReturn(industry);

        IndustryResponse response = industryService.createIndustry(request);

        assertEquals("641", response.getCode());
        assertEquals("Banking", response.getName());
        assertEquals("64", response.getParentCode());
        assertTrue(response.isActive());

        verify(industryRepository).save(any(Industry.class));
    }

    @Test
    void createIndustry_withNonExistentParent_throws() {
        CreateIndustryRequest request = createRequest("641", "Banking", "99");

        when(industryRepository.existsById("99")).thenReturn(false);

        assertThrows(IndustryNotFoundException.class, () ->
                industryService.createIndustry(request));

        verify(industryRepository, never()).save(any());
    }

    @Test
    void getIndustry_success() {
        Industry industry = new Industry("64", "Financial Services", null);

        when(industryRepository.findById("64")).thenReturn(Optional.of(industry));

        IndustryResponse response = industryService.getIndustry("64");

        assertEquals("64", response.getCode());
        assertEquals("Financial Services", response.getName());
    }

    @Test
    void getIndustry_notFound_throws() {
        when(industryRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(IndustryNotFoundException.class, () ->
                industryService.getIndustry("INVALID"));
    }

    @Test
    void getAllIndustries_activeOnly_excludesInactive() {
        Industry active = new Industry("64", "Financial Services", null);
        Industry active2 = new Industry("62", "Insurance", null);

        when(industryRepository.findByIsActiveTrue()).thenReturn(Arrays.asList(active, active2));

        List<IndustryResponse> responses = industryService.getAllIndustries(true);

        assertEquals(2, responses.size());
        verify(industryRepository).findByIsActiveTrue();
        verify(industryRepository, never()).findAll();
    }

    @Test
    void getAllIndustries_all_includesInactive() {
        Industry active = new Industry("64", "Financial Services", null);
        Industry inactive = new Industry("00", "Inactive", null);
        inactive.setActive(false);

        when(industryRepository.findAll()).thenReturn(Arrays.asList(active, inactive));

        List<IndustryResponse> responses = industryService.getAllIndustries(false);

        assertEquals(2, responses.size());
        verify(industryRepository).findAll();
        verify(industryRepository, never()).findByIsActiveTrue();
    }
}

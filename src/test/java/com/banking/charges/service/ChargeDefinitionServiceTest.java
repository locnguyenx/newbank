package com.banking.charges.service;

import com.banking.charges.domain.entity.ChargeDefinition;
import com.banking.charges.domain.enums.ChargeStatus;
import com.banking.charges.domain.enums.ChargeType;
import com.banking.charges.dto.request.CreateChargeDefinitionRequest;
import com.banking.charges.dto.response.ChargeDefinitionResponse;
import com.banking.charges.exception.ChargeAlreadyExistsException;
import com.banking.charges.exception.ChargeNotFoundException;
import com.banking.charges.exception.InvalidChargeTypeException;
import com.banking.charges.mapper.ChargeMapper;
import com.banking.charges.repository.ChargeDefinitionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChargeDefinitionServiceTest {

    @Mock
    private ChargeDefinitionRepository chargeDefinitionRepository;

    @Mock
    private ChargeMapper chargeMapper;

    private ChargeDefinitionService chargeDefinitionService;

    @BeforeEach
    void setUp() {
        chargeDefinitionService = new ChargeDefinitionService(chargeDefinitionRepository, chargeMapper);
    }

    private CreateChargeDefinitionRequest createRequest(String name, String chargeType, String currency) {
        CreateChargeDefinitionRequest request = new CreateChargeDefinitionRequest();
        request.setName(name);
        request.setChargeType(chargeType);
        request.setCurrency(currency);
        return request;
    }

    @Test
    void createCharge_success() {
        CreateChargeDefinitionRequest request = createRequest("Monthly Fee", "MONTHLY_MAINTENANCE", "USD");
        ChargeDefinition entity = new ChargeDefinition("Monthly Fee", ChargeType.MONTHLY_MAINTENANCE, "USD");

        when(chargeDefinitionRepository.existsByName("Monthly Fee")).thenReturn(false);
        when(chargeMapper.toEntity(request)).thenReturn(entity);
        when(chargeDefinitionRepository.save(any())).thenReturn(entity);
        when(chargeMapper.toResponse(entity)).thenReturn(ChargeDefinitionResponse.fromEntity(entity));

        ChargeDefinitionResponse response = chargeDefinitionService.createCharge(request);

        assertEquals("Monthly Fee", response.getName());
        assertEquals("MONTHLY_MAINTENANCE", response.getChargeType());
        assertEquals("USD", response.getCurrency());
        assertEquals("ACTIVE", response.getStatus());

        verify(chargeDefinitionRepository).save(any());
    }

    @Test
    void createCharge_duplicateName_throws() {
        CreateChargeDefinitionRequest request = createRequest("Monthly Fee", "MONTHLY_MAINTENANCE", "USD");

        when(chargeDefinitionRepository.existsByName("Monthly Fee")).thenReturn(true);

        assertThrows(ChargeAlreadyExistsException.class, () ->
                chargeDefinitionService.createCharge(request));

        verify(chargeDefinitionRepository, never()).save(any());
    }

    @Test
    void createCharge_invalidType_throws() {
        CreateChargeDefinitionRequest request = createRequest("Bad Charge", "INVALID", "USD");

        when(chargeDefinitionRepository.existsByName("Bad Charge")).thenReturn(false);
        when(chargeMapper.toEntity(request)).thenThrow(new InvalidChargeTypeException("INVALID"));

        assertThrows(InvalidChargeTypeException.class, () ->
                chargeDefinitionService.createCharge(request));

        verify(chargeDefinitionRepository, never()).save(any());
    }

    @Test
    void getCharge_notFound_throws() {
        when(chargeDefinitionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ChargeNotFoundException.class, () ->
                chargeDefinitionService.getCharge(999L));
    }

    @Test
    void activateCharge_success() {
        ChargeDefinition inactiveEntity = new ChargeDefinition("Monthly Fee", ChargeType.MONTHLY_MAINTENANCE, "USD");
        inactiveEntity.setStatus(ChargeStatus.INACTIVE);
        ChargeDefinition activatedEntity = new ChargeDefinition("Monthly Fee", ChargeType.MONTHLY_MAINTENANCE, "USD");
        activatedEntity.setStatus(ChargeStatus.ACTIVE);

        when(chargeDefinitionRepository.findById(1L)).thenReturn(Optional.of(inactiveEntity));
        when(chargeDefinitionRepository.save(any())).thenReturn(activatedEntity);
        when(chargeMapper.toResponse(activatedEntity)).thenReturn(ChargeDefinitionResponse.fromEntity(activatedEntity));

        ChargeDefinitionResponse response = chargeDefinitionService.activateCharge(1L);

        assertEquals("ACTIVE", response.getStatus());
        verify(chargeDefinitionRepository).save(any());
    }

    @Test
    void deactivateCharge_success() {
        ChargeDefinition activeEntity = new ChargeDefinition("Monthly Fee", ChargeType.MONTHLY_MAINTENANCE, "USD");
        ChargeDefinition deactivatedEntity = new ChargeDefinition("Monthly Fee", ChargeType.MONTHLY_MAINTENANCE, "USD");
        deactivatedEntity.setStatus(ChargeStatus.INACTIVE);

        when(chargeDefinitionRepository.findById(1L)).thenReturn(Optional.of(activeEntity));
        when(chargeDefinitionRepository.save(any())).thenReturn(deactivatedEntity);
        when(chargeMapper.toResponse(deactivatedEntity)).thenReturn(ChargeDefinitionResponse.fromEntity(deactivatedEntity));

        ChargeDefinitionResponse response = chargeDefinitionService.deactivateCharge(1L);

        assertEquals("INACTIVE", response.getStatus());
        verify(chargeDefinitionRepository).save(any());
    }

    @Test
    void getAllCharges_withStatusFilter() {
        ChargeDefinition entity = new ChargeDefinition("Monthly Fee", ChargeType.MONTHLY_MAINTENANCE, "USD");
        List<ChargeDefinition> charges = List.of(entity);

        when(chargeDefinitionRepository.findByStatus(ChargeStatus.ACTIVE)).thenReturn(charges);
        when(chargeMapper.toResponse(any())).thenReturn(ChargeDefinitionResponse.fromEntity(entity));

        List<ChargeDefinitionResponse> response = chargeDefinitionService.getAllCharges(null, "ACTIVE");

        assertEquals(1, response.size());
        assertEquals("ACTIVE", response.get(0).getStatus());
    }

    @Test
    void getAllCharges_noFilter() {
        ChargeDefinition entity = new ChargeDefinition("Monthly Fee", ChargeType.MONTHLY_MAINTENANCE, "USD");
        List<ChargeDefinition> charges = List.of(entity);

        when(chargeDefinitionRepository.findAll()).thenReturn(charges);
        when(chargeMapper.toResponse(any())).thenReturn(ChargeDefinitionResponse.fromEntity(entity));

        List<ChargeDefinitionResponse> response = chargeDefinitionService.getAllCharges(null, null);

        assertEquals(1, response.size());
    }
}
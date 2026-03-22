package com.banking.charges.service;

import com.banking.charges.domain.entity.ChargeDefinition;
import com.banking.charges.domain.entity.FeeWaiver;
import com.banking.charges.domain.enums.ChargeType;
import com.banking.charges.domain.enums.WaiverScope;
import com.banking.charges.dto.request.CreateFeeWaiverRequest;
import com.banking.charges.dto.response.FeeWaiverResponse;
import com.banking.charges.exception.ChargeNotFoundException;
import com.banking.charges.exception.WaiverAlreadyExistsException;
import com.banking.charges.exception.WaiverNotFoundException;
import com.banking.charges.repository.ChargeDefinitionRepository;
import com.banking.charges.repository.FeeWaiverRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeeWaiverServiceTest {

    @Mock
    private FeeWaiverRepository feeWaiverRepository;

    @Mock
    private ChargeDefinitionRepository chargeDefinitionRepository;

    private FeeWaiverService feeWaiverService;

    @BeforeEach
    void setUp() {
        feeWaiverService = new FeeWaiverService(feeWaiverRepository, chargeDefinitionRepository);
    }

    @Test
    void createWaiver_success() {
        ChargeDefinition charge = new ChargeDefinition("Transfer Fee", ChargeType.TRANSACTION_FEE, "USD");
        charge.setId(1L);

        CreateFeeWaiverRequest request = new CreateFeeWaiverRequest();
        request.setChargeId(1L);
        request.setScope("CUSTOMER");
        request.setReferenceId("CUSTOMER_002");
        request.setWaiverPercentage(50);
        request.setValidFrom(LocalDate.now());
        request.setValidTo(LocalDate.now().plusYears(1));

        when(chargeDefinitionRepository.findById(1L)).thenReturn(Optional.of(charge));
        when(feeWaiverRepository.findByChargeDefinitionIdAndScopeAndReferenceId(
                1L, WaiverScope.CUSTOMER, "CUSTOMER_002")).thenReturn(Optional.empty());
        when(feeWaiverRepository.save(any())).thenAnswer(inv -> {
            FeeWaiver waiver = inv.getArgument(0);
            waiver.setId(1L);
            return waiver;
        });

        FeeWaiverResponse response = feeWaiverService.createWaiver(request);

        assertEquals("CUSTOMER", response.getScope());
        assertEquals("CUSTOMER_002", response.getReferenceId());
        assertEquals(50, response.getWaiverPercentage());
    }

    @Test
    void createWaiver_chargeNotFound_throws() {
        CreateFeeWaiverRequest request = new CreateFeeWaiverRequest();
        request.setChargeId(999L);
        request.setScope("CUSTOMER");
        request.setReferenceId("CUSTOMER_002");
        request.setWaiverPercentage(50);
        request.setValidFrom(LocalDate.now());

        when(chargeDefinitionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ChargeNotFoundException.class, () -> feeWaiverService.createWaiver(request));
    }

    @Test
    void createWaiver_alreadyExists_throws() {
        ChargeDefinition charge = new ChargeDefinition("Transfer Fee", ChargeType.TRANSACTION_FEE, "USD");
        charge.setId(1L);

        FeeWaiver existing = new FeeWaiver(charge, WaiverScope.CUSTOMER, "CUSTOMER_002",
                50, LocalDate.now(), LocalDate.now().plusYears(1));
        existing.setId(1L);

        CreateFeeWaiverRequest request = new CreateFeeWaiverRequest();
        request.setChargeId(1L);
        request.setScope("CUSTOMER");
        request.setReferenceId("CUSTOMER_002");
        request.setWaiverPercentage(50);
        request.setValidFrom(LocalDate.now());

        when(chargeDefinitionRepository.findById(1L)).thenReturn(Optional.of(charge));
        when(feeWaiverRepository.findByChargeDefinitionIdAndScopeAndReferenceId(
                1L, WaiverScope.CUSTOMER, "CUSTOMER_002")).thenReturn(Optional.of(existing));

        assertThrows(WaiverAlreadyExistsException.class, () -> feeWaiverService.createWaiver(request));
    }

    @Test
    void removeWaiver_success() {
        when(feeWaiverRepository.existsById(1L)).thenReturn(true);

        feeWaiverService.removeWaiver(1L);

        verify(feeWaiverRepository).deleteById(1L);
    }

    @Test
    void removeWaiver_notFound_throws() {
        when(feeWaiverRepository.existsById(999L)).thenReturn(false);

        assertThrows(WaiverNotFoundException.class, () -> feeWaiverService.removeWaiver(999L));
    }

    @Test
    void getApplicableWaivers_customerScope_success() {
        ChargeDefinition charge = new ChargeDefinition("Transfer Fee", ChargeType.TRANSACTION_FEE, "USD");
        charge.setId(1L);

        FeeWaiver waiver = new FeeWaiver(charge, WaiverScope.CUSTOMER, "123",
                50, LocalDate.now().minusDays(1), LocalDate.now().plusDays(30));
        waiver.setId(1L);

        when(feeWaiverRepository.findByChargeDefinitionId(1L)).thenReturn(List.of(waiver));

        List<FeeWaiverResponse> result = feeWaiverService.getApplicableWaivers(
                1L, 123L, null, null, LocalDate.now());

        assertEquals(1, result.size());
        assertEquals("CUSTOMER", result.get(0).getScope());
    }

    @Test
    void getApplicableWaivers_inactiveOnDate_returnsEmpty() {
        ChargeDefinition charge = new ChargeDefinition("Transfer Fee", ChargeType.TRANSACTION_FEE, "USD");
        charge.setId(1L);

        FeeWaiver waiver = new FeeWaiver(charge, WaiverScope.CUSTOMER, "123",
                50, LocalDate.now().minusDays(30), LocalDate.now().minusDays(1));
        waiver.setId(1L);

        when(feeWaiverRepository.findByChargeDefinitionId(1L)).thenReturn(List.of(waiver));

        List<FeeWaiverResponse> result = feeWaiverService.getApplicableWaivers(
                1L, 123L, null, null, LocalDate.now());

        assertEquals(0, result.size());
    }
}

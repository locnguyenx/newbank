package com.banking.masterdata.service;

import com.banking.masterdata.domain.entity.DocumentType;
import com.banking.masterdata.dto.request.CreateDocumentTypeRequest;
import com.banking.masterdata.dto.response.DocumentTypeResponse;
import com.banking.masterdata.exception.DocumentTypeAlreadyExistsException;
import com.banking.masterdata.exception.DocumentTypeNotFoundException;
import com.banking.masterdata.mapper.MasterDataMapper;
import com.banking.masterdata.repository.DocumentTypeRepository;
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
class DocumentTypeServiceTest {

    @Mock
    private DocumentTypeRepository documentTypeRepository;

    @Mock
    private MasterDataMapper masterDataMapper;

    private DocumentTypeService documentTypeService;

    @BeforeEach
    void setUp() {
        documentTypeService = new DocumentTypeService(documentTypeRepository, masterDataMapper);
    }

    private CreateDocumentTypeRequest createRequest(String code, String name, String category) {
        CreateDocumentTypeRequest request = new CreateDocumentTypeRequest();
        request.setCode(code);
        request.setName(name);
        request.setCategory(category);
        return request;
    }

    // BDD S8.1: createDocumentType_success
    @Test
    void createDocumentType_success() {
        CreateDocumentTypeRequest request = createRequest("PASSPORT", "Passport", "IDENTITY");
        DocumentType documentType = new DocumentType("PASSPORT", "Passport", "IDENTITY");

        when(documentTypeRepository.existsById("PASSPORT")).thenReturn(false);
        when(masterDataMapper.toEntity(any(CreateDocumentTypeRequest.class))).thenReturn(documentType);
        when(documentTypeRepository.save(any())).thenReturn(documentType);

        DocumentTypeResponse expected = DocumentTypeResponse.fromEntity(documentType);
        when(masterDataMapper.toResponse(any(DocumentType.class))).thenReturn(expected);

        DocumentTypeResponse response = documentTypeService.createDocumentType(request);

        assertEquals("PASSPORT", response.getCode());
        assertEquals("Passport", response.getName());
        assertEquals("IDENTITY", response.getCategory());
        assertTrue(response.isActive());

        verify(documentTypeRepository).save(any());
    }

    // BDD S8.2: createDocumentType_duplicate_throws
    @Test
    void createDocumentType_duplicate_throws() {
        CreateDocumentTypeRequest request = createRequest("PASSPORT", "Passport", "IDENTITY");

        when(documentTypeRepository.existsById("PASSPORT")).thenReturn(true);

        assertThrows(DocumentTypeAlreadyExistsException.class, () ->
                documentTypeService.createDocumentType(request));

        verify(documentTypeRepository, never()).save(any());
    }

    // BDD S8.3: getActiveDocumentTypes_byCategory
    @Test
    void getActiveDocumentTypes_byCategory() {
        DocumentType passport = new DocumentType("PASSPORT", "Passport", "IDENTITY");
        DocumentType nationalId = new DocumentType("NATIONAL_ID", "National ID", "IDENTITY");

        when(documentTypeRepository.findByIsActiveTrueAndCategory("IDENTITY"))
                .thenReturn(Arrays.asList(passport, nationalId));
        when(masterDataMapper.toResponse(passport)).thenReturn(DocumentTypeResponse.fromEntity(passport));
        when(masterDataMapper.toResponse(nationalId)).thenReturn(DocumentTypeResponse.fromEntity(nationalId));

        List<DocumentTypeResponse> responses = documentTypeService.getActiveDocumentTypes("IDENTITY");

        assertEquals(2, responses.size());
        verify(documentTypeRepository).findByIsActiveTrueAndCategory("IDENTITY");
        verify(documentTypeRepository, never()).findByIsActiveTrue();
    }

    // BDD S8.4: getActiveDocumentTypes_noMatch_returnsEmpty
    @Test
    void getActiveDocumentTypes_noMatch_returnsEmpty() {
        when(documentTypeRepository.findByIsActiveTrueAndCategory("CORPORATE"))
                .thenReturn(List.of());

        List<DocumentTypeResponse> responses = documentTypeService.getActiveDocumentTypes("CORPORATE");

        assertTrue(responses.isEmpty());
        verify(documentTypeRepository).findByIsActiveTrueAndCategory("CORPORATE");
    }
}

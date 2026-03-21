package com.banking.masterdata.service;

import com.banking.masterdata.domain.entity.DocumentType;
import com.banking.masterdata.dto.request.CreateDocumentTypeRequest;
import com.banking.masterdata.dto.response.DocumentTypeResponse;
import com.banking.masterdata.exception.DocumentTypeAlreadyExistsException;
import com.banking.masterdata.exception.DocumentTypeNotFoundException;
import com.banking.masterdata.mapper.MasterDataMapper;
import com.banking.masterdata.repository.DocumentTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DocumentTypeService {

    private final DocumentTypeRepository documentTypeRepository;
    private final MasterDataMapper masterDataMapper;

    public DocumentTypeService(DocumentTypeRepository documentTypeRepository, MasterDataMapper masterDataMapper) {
        this.documentTypeRepository = documentTypeRepository;
        this.masterDataMapper = masterDataMapper;
    }

    public DocumentTypeResponse createDocumentType(CreateDocumentTypeRequest request) {
        if (documentTypeRepository.existsById(request.getCode())) {
            throw new DocumentTypeAlreadyExistsException(request.getCode());
        }

        DocumentType documentType = masterDataMapper.toEntity(request);
        DocumentType savedDocumentType = documentTypeRepository.save(documentType);

        return masterDataMapper.toResponse(savedDocumentType);
    }

    @Transactional(readOnly = true)
    public List<DocumentTypeResponse> getActiveDocumentTypes(String category) {
        List<DocumentType> documentTypes;
        if (category != null) {
            documentTypes = documentTypeRepository.findByIsActiveTrueAndCategory(category);
        } else {
            documentTypes = documentTypeRepository.findByIsActiveTrue();
        }
        return documentTypes.stream()
                .map(masterDataMapper::toResponse)
                .toList();
    }

    public DocumentTypeResponse deactivateDocumentType(String code) {
        DocumentType documentType = documentTypeRepository.findById(code)
                .orElseThrow(() -> new DocumentTypeNotFoundException(code));

        documentType.setActive(false);
        DocumentType savedDocumentType = documentTypeRepository.save(documentType);
        return masterDataMapper.toResponse(savedDocumentType);
    }
}

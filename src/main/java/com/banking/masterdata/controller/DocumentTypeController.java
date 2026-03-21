package com.banking.masterdata.controller;

import com.banking.masterdata.dto.request.CreateDocumentTypeRequest;
import com.banking.masterdata.dto.response.DocumentTypeResponse;
import com.banking.masterdata.service.DocumentTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master-data/document-types")
public class DocumentTypeController {

    private final DocumentTypeService documentTypeService;

    public DocumentTypeController(DocumentTypeService documentTypeService) {
        this.documentTypeService = documentTypeService;
    }

    @PostMapping
    public ResponseEntity<DocumentTypeResponse> createDocumentType(@Valid @RequestBody CreateDocumentTypeRequest request) {
        DocumentTypeResponse response = documentTypeService.createDocumentType(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DocumentTypeResponse>> getActiveDocumentTypes(
            @RequestParam(required = false) String category) {
        List<DocumentTypeResponse> response = documentTypeService.getActiveDocumentTypes(category);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{code}/deactivate")
    public ResponseEntity<DocumentTypeResponse> deactivateDocumentType(@PathVariable String code) {
        DocumentTypeResponse response = documentTypeService.deactivateDocumentType(code);
        return ResponseEntity.ok(response);
    }
}

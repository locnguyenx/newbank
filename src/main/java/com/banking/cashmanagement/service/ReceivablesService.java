package com.banking.cashmanagement.service;

import com.banking.cashmanagement.domain.entity.ReceivableInvoice;
import com.banking.cashmanagement.domain.enums.InvoiceStatus;
import com.banking.cashmanagement.dto.ReceivableInvoiceRequest;
import com.banking.cashmanagement.dto.ReceivableInvoiceResponse;
import com.banking.cashmanagement.exception.ReceivableInvoiceNotFoundException;
import com.banking.cashmanagement.integration.CustomerServiceAdapter;
import com.banking.cashmanagement.repository.ReceivableInvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReceivablesService {
    
    private final ReceivableInvoiceRepository receivableInvoiceRepository;
    private final CustomerServiceAdapter customerServiceAdapter;
    
    public ReceivablesService(ReceivableInvoiceRepository receivableInvoiceRepository,
                              CustomerServiceAdapter customerServiceAdapter) {
        this.receivableInvoiceRepository = receivableInvoiceRepository;
        this.customerServiceAdapter = customerServiceAdapter;
    }
    
    public ReceivableInvoiceResponse createInvoice(ReceivableInvoiceRequest request) {
        if (!customerServiceAdapter.isValidCustomer(request.getCustomerId())) {
            throw new IllegalArgumentException("Invalid customer: " + request.getCustomerId());
        }
        
        ReceivableInvoice invoice = new ReceivableInvoice();
        invoice.setInvoiceNumber(generateInvoiceNumber());
        invoice.setCustomerId(request.getCustomerId());
        invoice.setBillToCustomerId(request.getBillToCustomerId());
        invoice.setAmount(request.getAmount());
        invoice.setCurrency(request.getCurrency());
        invoice.setIssueDate(request.getIssueDate());
        invoice.setDueDate(request.getDueDate());
        invoice.setStatus(InvoiceStatus.DRAFT);
        invoice.setBalanceDue(request.getAmount());
        invoice.setReferenceNumber(request.getReferenceNumber());
        invoice.setDescription(request.getDescription());
        
        ReceivableInvoice saved = receivableInvoiceRepository.save(invoice);
        return mapToResponse(saved);
    }
    
    @Transactional(readOnly = true)
    public ReceivableInvoiceResponse getInvoice(Long id) {
        ReceivableInvoice invoice = receivableInvoiceRepository.findById(id)
            .orElseThrow(() -> ReceivableInvoiceNotFoundException.forId(id));
        return mapToResponse(invoice);
    }
    
    @Transactional(readOnly = true)
    public List<ReceivableInvoiceResponse> listInvoices(Long customerId) {
        List<ReceivableInvoice> invoices;
        if (customerId != null) {
            invoices = receivableInvoiceRepository.findByCustomerId(customerId);
        } else {
            invoices = receivableInvoiceRepository.findAll();
        }
        return invoices.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    public ReceivableInvoiceResponse issueInvoice(Long id) {
        ReceivableInvoice invoice = receivableInvoiceRepository.findById(id)
            .orElseThrow(() -> ReceivableInvoiceNotFoundException.forId(id));
        invoice.setStatus(InvoiceStatus.ISSUED);
        ReceivableInvoice saved = receivableInvoiceRepository.save(invoice);
        return mapToResponse(saved);
    }
    
    private String generateInvoiceNumber() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return "INV-" + date + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
    
    private ReceivableInvoiceResponse mapToResponse(ReceivableInvoice invoice) {
        ReceivableInvoiceResponse response = new ReceivableInvoiceResponse();
        response.setId(invoice.getId());
        response.setInvoiceNumber(invoice.getInvoiceNumber());
        response.setCustomerId(invoice.getCustomerId());
        response.setBillToCustomerId(invoice.getBillToCustomerId());
        response.setAmount(invoice.getAmount());
        response.setCurrency(invoice.getCurrency());
        response.setIssueDate(invoice.getIssueDate());
        response.setDueDate(invoice.getDueDate());
        response.setStatus(invoice.getStatus());
        response.setBalanceDue(invoice.getBalanceDue());
        response.setReferenceNumber(invoice.getReferenceNumber());
        response.setDescription(invoice.getDescription());
        return response;
    }
}

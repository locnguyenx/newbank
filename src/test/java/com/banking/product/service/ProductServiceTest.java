package com.banking.product.service;

import com.banking.product.domain.entity.Product;
import com.banking.product.domain.entity.ProductVersion;
import com.banking.product.domain.embeddable.AuditFields;
import com.banking.product.domain.enums.ProductFamily;
import com.banking.product.domain.enums.ProductStatus;
import com.banking.product.dto.request.CreateProductRequest;
import com.banking.product.dto.request.UpdateProductRequest;
import com.banking.product.dto.response.ProductResponse;
import com.banking.product.exception.DuplicateProductCodeException;
import com.banking.product.exception.InvalidProductStatusException;
import com.banking.product.exception.ProductNotFoundException;
import com.banking.product.mapper.ProductMapper;
import com.banking.product.repository.ProductRepository;
import com.banking.product.repository.ProductVersionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductVersionRepository productVersionRepository;

    @Mock
    private ProductSegmentService productSegmentService;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository, productMapper, productVersionRepository, productSegmentService);
    }

    private CreateProductRequest req(String code) {
        CreateProductRequest request = new CreateProductRequest();
        request.setCode(code);
        request.setName("Test Product");
        request.setFamily(ProductFamily.ACCOUNT);
        request.setDescription("Test description");
        return request;
    }

    @Test
    void createProduct_createsDraftWithVersion1() {
        CreateProductRequest request = new CreateProductRequest();
        request.setCode("BIZ-CURRENT");
        request.setName("Business Current Account");
        request.setFamily(ProductFamily.ACCOUNT);
        request.setDescription("Test product");

        Product savedProduct = new Product("BIZ-CURRENT", "Business Current Account", ProductFamily.ACCOUNT, "Test product");

        ProductResponse expectedResponse = new ProductResponse();
        expectedResponse.setCode("BIZ-CURRENT");
        expectedResponse.setName("Business Current Account");
        expectedResponse.setFamily("ACCOUNT");
        expectedResponse.setStatus(ProductStatus.DRAFT.name());
        expectedResponse.setVersionNumber(1L);

        when(productRepository.existsByCode("BIZ-CURRENT")).thenReturn(false);
        when(productMapper.toEntity(any())).thenReturn(savedProduct);
        when(productRepository.save(any())).thenReturn(savedProduct);
        when(productVersionRepository.save(any())).thenReturn(new ProductVersion(savedProduct, 1, ProductStatus.DRAFT));
        when(productMapper.toResponse(any())).thenReturn(expectedResponse);

        ProductResponse response = productService.createProduct(request, "alice");

        assertEquals("BIZ-CURRENT", response.getCode());
        assertEquals(ProductStatus.DRAFT.name(), response.getStatus());
        assertEquals(1, response.getVersionNumber());
    }

    @Test
    void createProduct_duplicateCode_throwsException() {
        when(productRepository.existsByCode("BIZ-CURRENT")).thenReturn(true);

        assertThrows(DuplicateProductCodeException.class, () ->
            productService.createProduct(req("BIZ-CURRENT"), "alice"));
        
        verify(productRepository, never()).save(any());
    }

    @Test
    void createProduct_withCustomerTypes_callsAssignSegments() {
        CreateProductRequest request = new CreateProductRequest();
        request.setCode("NEW-PROD");
        request.setName("New Product");
        request.setFamily(ProductFamily.ACCOUNT);
        request.setCustomerTypes(List.of(
            com.banking.product.domain.enums.CustomerType.CORPORATE,
            com.banking.product.domain.enums.CustomerType.INDIVIDUAL
        ));

        Product savedProduct = mock(Product.class);
        ProductVersion savedVersion = mock(ProductVersion.class);
        AuditFields auditFields = mock(AuditFields.class);

        lenient().when(productRepository.existsByCode("NEW-PROD")).thenReturn(false);
        lenient().when(productMapper.toEntity(any())).thenReturn(savedProduct);
        lenient().when(savedProduct.getAudit()).thenReturn(auditFields);
        lenient().when(productRepository.save(any())).thenReturn(savedProduct);
        lenient().when(productVersionRepository.save(any())).thenReturn(savedVersion);
        lenient().when(productMapper.toResponse(any())).thenReturn(new ProductResponse());

        productService.createProduct(request, "alice");

        verify(productSegmentService).assignSegments(
            any(),
            eq(request.getCustomerTypes()),
            eq("alice")
        );
    }

    @Test
    void updateProduct_draftVersion_updatesInPlace() {
        Product existingProduct = new Product("BIZ-CURRENT", "Business Current Account", ProductFamily.ACCOUNT, "Test product");
        ProductVersion existingVersion = new ProductVersion(existingProduct, 1, ProductStatus.DRAFT);

        Product updatedProduct = new Product("BIZ-CURRENT", "Updated Name", ProductFamily.ACCOUNT, "Test product");

        ProductResponse expectedResponse = new ProductResponse();
        expectedResponse.setCode("BIZ-CURRENT");
        expectedResponse.setName("Updated Name");
        expectedResponse.setStatus(ProductStatus.DRAFT.name());
        expectedResponse.setVersionNumber(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productVersionRepository.findTopByProductIdOrderByVersionNumberDesc(1L)).thenReturn(Optional.of(existingVersion));
        when(productRepository.save(any())).thenReturn(updatedProduct);
        when(productMapper.toResponse(any())).thenReturn(expectedResponse);

        UpdateProductRequest upd = new UpdateProductRequest();
        upd.setName("Updated Name");

        ProductResponse updated = productService.updateProduct(1L, upd, "alice");

        assertEquals("Updated Name", updated.getName());
        assertEquals(1, updated.getVersionNumber());
    }

    @Test
    void getProduct_returnsProduct() {
        Product product = new Product("BIZ-CURRENT", "Business Current Account", ProductFamily.ACCOUNT, "Test product");

        ProductResponse expectedResponse = new ProductResponse();
        expectedResponse.setCode("BIZ-CURRENT");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toResponse(product)).thenReturn(expectedResponse);

        ProductResponse response = productService.getProduct(1L);

        assertEquals("BIZ-CURRENT", response.getCode());
    }

    @Test
    void getProduct_notFound_throwsException() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProduct(999L));
    }

    @Test
    void getProductByCode_returnsProduct() {
        Product product = new Product("BIZ-CURRENT", "Business Current Account", ProductFamily.ACCOUNT, "Test product");

        ProductResponse expectedResponse = new ProductResponse();
        expectedResponse.setCode("BIZ-CURRENT");

        when(productRepository.findByCode("BIZ-CURRENT")).thenReturn(Optional.of(product));
        when(productMapper.toResponse(product)).thenReturn(expectedResponse);

        ProductResponse response = productService.getProductByCode("BIZ-CURRENT");

        assertEquals("BIZ-CURRENT", response.getCode());
    }

    @Test
    void getProductByCode_notFound_throwsException() {
        when(productRepository.findByCode("INVALID")).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductByCode("INVALID"));
    }

    @Test
    void searchProducts_returnsPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Product product = new Product("BIZ-CURRENT", "Business Current Account", ProductFamily.ACCOUNT, "Test product");
        List<Product> products = new ArrayList<>();
        products.add(product);
        Page<Product> productPage = new PageImpl<>(products, pageable, products.size());

        ProductResponse expectedResponse = new ProductResponse();
        expectedResponse.setCode("BIZ-CURRENT");

        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(productMapper.toResponse(any())).thenReturn(expectedResponse);

        Page<ProductResponse> response = productService.searchProducts(null, pageable);

        assertEquals(1, response.getTotalElements());
    }

    @Test
    void createProduct_duplicateCode_hasCorrectErrorCode() {
        when(productRepository.existsByCode("BIZ-CURRENT")).thenReturn(true);

        try {
            productService.createProduct(req("BIZ-CURRENT"), "alice");
            fail("Expected DuplicateProductCodeException");
        } catch (DuplicateProductCodeException e) {
            assertEquals(DuplicateProductCodeException.ERROR_CODE, e.getErrorCode());
        }
    }

    @Test
    void updateProduct_nonDraft_throwsException() {
        Product existingProduct = new Product("BIZ-CURRENT", "Business Current Account", ProductFamily.ACCOUNT, "Test product");
        ProductVersion approvedVersion = new ProductVersion(existingProduct, 1, ProductStatus.APPROVED);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productVersionRepository.findTopByProductIdOrderByVersionNumberDesc(1L)).thenReturn(Optional.of(approvedVersion));

        UpdateProductRequest upd = new UpdateProductRequest();
        upd.setName("New Name");

        assertThrows(InvalidProductStatusException.class, () ->
            productService.updateProduct(1L, upd, "alice"));
    }

    @Test
    void updateProduct_changeCode_throwsException() {
        Product existingProduct = new Product("BIZ-CURRENT", "Business Current Account", ProductFamily.ACCOUNT, "Test product");

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));

        UpdateProductRequest upd = new UpdateProductRequest();
        upd.setCode("NEW-CODE");

        assertThrows(InvalidProductStatusException.class, () ->
            productService.updateProduct(1L, upd, "alice"));
    }

    @Test
    void updateProduct_withCustomerTypes_callsAssignSegments() {
        Product existingProduct = mock(Product.class);
        ProductVersion draftVersion = mock(ProductVersion.class);
        AuditFields auditFields = mock(AuditFields.class);
        
        lenient().when(existingProduct.getCode()).thenReturn("BIZ-CURRENT");
        lenient().when(existingProduct.getAudit()).thenReturn(auditFields);
        lenient().when(draftVersion.getStatus()).thenReturn(ProductStatus.DRAFT);
        lenient().when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        lenient().when(productVersionRepository.findTopByProductIdOrderByVersionNumberDesc(1L)).thenReturn(Optional.of(draftVersion));
        lenient().when(productRepository.save(any())).thenReturn(existingProduct);

        UpdateProductRequest upd = new UpdateProductRequest();
        upd.setName("Updated Name");
        upd.setCustomerTypes(List.of(
            com.banking.product.domain.enums.CustomerType.CORPORATE,
            com.banking.product.domain.enums.CustomerType.SME
        ));

        productService.updateProduct(1L, upd, "alice");

        verify(productSegmentService).assignSegments(
            any(),
            eq(upd.getCustomerTypes()),
            eq("alice")
        );
    }
}
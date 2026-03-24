package com.banking.product.controller;

import com.banking.product.domain.enums.ProductFamily;
import com.banking.product.domain.enums.ProductStatus;
import com.banking.product.dto.request.CreateProductRequest;
import com.banking.product.dto.request.UpdateProductRequest;
import com.banking.product.dto.response.ProductResponse;
import com.banking.product.exception.DuplicateProductCodeException;
import com.banking.product.exception.ProductNotFoundException;
import com.banking.product.exception.InvalidProductStatusException;
import com.banking.product.mapper.ProductMapper;
import com.banking.product.service.ProductService;
import com.banking.product.service.ProductVersionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.banking.common.security.config.TestSecurityConfig;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class)
@ContextConfiguration(classes = {ProductController.class, ProductExceptionHandler.class})
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductVersionService productVersionService;

    @MockBean
    private ProductMapper productMapper;

    @Test
    void shouldCreateProduct() throws Exception {
        CreateProductRequest request = createProductRequest();
        ProductResponse response = createProductResponse();

        when(productService.createProduct(any(), eq("testuser"))).thenReturn(response);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Username", "testuser")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.code").value("CUR-001"))
                .andExpect(jsonPath("$.name").value("Current Account"))
                .andExpect(jsonPath("$.family").value("ACCOUNT"))
                .andExpect(jsonPath("$.status").value("DRAFT"));
    }

    @Test
    void shouldReturn409WhenDuplicateProductCode() throws Exception {
        CreateProductRequest request = createProductRequest();

        when(productService.createProduct(any(), any()))
                .thenThrow(new DuplicateProductCodeException("CUR-001"));

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Username", "testuser")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.messageCode").value("PROD-003"));
    }

    @Test
    void shouldReturn400WhenCreatingProductWithMissingCode() throws Exception {
        CreateProductRequest request = createProductRequest();
        request.setCode(null);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Username", "testuser")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messageCode").value("VALIDATION_ERROR"));
    }

    @Test
    void shouldReturn400WhenCreatingProductWithMissingName() throws Exception {
        CreateProductRequest request = createProductRequest();
        request.setName(null);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Username", "testuser")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messageCode").value("VALIDATION_ERROR"));
    }

    @Test
    void shouldReturn400WhenCreatingProductWithMissingFamily() throws Exception {
        CreateProductRequest request = createProductRequest();
        request.setFamily(null);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Username", "testuser")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messageCode").value("VALIDATION_ERROR"));
    }

    @Test
    void shouldGetProduct() throws Exception {
        ProductResponse response = createProductResponse();

        when(productService.getProduct(1L)).thenReturn(response);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.code").value("CUR-001"))
                .andExpect(jsonPath("$.name").value("Current Account"));
    }

    @Test
    void shouldReturn404WhenProductNotFound() throws Exception {
        when(productService.getProduct(999L)).thenThrow(new ProductNotFoundException("999"));

        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messageCode").value("PROD-001"));
    }

    @Test
    void shouldGetProductByCode() throws Exception {
        ProductResponse response = createProductResponse();

        when(productService.getProductByCode("CUR-001")).thenReturn(response);

        mockMvc.perform(get("/api/products/code/CUR-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("CUR-001"));
    }

    @Test
    void shouldReturn404WhenProductCodeNotFound() throws Exception {
        when(productService.getProductByCode("INVALID")).thenThrow(new ProductNotFoundException("INVALID"));

        mockMvc.perform(get("/api/products/code/INVALID"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messageCode").value("PROD-001"));
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Updated Name");
        request.setDescription("Updated Description");

        ProductResponse response = createProductResponse();
        response.setName("Updated Name");

        when(productService.updateProduct(eq(1L), any(), eq("testuser"))).thenReturn(response);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Username", "testuser")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistentProduct() throws Exception {
        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Updated Name");

        when(productService.updateProduct(eq(999L), any(), any()))
                .thenThrow(new ProductNotFoundException("999"));

        mockMvc.perform(put("/api/products/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Username", "testuser")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messageCode").value("PROD-001"));
    }

    @Test
    void shouldReturn400WhenUpdatingNonDraftProduct() throws Exception {
        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Updated Name");

        when(productService.updateProduct(eq(1L), any(), any()))
                .thenThrow(new InvalidProductStatusException("Only DRAFT version can be updated"));

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Username", "testuser")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messageCode").value("PROD-004"));
    }

    @Test
    void shouldSearchProducts() throws Exception {
        when(productService.searchProducts(any(), any())).thenReturn(new org.springframework.data.domain.PageImpl<>(java.util.List.of()));

        mockMvc.perform(get("/api/products/search")
                        .param("family", "CURRENT")
                        .param("status", "ACTIVE"))
                .andExpect(status().isOk());
    }

    private CreateProductRequest createProductRequest() {
        CreateProductRequest request = new CreateProductRequest();
        request.setCode("CUR-001");
        request.setName("Current Account");
        request.setFamily(ProductFamily.ACCOUNT);
        request.setDescription("Standard current account");
        return request;
    }

    private ProductResponse createProductResponse() {
        ProductResponse response = new ProductResponse();
        response.setId(1L);
        response.setCode("CUR-001");
        response.setName("Current Account");
        response.setFamily("ACCOUNT");
        response.setStatus("DRAFT");
        response.setVersionNumber(1L);
        return response;
    }
}
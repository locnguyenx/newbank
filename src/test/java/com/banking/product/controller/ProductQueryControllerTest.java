package com.banking.product.controller;

import com.banking.product.domain.enums.ProductFamily;
import com.banking.product.domain.enums.ProductStatus;
import com.banking.product.dto.response.ProductVersionResponse;
import com.banking.product.service.ProductQueryServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.banking.common.security.config.TestSecurityConfig;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductQueryController.class)
@ContextConfiguration(classes = {ProductQueryController.class, ProductExceptionHandler.class})
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class ProductQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductQueryServiceImpl productQueryService;

    @Test
    void shouldGetActiveProductByCode() throws Exception {
        ProductVersionResponse response = createVersionResponse();
        response.setStatus("ACTIVE");

        when(productQueryService.getActiveProductByCode("CUR-001")).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/product-query/active")
                        .param("code", "CUR-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldReturn404WhenActiveProductNotFound() throws Exception {
        when(productQueryService.getActiveProductByCode("INVALID")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/product-query/active")
                        .param("code", "INVALID"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetProductVersionById() throws Exception {
        ProductVersionResponse response = createVersionResponse();

        when(productQueryService.getProductVersionById(1L)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/product-query/version/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldReturn404WhenVersionNotFound() throws Exception {
        when(productQueryService.getProductVersionById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/product-query/version/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetProductsByFamily() throws Exception {
        List<ProductVersionResponse> responses = List.of(createVersionResponse());

        when(productQueryService.getActiveProductsByFamily(com.banking.product.domain.enums.ProductFamily.ACCOUNT)).thenReturn(responses);

        mockMvc.perform(get("/api/product-query/family/ACCOUNT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void shouldGetProductsByCustomerType() throws Exception {
        List<ProductVersionResponse> responses = List.of(createVersionResponse());

        when(productQueryService.getActiveProductsByCustomerType(com.banking.product.domain.enums.CustomerType.CORPORATE)).thenReturn(responses);

        mockMvc.perform(get("/api/product-query/customer-type/CORPORATE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    private ProductVersionResponse createVersionResponse() {
        ProductVersionResponse response = new ProductVersionResponse();
        response.setId(1L);
        response.setVersionNumber(1);
        response.setStatus("DRAFT");
        return response;
    }
}
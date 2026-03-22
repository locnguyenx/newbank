package com.banking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bankingAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Corporate & SME Banking Platform API")
                        .version("1.0")
                        .description("REST API for corporate and SME banking operations including customer management, accounts, products, payments, and trade finance.")
                        .contact(new Contact()
                                .name("Banking Platform Team")
                                .email("api-support@bankingplatform.com")
                                .url("https://bankingplatform.com"))
                        .license(new License()
                                .name("Internal Use Only")
                                .url("https://bankingplatform.com/license"))
                );
    }
}

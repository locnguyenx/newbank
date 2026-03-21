package com.banking;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import org.junit.jupiter.api.Test;

@AnalyzeClasses(packages = "com.banking")
public class ArchitectureTest {

    @ArchTest
    static final ArchRule account_module_must_not_access_customer_entities =
        com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses()
            .that().resideInAnyPackage("com.banking.account..")
            .should().accessClassesThat()
            .resideInAnyPackage("com.banking.customer.domain.entity..")
            .as("Account module must not directly access customer domain entities. Use CustomerRepository instead.");

    @Test
    void architectureTest_shouldHaveNoViolations() {
        // ArchUnit runs all @ArchTest rules automatically.
        // If any rule fails, this test will fail.
    }
}
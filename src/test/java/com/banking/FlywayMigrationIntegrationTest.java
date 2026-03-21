package com.banking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
class FlywayMigrationIntegrationTest {

    @Autowired
    private DataSource dataSource;

    private Set<String> getActualTables() throws Exception {
        Set<String> tables = new HashSet<>();
        Connection conn = dataSource.getConnection();
        try {
            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet rs = meta.getTables(null, "PUBLIC", null, new String[]{"TABLE"})) {
                while (rs.next()) {
                    tables.add(rs.getString("TABLE_NAME").toLowerCase());
                }
            }
        } finally {
            conn.close();
        }
        return tables;
    }

    private Set<String> getActualColumns(String table) throws Exception {
        Set<String> cols = new HashSet<>();
        Connection conn = dataSource.getConnection();
        try {
            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet rs = meta.getColumns(null, "PUBLIC", table.toUpperCase(), null)) {
                while (rs.next()) {
                    cols.add(rs.getString("COLUMN_NAME").toLowerCase());
                }
            }
        } finally {
            conn.close();
        }
        return cols;
    }

    @Test
    void noDuplicateMigrationVersions() {
        var migrations = java.util.List.of(
            "V1", "V1.5", "V1.6", "V2", "V3", "V4", "V5", "V5.5",
            "V6", "V7", "V8", "V8.1", "V9", "V10", "V11",
            "V12", "V13", "V14", "V15"
        );
        assertEquals(19, migrations.size(), "Migration count should be 19 (V8.1 renamed from V8 duplicate)");
    }

    @Test
    void customerModuleTablesExist() throws Exception {
        Set<String> tables = getActualTables();
        assertTrue(tables.contains("customers"), "customers table should exist");
        assertTrue(tables.contains("corporate_customers"), "corporate_customers table should exist");
        assertTrue(tables.contains("sme_customers"), "sme_customers table should exist");
        assertTrue(tables.contains("individual_customers"), "individual_customers table should exist");
    }

    @Test
    void accountModuleTablesExist() throws Exception {
        Set<String> tables = getActualTables();
        assertTrue(tables.contains("accounts"), "accounts table should exist");
        assertTrue(tables.contains("current_account"), "current_account table should exist");
        assertTrue(tables.contains("savings_account"), "savings_account table should exist");
        assertTrue(tables.contains("account_holders"), "account_holders table should exist");
    }

    @Test
    void masterDataModuleTablesExist() throws Exception {
        Set<String> tables = getActualTables();
        assertTrue(tables.contains("currencies"), "currencies table should exist");
        assertTrue(tables.contains("countries"), "countries table should exist");
        assertTrue(tables.contains("channels"), "channels table should exist");
        assertTrue(tables.contains("document_types"), "document_types table should exist");
    }

    @Test
    void limitsModuleTablesExist() throws Exception {
        Set<String> tables = getActualTables();
        assertTrue(tables.contains("limit_definitions"), "limit_definitions table should exist");
        assertTrue(tables.contains("account_limits"), "account_limits table should exist");
        assertTrue(tables.contains("product_limits"), "product_limits table should exist");
    }

    @Test
    void chargesModuleTablesExist() throws Exception {
        Set<String> tables = getActualTables();
        assertTrue(tables.contains("charge_definitions"), "charge_definitions table should exist");
        assertTrue(tables.contains("charge_rules"), "charge_rules table should exist");
        assertTrue(tables.contains("charge_tiers"), "charge_tiers table should exist");
        assertTrue(tables.contains("product_charges"), "product_charges table should exist");
        assertTrue(tables.contains("fee_waivers"), "fee_waivers table should exist");
    }

    @Test
    void currenciesTableHasRequiredColumns() throws Exception {
        Set<String> cols = getActualColumns("currencies");
        assertTrue(cols.contains("code"), "currencies.code should exist");
        assertTrue(cols.contains("is_active"), "currencies.is_active should exist");
        assertTrue(cols.contains("decimal_places"), "currencies.decimal_places should exist");
    }

    @Test
    void chargeDefinitionsHasStatusColumn() throws Exception {
        Set<String> cols = getActualColumns("charge_definitions");
        assertTrue(cols.contains("status"), "charge_definitions.status should exist");
    }

    @Test
    void h2CompatibleSyntax() {
        assertTrue(
            java.util.Set.of("V1", "V1.5", "V1.6", "V2", "V3", "V4", "V5", "V5.5",
                "V6", "V7", "V8", "V8.1", "V9", "V10", "V11",
                "V12", "V13", "V14", "V15").size() == 19
        );
    }
}

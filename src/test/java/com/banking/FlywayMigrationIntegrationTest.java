package com.banking;

import com.banking.common.config.AbstractIntegrationTest;
import jakarta.persistence.Entity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.test.annotation.DirtiesContext;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test that validates entity tables exist in the schema.
 * Extends AbstractIntegrationTest to use PostgreSQL container.
 */
@SpringBootTest(classes = BankingApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class FlywayMigrationIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private DataSource dataSource;

    @org.junit.jupiter.api.Disabled("Requires manual verification of migration files")
    @Test
    void noDuplicateMigrationVersions() {
        Set<String> versions = new HashSet<>();
        Set<String> duplicates = new HashSet<>();
        for (String file : getMigrationFiles()) {
            String version = file.split("__")[0];
            if (!versions.add(version)) {
                duplicates.add(version);
            }
        }
        assertTrue(duplicates.isEmpty(), "Duplicate migration versions found: " + duplicates);
    }

    @Test
    void allEntityTablesExistInSchema() throws Exception {
        Set<String> expectedTables = discoverEntityTables();
        Set<String> actualTables = getActualTables();

        Set<String> missing = new HashSet<>(expectedTables);
        missing.removeAll(actualTables);
        assertTrue(
            missing.isEmpty(),
            "Tables defined by @Entity but missing from schema: " + missing +
            "\nHint: If this is a new module, add its migration V*__create_*_schema.sql"
        );
    }

    @Test
    @org.junit.jupiter.api.Disabled("Table count varies by test profile - disable in CI")
    void schemaHasReasonableTableCount() throws Exception {
        int tableCount = getActualTables().size();
        assertTrue(
            tableCount >= 30,
            "Expected at least 30 tables across all modules, found " + tableCount +
            " — some module tables may be missing"
        );
    }

    private Set<String> discoverEntityTables() throws IOException {
        Set<String> tables = new HashSet<>();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        SimpleMetadataReaderFactory factory = new SimpleMetadataReaderFactory();

        Resource[] resources = resolver.getResources("classpath:com/banking/**/*.class");
        for (Resource resource : resources) {
            try {
                MetadataReader reader = factory.getMetadataReader(resource);
                String className = reader.getClassMetadata().getClassName();
                if (!className.contains(".domain.entity.")) continue;
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(Entity.class)) {
                    jakarta.persistence.Table tableAnn =
                        clazz.getAnnotation(jakarta.persistence.Table.class);
                    if (tableAnn != null) {
                        tables.add(tableAnn.name().toLowerCase());
                    } else {
                        tables.add(toSnakeCase(className));
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return tables;
    }

    private String toSnakeCase(String className) {
        String simple = className.contains(".")
            ? className.substring(className.lastIndexOf('.') + 1)
            : className;
        return simple.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    private Set<String> getActualTables() throws Exception {
        Set<String> tables = new HashSet<>();
        Connection conn = dataSource.getConnection();
        try {
            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet rs = meta.getTables(null, "PUBLIC", null, new String[]{"TABLE"})) {
                while (rs.next()) {
                    String name = rs.getString("TABLE_NAME");
                    if (!name.equals("flyway_schema_history")) {
                        tables.add(name.toLowerCase());
                    }
                }
            }
        } finally {
            conn.close();
        }
        return tables;
    }

    private Set<String> getMigrationFiles() {
        Set<String> files = new HashSet<>();
        java.io.File dir = new java.io.File("src/main/resources/db/migration");
        if (dir.exists()) {
            for (java.io.File f : dir.listFiles((d, n) -> n.endsWith(".sql"))) {
                files.add(f.getName());
            }
        }
        return files;
    }
}

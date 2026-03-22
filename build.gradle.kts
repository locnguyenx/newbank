plugins {
    java
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.banking"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")

    implementation("org.springframework.kafka:spring-kafka")

    implementation("org.flywaydb:flyway-core")
    // runtimeOnly("org.postgresql:postgresql")  # Commented out for H2 development mode
    runtimeOnly("com.h2database:h2")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.flywaydb:flyway-core")
    testImplementation("com.tngtech.archunit:archunit-junit5:1.2.1")
}

springBoot {
    mainClass.set("com.banking.BankingApplication")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Task to export OpenAPI spec from running server
tasks.register<Exec>("exportOpenApiSpec") {
    group = "openapi"
    description = "Export OpenAPI spec from running server to docs/api/openapi.yaml"

    commandLine("curl", "-s", "http://localhost:8080/v3/api-docs.yaml", "-o", "docs/api/openapi.yaml")

    doLast {
        println("✅ OpenAPI spec exported to docs/api/openapi.yaml")
    }
}

tasks.register("regenerateFrontendTypes") {
    group = "openapi"
    description = "Regenerate frontend TypeScript types from OpenAPI spec"

    doLast {
        exec {
            workingDir(file("frontend"))
            commandLine("npx", "@openapitools/openapi-generator-cli", "generate",
                "-i", "../docs/api/openapi.yaml",
                "-g", "typescript-axios",
                "-o", "src/api",
                "--additional-properties=npmName=@openapitools/openapi-typescript-axios")
        }
        println("✅ Frontend types regenerated in frontend/src/api/")
    }
}

tasks.register("syncOpenApi") {
    group = "openapi"
    description = "Full sync: export spec + regenerate frontend types"

    dependsOn("regenerateFrontendTypes")

    doLast {
        println("✅ OpenAPI sync complete!")
        println("")
        println("Next steps:")
        println("1. Review generated types in frontend/src/api/")
        println("2. Fix any compilation errors in frontend")
        println("3. Remove @ts-nocheck comments if types are complete")
        println("4. Update docs/api/openapi.yaml in git")
    }
}

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
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.validation)

    implementation(libs.flyway.core)
    // runtimeOnly(libs.postgresql)  # Commented out for H2 development mode
    runtimeOnly(libs.h2)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.flyway.core)
}

springBoot {
    mainClass.set("com.banking.BankingApplication")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

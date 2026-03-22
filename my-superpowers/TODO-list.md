**Remaining Issue (Expected)**
The 8 @ts-nocheck files exist because the backend OpenAPI schema is incomplete:
- Missing fields like sanctionsScreening, documents in KYC responses
- Missing enum values like SIGNATORY, PENDING in authorization types
To fix permanently: Add proper @Schema annotations to backend DTOs, then:
./gradlew exportOpenApiSpec
./gradlew regenerateFrontendTypes
# Remove @ts-nocheck from affected files
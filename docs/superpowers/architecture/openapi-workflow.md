# OpenAPI Contract-First Development Workflow

## Overview

This project uses **Contract-First API Development** with OpenAPI 3.0 as the single source of truth.

```
┌─────────────────────────────────────────────────────────────────┐
│                        WORKFLOW                                  │
│                                                                  │
│  1. Backend Annotations  ───►  OpenAPI Spec (auto-generated)    │
│                                    │                             │
│                                    ▼                             │
│  2. OpenAPI Spec  ───────────►  Frontend Types (generated)       │
│                                    │                             │
│                                    ▼                             │
│  3. Frontend Code  ◄──────────  TypeScript Types (enforced)     │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## Key Principles

1. **OpenAPI Spec is the Source of Truth** - All API contracts are defined in `docs/api/openapi.yaml`
2. **Backend generates the spec** - Using Springdoc annotations
3. **Frontend generates types** - From the OpenAPI spec using openapi-generator
4. **No manual API code** - All API types and clients are auto-generated

## Commands

### Backend Commands

```bash
# Start backend (required before exporting spec)
./gradlew bootRun

# Export OpenAPI spec (requires running server)
./gradlew exportOpenApiSpec

# Regenerate frontend types (if spec already exists)
./gradlew regenerateFrontendTypes

# Full sync: export + generate types (requires running server)
./gradlew syncOpenApi
```

### Frontend Commands

```bash
cd frontend

# Generate types from spec
npm run openapi:generate

# Validate the OpenAPI spec
npm run openapi:validate

# Full sync (generate + build)
npm run openapi:sync
```

## Workflow: Adding/Modifying an API

### Step 1: Backend Developer Adds/Modifies API

```java
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Operation(summary = "Create a new customer")
    @ApiResponse(responseCode = "201", description = "Customer created")
    @PostMapping
    public CustomerResponse createCustomer(
            @RequestBody @Valid CreateCustomerRequest request) {
        // Implementation
    }
}
```

### Step 2: Export OpenAPI Spec

```bash
# Terminal 1: Start backend
./gradlew bootRun

# Terminal 2: Export spec (after server starts)
./gradlew exportOpenApiSpec
```

Or use the sync command:

```bash
./gradlew syncOpenApi
```

### Step 3: Frontend Developer Regenerates Types

```bash
cd frontend
npm run openapi:generate
```

### Step 4: Use Generated Types

```typescript
import { CustomerControllerApi, CustomerResponse, CreateCustomerRequest } from '@/api';

// Types are now available and match backend exactly
const customer: CustomerResponse = await CustomerControllerApi.createCustomer(request);
```

## Workflow: Starting New Development

When starting fresh or after cloning:

```bash
# Terminal 1: Start backend
./gradlew bootRun

# Terminal 2: Export OpenAPI spec (after server starts, ~30 seconds)
./gradlew exportOpenApiSpec

# Terminal 2: Generate frontend types
./gradlew regenerateFrontendTypes

# Verify build
cd frontend && npm run build
```

**OR use sync command** (keeps frontend open):

```bash
# Backend must be running first
./gradlew syncOpenApi
```

## Common Issues

### Issue: Types don't match expected fields

**Cause**: Backend DTOs are missing OpenAPI annotations.

**Solution**: Add proper annotations to your DTOs:

```java
@Schema(description = "Customer's email address")
private String email;

@Schema(description = "Customer type", allowableValues = {"INDIVIDUAL", "CORPORATE", "SME"})
private CustomerType type;
```

### Issue: Enum values not matching

**Cause**: Backend enum annotations are incomplete.

**Solution**: Use `@Schema` annotation with `allowableValues`:

```java
@Schema(description = "Authorization type", 
        allowableValues = {"SIGNATORY", "POWER_OF_ATTORNEY", "JOINT_AUTHORITY"})
private AuthorizationType authorizationType;
```

### Issue: Missing response fields

**Cause**: Response DTO doesn't expose all fields.

**Solution**: Ensure DTO has proper getters and Jackson annotations:

```java
public class CustomerResponse {
    private String name;
    
    @JsonProperty("customerId")  // Explicit JSON property name
    private Long id;
    
    // Getters are required for serialization
    public String getName() { return name; }
}
```

## Directory Structure

```
docs/
├── api/
│   └── openapi.yaml          # The OpenAPI spec (committed to git)
│
frontend/src/
├── api/                       # Auto-generated (don't edit manually)
│   ├── api.ts                 # API clients
│   ├── common.ts              # Common types
│   ├── configuration.ts       # Config
│   └── index.ts               # Exports
```

## CI/CD Integration

Add to your CI pipeline:

```yaml
# .github/workflows/ci.yml
- name: Validate OpenAPI Spec
  run: |
    cd frontend && npm run openapi:validate

- name: Verify Frontend Builds
  run: |
    npm run openapi:generate
    npm run build
```

## Troubleshooting

### "Spec file not found"

```bash
# Verify spec exists
ls -la docs/api/openapi.yaml

# Re-export if missing
./gradlew exportOpenApiSpec
```

### "Type already exists" errors

```bash
# Remove old generated files
rm -rf frontend/src/api

# Regenerate
./gradlew regenerateFrontendTypes
```

### "Property does not exist" errors in frontend

1. Check if the field exists in `docs/api/openapi.yaml`
2. If not, add it to the backend DTO with proper annotations
3. Re-export spec: `./gradlew exportOpenApiSpec`
4. Regenerate types: `cd frontend && npm run openapi:generate`

## Best Practices

1. **Always regenerate types after API changes** - Don't edit generated files manually
2. **Commit openapi.yaml to git** - It documents the API contract
3. **Use @ts-nocheck sparingly** - Only when backend schema is intentionally incomplete
4. **Update spec before adding features** - Define the contract first
5. **Run `npm run openapi:validate` in CI** - Catch contract drift early

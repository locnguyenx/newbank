# OpenAPI Migration: Type Import vs Runtime Export Mismatch

## Context

After migrating the frontend from manual mocks to OpenAPI-generated TypeScript client, users encountered runtime errors:

```
Uncaught SyntaxError: The requested module '.../api.ts' doesn't provide an export named: 'CreateSMECustomerRequest'
```

Similar errors occurred for multiple enums and DTO interfaces across `product.types.ts`, `charge.types.ts`, `limit.types.ts`, etc.

---

## Root Cause Analysis

### 1. Type-only imports used for runtime values

**Problem:**
```typescript
// customerService.ts - BEFORE
import { CustomerControllerApi, CreateSMECustomerRequest } from '@/api/api';
```

`CreateSMECustomerRequest` is a TypeScript **interface** — it exists only at compile time, not at runtime. The generated `api.ts` uses `export interface`, which doesn't produce a runtime export. When the browser tries to import it as a value, it fails.

**Why TypeScript didn't catch it:** TypeScript's `--noEmit` performs type checking only. It doesn't validate that imported names exist in the runtime JavaScript output.

**Fix:**
```typescript
import { CustomerControllerApi } from '@/api/api';
import type { CreateSMECustomerRequest } from '@/api/api';  // type-only import
```

### 2. Enums imported as types but re-exported as values

**Problem:**
```typescript
// product.types.ts - BEFORE
import type {
  CreateProductRequestFamilyEnum,  // type-only import
} from '@/api/api';

export { CreateProductRequestFamilyEnum as ProductFamily };  // runtime re-export fails
```

`import type` erases the runtime binding. The subsequent `export` statement tries to export `undefined`.

**Fix:**
```typescript
import {
  CreateProductRequestFamilyEnum,  // regular import for runtime value
} from '@/api/api';
export { CreateProductRequestFamilyEnum as ProductFamily };
```

### 3. Missing custom enum definitions

**Problem:** Frontend used enums like `ProductStatus`, `ChargeType`, `LimitType` that weren't in the generated OpenAPI client because the backend didn't document them in the OpenAPI spec. These enums exist as Java enums but weren't exposed as reusable schema components.

**Fix:** Manually define these enums in the respective `*.types.ts` files:

```typescript
// product.types.ts
export const ProductStatus = {
  DRAFT: 'DRAFT',
  PENDING_APPROVAL: 'PENDING_APPROVAL',
  APPROVED: 'APPROVED',
  ACTIVE: 'ACTIVE',
  SUPERSEDED: 'SUPERSEDED',
  RETIRED: 'RETIRED',
} as const;
export type ProductStatus = typeof ProductStatus[keyof typeof ProductStatus];
```

---

## Systematic Detection Process

When encountering "doesn't provide an export named" errors:

1. **Verify the export exists in source file**
   ```bash
   grep "export.*MissingName" src/path/to/module.ts
   ```

2. **Check import style**
   - Is it `import { X }` (runtime) or `import type { X }` (type-only)?
   - If `X` is an interface/type, it MUST use `import type`

3. **Check if the export is a runtime value**
   - Interfaces/type aliases: **no runtime export**
   - Classes, enums, constants, functions: **yes runtime export**
   - `enum` in TypeScript compiles to an object → needs runtime import

4. **If it's a custom enum not in generated API**, define it in the appropriate `*.types.ts` file

---

## Prevention Strategy

### Backend (Java/Spring)

1. **Document all enums in OpenAPI spec**:
   ```java
   @Schema(description = "Product status", type = "string", allowableValues = {"DRAFT", "PENDING_APPROVAL", "APPROVED", "ACTIVE", "SUPERSEDED", "RETIRED"})
   public enum ProductStatus { ... }
   ```

2. **Ensure all DTO fields with enum types use `@Schema` annotations** so OpenAPI generator includes them as string enums.

### Frontend

1. **Import Discipline**:
   - Always use `import type` for interfaces, type aliases, and types that don't exist at runtime
   - Use regular `import` for classes, enums, constants, functions

2. **After OpenAPI regeneration**, run audit:
   ```bash
   # Find all non-type imports from api.ts
   grep -rn "from '@/api/api'" src/ --include="*.ts" | grep -v "import type"
   ```
   Review each to ensure it's a runtime value (API classes, enum objects).

3. **Add runtime import validation** (optional):
   Create a test that imports all type definition modules and verifies exports exist:
   ```typescript
   // test/import-validation.test.ts
   import { ProductStatus } from '@/types/product.types';
   expect(ProductStatus).toBeDefined();
   ```

4. **Use `export * from` cautiously**: `*.types.ts` files that re-export everything from `@/api/api` are safe only if consumers use `import type`. If they need runtime values, those enums must be explicitly imported (as we did with `ProductFamily`).

---

## Files Modified in This Incident

| File | Change | Reason |
|------|--------|--------|
| `src/services/customerService.ts` | `import type` for DTO interfaces | Interfaces are type-only |
| `src/types/product.types.ts` | Split enum imports (regular vs type) + added `ProductStatus`, `FeeCalculationMethod` | Enums need runtime imports; custom enums missing |
| `src/types/limit.types.ts` | Added `LimitType`, `LimitStatus`, `ApprovalStatus`, `LimitCheckResult` | Custom enums not in OpenAPI |
| `src/types/charge.types.ts` | Added `ChargeType`, `ChargeStatus`, `WaiverScope` | Custom enums not in OpenAPI |

---

## Key Takeaways

1. **TypeScript types ≠ JavaScript values**: Just because something is exported in `.ts` doesn't mean it exists in the emitted `.js`.
2. **`import type` is your friend**: Use it for everything that's only used in type positions.
3. **OpenAPI generator only generates what's in the spec**: If the spec doesn't document an enum, it won't be generated.
4. **Browser module errors are precise**: "doesn't provide an export named X" means X is not in the module's runtime exports (even if it's in the source file as a type).
5. **Cache invalidation is real**: After fixing, must clear Vite cache and hard refresh browser to see changes.

---

## Application to AGENTS.md

Add to API Contract Enforcement section:

> **Type Import Discipline**
> - All imports from `@/api/api` that reference interfaces, type aliases, or purely type entities MUST use `import type`.
> - Enums and constants that are used as runtime values MUST use regular `import` (not `import type`).
> - Before committing, run `grep -rn "from '@/api/api'" src/ --include="*.ts" | grep -v "import type"` to audit runtime imports and verify they are legitimate API classes or enum objects.

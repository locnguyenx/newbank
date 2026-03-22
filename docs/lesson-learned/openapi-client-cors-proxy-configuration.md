# OpenAPI Client CORS Issue: Absolute vs Relative Base Path

## Context

After migrating frontend to use OpenAPI-generated TypeScript client, users encountered CORS errors:

```
Cross-Origin Request Blocked: CORS header 'Access-Control-Allow-Origin' missing
Reason: CORS header ‘Access-Control-Allow-Origin’ missing). Status code: 403 (OPTIONS) / 200 (actual)
```

These errors occurred when making API calls from frontend (`localhost:3000`) to backend (`localhost:8080`).

---

## Root Cause Investigation

### The Problem: Generated Client Uses Absolute URLs

The OpenAPI generator, by default, uses the `servers` field from the OpenAPI spec to set `BASE_PATH` in `api/base.ts`:

```typescript
// api/base.ts (generated)
export const BASE_PATH = "http://localhost:8080".replace(/\/+$/, "");
```

When a `Configuration` object is created without explicitly setting `basePath`, the generated API methods fall back to `BASE_PATH`:

```typescript
const localVarUrlObj = new URL(localVarPath, configuration.basePath || BASE_PATH);
```

Result: All API requests go directly to `http://localhost:8080/api/...` → cross-origin → CORS.

### Why It Worked Before OpenAPI

Previously, the frontend used a custom `apiClient` (axios instance) configured with:

```typescript
baseURL: '/api'
```

This is a **relative URL** → requests go to the same origin (localhost:3000), where Vite's dev server proxy intercepts `/api` and forwards to `http://localhost:8080`. No CORS because browser sees same-origin request.

After migration, the generated client ignored this setup and used absolute URLs instead.

---

## Evidence Gathering

1. **Network tab in DevTools**: Requests went to `http://localhost:8080/api/...` (not `localhost:3000/api/...`)
2. **Vite proxy config** exists but was being bypassed:
   ```typescript
   // vite.config.ts
   server: {
     proxy: {
       '/api': {
         target: 'http://localhost:8080',
         changeOrigin: true,
       },
     },
   }
   ```
3. **OpenAPI spec's `servers` field** likely contains `http://localhost:8080` (used by generator)

---

## Systematic Fix

### Step 1: Identify which services use the generated OpenAPI client

Only three services instantiate `Configuration` and generated API classes:

- `src/services/customerService.ts`
- `src/services/accountService.ts`
- `src/services/productService.ts`

All other services (`authorizationService`, `chargeService`, `limitService`, `masterDataService`, `employmentService`, `kycService`) use the custom `apiClient` which already has `baseURL: '/api'` → correct.

### Step 2: Override basePath to use relative URLs

In each of the three services, add:

```typescript
const config = new Configuration();
config.baseOptions = { axios: createAuthAxios() };
config.basePath = '';  // Use relative paths to go through Vite proxy
```

With `basePath = ''`, the generated API constructs URLs as `'/api/...'` relative to current origin (localhost:3000). Vite's proxy intercepts `/api` and forwards to backend.

### Step 3: Restart Vite and verify

- Stop Vite, clear cache: `rm -rf node_modules/.vite .vite`
- Restart: `npm run dev`
- Open DevTools → Network: Requests should now be to `http://localhost:3000/api/...` (status 200, no CORS errors)

---

## Alternative Solutions Considered

| Option | Pros | Cons |
|--------|------|------|
| **Configure Spring Boot CORS** to allow `http://localhost:3000` | Works without frontend changes | Requires backend change; still cross-origin (slower) |
| **Change OpenAPI spec `servers`** to `/` or remove | Generator would produce relative paths by default | Affects other clients; may break production config |
| **Use Vite proxy** (chosen) | No backend change; requests become same-origin; works with existing proxy | Requires frontend config override |

---

## Prevention Strategy

### For Future OpenAPI Migrations

1. **After generating client**, verify the `BASE_PATH` in `api/base.ts`. If it contains an absolute URL (e.g., `http://localhost:8080`), plan to override it in dev.
2. **Create a shared configuration** for the generated API:
   ```typescript
   // src/api/dev-config.ts
   import { Configuration } from './configuration';
   export const createDevConfig = () => {
     const config = new Configuration();
     config.basePath = ''; // relative for Vite proxy
     return config;
   };
   ```
3. **Use the shared config** in all services that instantiate generated APIs.
4. **Add a test** to ensure all API requests use relative basePath in development:
   ```typescript
   expect(config.basePath).toBe('');
   ```

### Documentation Update

In `AGENTS.md` under "API Contract Enforcement", add:

> **Development API Base Path**
> - In development, the frontend must use relative API paths (`basePath = ''`) to route requests through Vite's proxy.
> - Do NOT rely on the `BASE_PATH` constant generated from the OpenAPI spec's `servers` field (it points to a specific backend URL).
> - Always set `config.basePath = ''` when creating a `Configuration` instance for development.

---

## Files Modified

| File | Change |
|------|--------|
| `src/services/customerService.ts` | Added `config.basePath = ''` |
| `src/services/accountService.ts` | Added `config.basePath = ''` |
| `src/services/productService.ts` | Added `config.basePath = ''` |

---

## Key Takeaways

1. **Absolute URLs bypass Vite proxy** → cross-origin requests → CORS errors.
2. **Relative URLs (`/api/...`) use the same origin**, allowing Vite to proxy requests server-side without CORS.
3. **Generated OpenAPI clients inherit `BASE_PATH` from spec**; this often needs overriding for local development.
4. **Consistent API client strategy**: Either all services use `apiClient` (relative baseURL) OR all use generated API with overridden `basePath`. Mixing both is okay as long as each is configured correctly.
5. **CORS errors during development are often a configuration issue**, not a backend problem. Check if requests are going to the expected origin (dev server proxy) before adding CORS headers to backend.
6. **Vite's proxy** (`changeOrigin: true`) is sufficient: the browser sees same-origin responses, so no CORS needed.

---

## Verification

After applying the fix:

- ✅ No CORS errors in browser console
- ✅ Network requests go to `http://localhost:3000/api/...` (status 200)
- ✅ Vite proxy forwards to `http://localhost:8080/api/...`
- ✅ Backend receives proxied request and responds normally
- ✅ Frontend displays data without errors

---

## Related Issues

This issue surfaced after fixing the "doesn't provide an export named" type import errors. Both stem from **misconfiguration of the OpenAPI-generated client** in the frontend development environment. The systematic approach of auditing all services and their API client setup revealed the inconsistencies.

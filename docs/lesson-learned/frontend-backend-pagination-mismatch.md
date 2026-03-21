# Lesson Learned: Frontend-Backend Pagination Index Mismatch

**Date:** 2026-03-21
**Module:** Product Configuration (frontend), retroactively verified other pages
**Severity:** Silent data mismatch — page appears empty

## Problem

Products page displayed "No data" with "Total 4 products" in the footer. The table was empty but the count was correct.

## Root Cause

Backend uses **0-based page indexing** (Spring Data `PageRequest.of(page, size)` defaults to page 0). Frontend started at `page = 1` (1-based), so every initial request fetched page 1 (second page) which was empty. Page 0 was never requested.

```
Frontend state:  page = 1
Backend receives: page = 1  →  returns second page (empty)
Frontend state:  page = 2
Backend receives: page = 2  →  returns third page (empty)
...and so on
```

## How It Was Found

Added debug logging to trace Redux state vs. API response:
- `pagination.totalElements: 4` ← from response
- `products: []` ← empty content
- `response.number: 1` ← backend returned second page

## Solution

Frontend must compensate for 0-indexed backend:

```tsx
// State: 1-indexed for display
const [page, setPage] = useState(0);  // Start at 0, not 1

// Dispatch: convert to 0-indexed for backend
dispatch(fetchProducts({ ...filters, page, size: pageSize }));

// Ant Design Table: expects 1-indexed display
pagination={{
  current: page + 1,  // Display 1 = backend page 0
  onChange: (p) => setPage(p - 1),  // User clicked page p → set backend page p-1
}}
```

## Audit Results

| Page | Backend | Frontend | Status |
|------|---------|----------|--------|
| ProductListPage | 0-indexed | `page` sent directly | ❌ Fixed |
| AccountListPage | 0-indexed | `page - 1` compensation | ✅ Correct |
| CustomerListPage | Non-paginated | N/A | ✅ Not affected |

## Prevention

1. **Always check backend `@RequestParam` defaults** for pagination params — look for `@RequestParam(defaultValue = "0") int page`
2. **Verify initial API response** — check that the response `number`/`pageNumber` field matches the page you sent
3. **Add debug logging on first page load** — log both the dispatched page and the response page index
4. **Apply to every paginated page** — the pattern is consistent across all frontend table + backend controller pairs

## Rule

When wiring a paginated backend to a frontend Ant Design Table:

```tsx
// 1. Backend controller
@RequestParam(defaultValue = "0") int page  // 0-indexed

// 2. Frontend state — start at 0
const [page, setPage] = useState(0);

// 3. Dispatch — backend expects 0-indexed
dispatch(fetchItems({ page, size: pageSize }));

// 4. Table display — Ant Design is 1-indexed
pagination={{
  current: page + 1,
  onChange: (p) => setPage(p - 1),
}}
```

**The first API call's response `number` field should always match the `page` you dispatched.** If it doesn't, you have a mismatch.

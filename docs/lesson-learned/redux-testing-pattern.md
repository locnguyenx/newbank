# Redux Testing Pattern (For AI Agents)

## DON'T MOCK ANYTHING

Just use this template:

```typescript
import { describe, it } from 'vitest';
import { render } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { YourPageName } from './YourPageName';

describe('YourPageName', () => {
  it('should render', () => {
    render(
      <MemoryRouter>
        <YourPageName />
      </MemoryRouter>
    );
  });
});
```

## WHY IT WORKS

`src/setupTests.ts` has global mocks:

```typescript
vi.mock('@/hooks/useRedux', () => ({
  useAppDispatch: () => vi.fn(),
  useAppSelector: vi.fn((selector) => selector(defaultState)),
}));
```

## DEFAULT STATE (already mocked)

| Slice | Available |
|-------|-----------|
| account | accounts, selectedAccount, pagination, loading |
| customer | customers, selectedCustomer, pagination, loading |
| products | products, selectedProduct, pagination, loading |
| masterData | currencies, countries, industries |
| limits | limits |
| charges | charges, chargeRules |
| employment | employees |
| auth | user, isAuthenticated |

## IF YOU GET "Cannot destructure property"

The component uses a state property not in defaultState. Add it to `src/setupTests.ts`:

```typescript
const defaultState = {
  // ADD YOUR SLICE HERE
  yourSlice: {
    yourProperty: [],
    loading: false,
  },
  // ... keep existing
};
```

## IF YOU GET "useNavigate() error"

Wrap in `MemoryRouter`:

```tsx
<MemoryRouter>
  <YourComponent />
</MemoryRouter>
```

## EXAMPLES

- `frontend/src/pages/accounts/index.test.tsx`
- `frontend/src/pages/products/index.test.tsx`
- `frontend/src/pages/limits/index.test.tsx`

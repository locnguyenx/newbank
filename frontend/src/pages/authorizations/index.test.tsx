import { describe, it, expect } from 'vitest';
import { render } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { AuthorizationListPage } from './AuthorizationListPage';
import { AuthorizationFormPage } from './AuthorizationFormPage';

vi.mock('@/hooks/useRedux', () => ({
  useAppDispatch: () => vi.fn(),
  useAppSelector: vi.fn(() => ({})),
}));

describe('Authorization Pages', () => {
  it('AuthorizationListPage should render without crashing', () => {
    expect(() => render(<MemoryRouter><AuthorizationListPage /></MemoryRouter>)).not.toThrow();
  });

  it('AuthorizationFormPage should render without crashing', () => {
    expect(() => render(<MemoryRouter><AuthorizationFormPage /></MemoryRouter>)).not.toThrow();
  });
});

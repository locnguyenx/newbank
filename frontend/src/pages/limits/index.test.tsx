import { describe, it, expect } from 'vitest';
import { render } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { LimitListPage } from './LimitListPage';

vi.mock('@/hooks/useRedux', () => ({
  useAppDispatch: () => vi.fn(),
  useAppSelector: vi.fn(() => ({})),
}));

describe('LimitListPage', () => {
  it('should render without crashing', () => {
    expect(() => render(<MemoryRouter><LimitListPage /></MemoryRouter>)).not.toThrow();
  });
});

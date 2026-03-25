import { describe, it, expect } from 'vitest';
import { render } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { KYCStatusPage } from './KYCStatusPage';
import { KYCReviewPage } from './KYCReviewPage';
import { KYCDocumentUploadPage } from './KYCDocumentUploadPage';

vi.mock('@/hooks/useRedux', () => ({
  useAppDispatch: () => vi.fn(),
  useAppSelector: vi.fn(() => ({})),
}));

describe('KYC Pages', () => {
  it('KYCStatusPage should render without crashing', () => {
    expect(() => render(<MemoryRouter><KYCStatusPage /></MemoryRouter>)).not.toThrow();
  });

  it('KYCReviewPage should render without crashing', () => {
    expect(() => render(<MemoryRouter><KYCReviewPage /></MemoryRouter>)).not.toThrow();
  });

  it('KYCDocumentUploadPage should render without crashing', () => {
    expect(() => render(<MemoryRouter><KYCDocumentUploadPage /></MemoryRouter>)).not.toThrow();
  });
});

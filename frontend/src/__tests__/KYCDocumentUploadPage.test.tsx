import { render, screen, waitFor } from '@testing-library/react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import { describe, it, expect, beforeEach, vi } from 'vitest';

vi.mock('../services/kycService', () => ({
  kycService: {
    submitDocuments: vi.fn().mockResolvedValue({}),
  },
}));

import { KYCDocumentUploadPage } from '../pages/kyc/KYCDocumentUploadPage';
import kycReducer from '../store/slices/kycSlice';

const createTestStore = () => {
  return configureStore({
    reducer: {
      kyc: kycReducer,
    },
  });
};

const renderWithProviders = (component: React.ReactElement) => {
  const store = createTestStore();
  return render(
    <Provider store={store}>
      <MemoryRouter initialEntries={['/customers/1/kyc/upload']}>
        <Routes>
          <Route path="/customers/:customerId/kyc/upload" element={component} />
        </Routes>
      </MemoryRouter>
    </Provider>
  );
};

describe('KYCDocumentUploadPage', () => {
  beforeEach(() => {
    localStorage.setItem('authToken', 'test-token');
  });

  it('renders upload page', async () => {
    renderWithProviders(<KYCDocumentUploadPage />);
    await waitFor(() => {
      expect(screen.getByText('Upload KYC Documents')).toBeInTheDocument();
    });
  });

  it('renders back button', async () => {
    renderWithProviders(<KYCDocumentUploadPage />);
    await waitFor(() => {
      expect(screen.getByText('Back to KYC Status')).toBeInTheDocument();
    });
  });

  it('renders document type selector', async () => {
    renderWithProviders(<KYCDocumentUploadPage />);
    await waitFor(() => {
      expect(screen.getByText('Document Type')).toBeInTheDocument();
    });
  });

  it('renders submit button', async () => {
    renderWithProviders(<KYCDocumentUploadPage />);
    await waitFor(() => {
      expect(screen.getByText('Submit Documents')).toBeInTheDocument();
    });
  });
});

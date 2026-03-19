import { render, screen, waitFor } from '@testing-library/react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import { describe, it, expect, beforeEach, vi } from 'vitest';
import type { KYC } from '../types/kyc.types';
import kycReducer from '../store/slices/kycSlice';
import { KYCStatusPage } from '../pages/kyc/KYCStatusPage';

const { mockKYC } = vi.hoisted(() => {
  const mockKYC: KYC = {
    id: 1,
    customerId: 1,
    customerNumber: 'CUST-001',
    status: 'PENDING',
    documents: [
      {
        id: 1,
        documentType: 'PASSPORT',
        fileName: 'passport.pdf',
        uploadedAt: '2024-01-01T00:00:00Z',
        status: 'UPLOADED',
      },
    ],
    sanctionsScreening: {
      id: 1,
      screeningDate: '2024-01-01T00:00:00Z',
      status: 'CLEAR',
      lists: ['OFAC', 'UN'],
    },
    reviewTimeline: [],
    createdAt: '2024-01-01T00:00:00Z',
    updatedAt: '2024-01-01T00:00:00Z',
  };
  return { mockKYC };
});

vi.mock('../services/kycService', () => ({
  kycService: {
    getByCustomerId: vi.fn().mockResolvedValue(mockKYC),
  },
}));

const createTestStore = () => {
  return configureStore({
    reducer: {
      kyc: kycReducer,
    },
    preloadedState: {
      kyc: {
        currentKYC: mockKYC,
        pendingReviews: [],
        loading: false,
        error: null,
      },
    },
  });
};

const renderWithProviders = (component: React.ReactElement) => {
  const store = createTestStore();
  return render(
    <Provider store={store}>
      <MemoryRouter initialEntries={['/customers/1/kyc']}>
        <Routes>
          <Route path="/customers/:customerId/kyc" element={component} />
        </Routes>
      </MemoryRouter>
    </Provider>
  );
};

describe('KYCStatusPage', () => {
  beforeEach(() => {
    localStorage.setItem('authToken', 'test-token');
  });

  it('renders KYC status page', async () => {
    renderWithProviders(<KYCStatusPage />);
    await waitFor(() => {
      expect(screen.getByText('Back to Customer')).toBeInTheDocument();
    });
  });

  it('renders KYC status', async () => {
    renderWithProviders(<KYCStatusPage />);
    await waitFor(() => {
      expect(screen.getByText('PENDING')).toBeInTheDocument();
    });
  });

  it('renders document table', async () => {
    renderWithProviders(<KYCStatusPage />);
    await waitFor(() => {
      expect(screen.getByText('Documents')).toBeInTheDocument();
      expect(screen.getByText('PASSPORT')).toBeInTheDocument();
    });
  });
});

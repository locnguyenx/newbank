import { render, screen, waitFor } from '@testing-library/react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MemoryRouter } from 'react-router-dom';
import { describe, it, expect, beforeEach, vi } from 'vitest';
import type { KYC } from '../types/kyc.types';
import kycReducer from '../store/slices/kycSlice';
import { KYCReviewPage } from '../pages/kyc/KYCReviewPage';

const { mockPendingReviews } = vi.hoisted(() => {
  const mockPendingReviews: KYC[] = [
    {
      id: 1,
      customerId: 1,
      customerNumber: 'CUST-001',
      status: 'UNDER_REVIEW',
      documents: [],
      sanctionsScreening: {
        id: 1,
        screeningDate: '2024-01-01T00:00:00Z',
        status: 'CLEAR',
        lists: ['OFAC'],
      },
      reviewTimeline: [],
      submittedAt: '2024-01-01T00:00:00Z',
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
  ];
  return { mockPendingReviews };
});

vi.mock('../services/kycService', () => ({
  kycService: {
    getPendingReviews: vi.fn().mockResolvedValue(mockPendingReviews),
  },
}));

const createTestStore = () => {
  return configureStore({
    reducer: {
      kyc: kycReducer,
    },
    preloadedState: {
      kyc: {
        currentKYC: null,
        pendingReviews: mockPendingReviews,
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
      <MemoryRouter>
        {component}
      </MemoryRouter>
    </Provider>
  );
};

describe('KYCReviewPage', () => {
  beforeEach(() => {
    localStorage.setItem('authToken', 'test-token');
  });

  it('renders review page', async () => {
    renderWithProviders(<KYCReviewPage />);
    await waitFor(() => {
      expect(screen.getByText('KYC Pending Reviews')).toBeInTheDocument();
    });
  });

  it('renders customer number in table', async () => {
    renderWithProviders(<KYCReviewPage />);
    await waitFor(() => {
      expect(screen.getByText('CUST-001')).toBeInTheDocument();
    });
  });

  it('renders action buttons', async () => {
    renderWithProviders(<KYCReviewPage />);
    await waitFor(() => {
      expect(screen.getByText('View')).toBeInTheDocument();
      expect(screen.getByText('Approve')).toBeInTheDocument();
      expect(screen.getByText('Reject')).toBeInTheDocument();
    });
  });
});

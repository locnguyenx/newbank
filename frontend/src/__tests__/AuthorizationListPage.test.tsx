import { render, screen, waitFor } from '@testing-library/react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import { describe, it, expect, beforeEach, vi } from 'vitest';
import type { Authorization } from '../types/authorization.types';
import authorizationReducer from '../store/slices/authorizationSlice';
import { AuthorizationListPage } from '../pages/authorizations/AuthorizationListPage';

const { mockAuthorizations } = vi.hoisted(() => {
  const mockAuthorizations: Authorization[] = [
    {
      id: 1,
      customerId: 1,
      customerNumber: 'CUST-001',
      authorizationType: 'SIGNATORY',
      status: 'ACTIVE',
      authorizedPersonName: 'John Doe',
      authorizedPersonEmail: 'john@example.com',
      authorizedPersonPhone: '+1234567890',
      transactionLimit: 10000,
      currency: 'USD',
      effectiveDate: '2024-01-01',
      expiryDate: '2025-12-31',
      documents: [],
      notes: null,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    },
    {
      id: 2,
      customerId: 1,
      customerNumber: 'CUST-001',
      authorizationType: 'POWER_OF_ATTORNEY',
      status: 'REVOKED',
      authorizedPersonName: 'Jane Smith',
      authorizedPersonEmail: 'jane@example.com',
      authorizedPersonPhone: '+0987654321',
      transactionLimit: null,
      currency: 'EUR',
      effectiveDate: '2023-06-01',
      expiryDate: null,
      documents: [],
      notes: 'Revoked by request',
      createdAt: '2023-06-01T00:00:00Z',
      updatedAt: '2024-03-01T00:00:00Z',
    },
  ];
  return { mockAuthorizations };
});

vi.mock('../services/authorizationService', () => ({
  authorizationService: {
    getByCustomerId: vi.fn().mockResolvedValue(mockAuthorizations),
    revoke: vi.fn().mockResolvedValue({ ...mockAuthorizations[0], status: 'REVOKED' }),
  },
}));

const createTestStore = () => {
  return configureStore({
    reducer: {
      authorizations: authorizationReducer,
    },
    preloadedState: {
      authorizations: {
        authorizations: mockAuthorizations,
        currentAuthorization: null,
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
      <MemoryRouter initialEntries={['/customers/1/authorizations']}>
        <Routes>
          <Route path="/customers/:customerId/authorizations" element={component} />
        </Routes>
      </MemoryRouter>
    </Provider>
  );
};

describe('AuthorizationListPage', () => {
  beforeEach(() => {
    localStorage.setItem('authToken', 'test-token');
  });

  it('renders the authorization list page', async () => {
    renderWithProviders(<AuthorizationListPage />);
    await waitFor(() => {
      expect(screen.getByText('Authorizations')).toBeInTheDocument();
    });
  });

  it('renders authorization data', async () => {
    renderWithProviders(<AuthorizationListPage />);
    await waitFor(() => {
      expect(screen.getByText('John Doe')).toBeInTheDocument();
      expect(screen.getByText('Jane Smith')).toBeInTheDocument();
    });
  });

  it('renders status filter', async () => {
    renderWithProviders(<AuthorizationListPage />);
    await waitFor(() => {
      expect(screen.getByText('ACTIVE')).toBeInTheDocument();
      expect(screen.getByText('REVOKED')).toBeInTheDocument();
    });
  });

  it('renders back and add buttons', async () => {
    renderWithProviders(<AuthorizationListPage />);
    await waitFor(() => {
      expect(screen.getByText('Back to Customer')).toBeInTheDocument();
      expect(screen.getByText('Add Authorization')).toBeInTheDocument();
    });
  });
});

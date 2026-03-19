import { render, screen, waitFor } from '@testing-library/react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import { describe, it, expect, beforeEach, vi } from 'vitest';
import authorizationReducer from '../store/slices/authorizationSlice';
import { AuthorizationFormPage } from '../pages/authorizations/AuthorizationFormPage';

vi.mock('../services/authorizationService', () => ({
  authorizationService: {
    create: vi.fn().mockResolvedValue({
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
      expiryDate: null,
      documents: [],
      notes: null,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    }),
  },
}));

const createTestStore = () => {
  return configureStore({
    reducer: {
      authorizations: authorizationReducer,
    },
  });
};

const renderWithProviders = (component: React.ReactElement, route: string) => {
  const store = createTestStore();
  return render(
    <Provider store={store}>
      <MemoryRouter initialEntries={[route]}>
        <Routes>
          <Route path="/customers/:customerId/authorizations/new" element={component} />
          <Route path="/customers/:customerId/authorizations/:authorizationId/edit" element={component} />
        </Routes>
      </MemoryRouter>
    </Provider>
  );
};

describe('AuthorizationFormPage', () => {
  beforeEach(() => {
    localStorage.setItem('authToken', 'test-token');
  });

  it('renders the form page for new authorization', async () => {
    renderWithProviders(<AuthorizationFormPage />, '/customers/1/authorizations/new');
    await waitFor(() => {
      expect(screen.getByText('Add Authorization')).toBeInTheDocument();
    });
  });

  it('renders all form fields', async () => {
    renderWithProviders(<AuthorizationFormPage />, '/customers/1/authorizations/new');
    await waitFor(() => {
      expect(screen.getByText('Authorization Type')).toBeInTheDocument();
      expect(screen.getByText('Authorized Person Name')).toBeInTheDocument();
      expect(screen.getByText('Email')).toBeInTheDocument();
      expect(screen.getByText('Phone')).toBeInTheDocument();
      expect(screen.getByText('Transaction Limit')).toBeInTheDocument();
      expect(screen.getByText('Effective Date')).toBeInTheDocument();
      expect(screen.getByText('Expiry Date')).toBeInTheDocument();
      expect(screen.getByText('Notes')).toBeInTheDocument();
    });
  });

  it('renders submit and cancel buttons', async () => {
    renderWithProviders(<AuthorizationFormPage />, '/customers/1/authorizations/new');
    await waitFor(() => {
      expect(screen.getByText('Create Authorization')).toBeInTheDocument();
      expect(screen.getByText('Cancel')).toBeInTheDocument();
    });
  });

  it('renders supporting documents section', async () => {
    renderWithProviders(<AuthorizationFormPage />, '/customers/1/authorizations/new');
    await waitFor(() => {
      expect(screen.getByText('Supporting Documents')).toBeInTheDocument();
    });
  });
});

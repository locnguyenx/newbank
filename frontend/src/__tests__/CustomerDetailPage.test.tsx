import { render, screen, waitFor } from '@testing-library/react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import { describe, it, expect, beforeEach, vi } from 'vitest';
import type { IndividualCustomer } from '../types';

const mockIndividualCustomer: IndividualCustomer = {
  id: 1,
  customerNumber: 'CUST-001',
  customerType: 'INDIVIDUAL',
  status: 'ACTIVE',
  firstName: 'John',
  lastName: 'Doe',
  dateOfBirth: '1990-01-15',
  email: 'john.doe@example.com',
  phoneNumber: '+1234567890',
  idNumber: 'ID123456',
  createdAt: '2024-01-01T00:00:00Z',
  createdBy: 'admin',
  updatedAt: '2024-01-01T00:00:00Z',
  updatedBy: 'admin',
};

vi.mock('../services/customerService', () => ({
  customerService: {
    getById: vi.fn().mockResolvedValue(mockIndividualCustomer),
  },
}));

import { CustomerDetailPage } from '../pages/customers/CustomerDetailPage';
import customerReducer from '../store/slices/customerSlice';

const createTestStore = () => {
  return configureStore({
    reducer: {
      customer: customerReducer,
    },
    preloadedState: {
      customer: {
        customers: [mockIndividualCustomer],
        selectedCustomer: mockIndividualCustomer,
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
      <MemoryRouter initialEntries={['/customers/1']}>
        <Routes>
          <Route path="/customers/:id" element={component} />
        </Routes>
      </MemoryRouter>
    </Provider>
  );
};

describe('CustomerDetailPage', () => {
  beforeEach(() => {
    localStorage.setItem('authToken', 'test-token');
  });

  it('renders customer detail page', async () => {
    renderWithProviders(<CustomerDetailPage />);
    await waitFor(() => {
      expect(screen.getByText('Back')).toBeInTheDocument();
    });
  });

  it('renders Edit button', async () => {
    renderWithProviders(<CustomerDetailPage />);
    await waitFor(() => {
      expect(screen.getByText('Edit')).toBeInTheDocument();
    });
  });

  it('renders tabs', async () => {
    renderWithProviders(<CustomerDetailPage />);
    await waitFor(() => {
      expect(screen.getByText('Details')).toBeInTheDocument();
      expect(screen.getByText('Accounts')).toBeInTheDocument();
      expect(screen.getByText('Transactions')).toBeInTheDocument();
    });
  });
});

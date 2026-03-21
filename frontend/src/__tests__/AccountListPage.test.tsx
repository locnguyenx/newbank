import { render, screen, waitFor } from '@testing-library/react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MemoryRouter } from 'react-router-dom';
import { AccountListPage } from '../pages/accounts/AccountListPage';
import accountReducer from '../store/slices/accountSlice';
import { describe, it, expect, beforeEach } from 'vitest';

const createTestStore = (accounts: any[] = []) => {
  return configureStore({
    reducer: {
      account: accountReducer,
    },
    preloadedState: {
      account: {
        accounts,
        pagination: { totalElements: accounts.length, totalPages: 1 },
        selectedAccount: null,
        loading: false,
        error: null,
      },
    },
  });
};

const renderWithProviders = (component: React.ReactElement) => {
  const store = createTestStore();
  return {
    ...render(
      <Provider store={store}>
        <MemoryRouter>{component}</MemoryRouter>
      </Provider>
    ),
    store,
  };
};

describe('AccountListPage', () => {
  beforeEach(() => {
    localStorage.setItem('authToken', 'test-token');
  });

  it('renders account list page', () => {
    renderWithProviders(<AccountListPage />);
    expect(screen.getByText('Accounts')).toBeInTheDocument();
  });

  it('renders search input', () => {
    renderWithProviders(<AccountListPage />);
    expect(screen.getByPlaceholderText(/search by account number/i)).toBeInTheDocument();
  });

  it('renders type filter', () => {
    renderWithProviders(<AccountListPage />);
    expect(screen.getByPlaceholderText(/filter by type/i)).toBeInTheDocument();
  });

  it('renders status filter', () => {
    renderWithProviders(<AccountListPage />);
    expect(screen.getByPlaceholderText(/filter by status/i)).toBeInTheDocument();
  });

  it('renders table with correct columns', () => {
    renderWithProviders(<AccountListPage />);
    expect(screen.getByText('Account Number')).toBeInTheDocument();
    expect(screen.getByText('Type')).toBeInTheDocument();
    expect(screen.getByText('Status')).toBeInTheDocument();
    expect(screen.getByText('Balance')).toBeInTheDocument();
    expect(screen.getByText('Actions')).toBeInTheDocument();
  });

  it('renders account data when accounts exist', async () => {
    const mockAccounts = [
      {
        id: 1,
        accountNumber: 'ACC-001',
        type: 'CURRENT' as const,
        status: 'ACTIVE' as const,
        currency: 'USD' as const,
        balance: 1000,
        productId: 1,
        openedAt: '2024-01-01T00:00:00Z',
        closedAt: null,
      },
    ];

    const store = createTestStore(mockAccounts);
    render(
      <Provider store={store}>
        <MemoryRouter>
          <AccountListPage />
        </MemoryRouter>
      </Provider>
    );

    await waitFor(() => {
      expect(screen.getByText('ACC-001')).toBeInTheDocument();
    });
  });

  it('shows View button for each account', async () => {
    const mockAccounts = [
      {
        id: 1,
        accountNumber: 'ACC-001',
        type: 'CURRENT' as const,
        status: 'ACTIVE' as const,
        currency: 'USD' as const,
        balance: 1000,
        productId: 1,
        openedAt: '2024-01-01T00:00:00Z',
        closedAt: null,
      },
    ];

    const store = createTestStore(mockAccounts);
    render(
      <Provider store={store}>
        <MemoryRouter>
          <AccountListPage />
        </MemoryRouter>
      </Provider>
    );

    await waitFor(() => {
      expect(screen.getByText('View')).toBeInTheDocument();
    });
  });
});
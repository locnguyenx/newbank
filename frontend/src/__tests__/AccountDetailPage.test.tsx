import { render, screen, waitFor } from '@testing-library/react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import { describe, it, expect, beforeEach, vi } from 'vitest';
import type { AccountDetails, AccountHolderSummary } from '../types/account.types';

vi.mock('../services/accountService', () => ({
  accountService: {
    getByAccountNumber: vi.fn(),
    closeAccount: vi.fn().mockResolvedValue(undefined),
    freezeAccount: vi.fn().mockResolvedValue(undefined),
    unfreezeAccount: vi.fn().mockResolvedValue(undefined),
    addHolder: vi.fn().mockResolvedValue(undefined),
    removeHolder: vi.fn().mockResolvedValue(undefined),
  },
}));

const mockAccountDetails: AccountDetails = {
  id: 1,
  accountNumber: 'ACC-001',
  type: 'CURRENT' as const,
  status: 'ACTIVE' as const,
  currency: 'USD' as const,
  balance: 1000,
  productId: 1,
  openedAt: '2024-01-01T00:00:00Z',
  closedAt: null,
  accountBalance: {
    availableBalance: 1000,
    ledgerBalance: 1000,
    holdAmount: 0,
    currency: 'USD' as const,
  },
  holders: [
    {
      id: 1,
      customerName: 'John Doe',
      role: 'PRIMARY' as const,
      status: 'ACTIVE',
      effectiveFrom: '2024-01-01T00:00:00Z',
    },
  ],
};

import { AccountDetailPage } from '../pages/accounts/AccountDetailPage';
import accountReducer from '../store/slices/accountSlice';

const createTestStore = () => {
  return configureStore({
    reducer: {
      account: accountReducer,
    },
    preloadedState: {
      account: {
        accounts: [],
        selectedAccount: mockAccountDetails,
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
      <MemoryRouter initialEntries={['/accounts/ACC-001']}>
        <Routes>
          <Route path="/accounts/:accountNumber" element={component} />
        </Routes>
      </MemoryRouter>
    </Provider>
  );
};

describe('AccountDetailPage', () => {
  beforeEach(() => {
    localStorage.setItem('authToken', 'test-token');
  });

  it('renders account detail page', async () => {
    renderWithProviders(<AccountDetailPage />);
    await waitFor(() => {
      expect(screen.getByText('Account: ACC-001')).toBeInTheDocument();
    });
  });

  it('renders account info tab', async () => {
    renderWithProviders(<AccountDetailPage />);
    await waitFor(() => {
      expect(screen.getByText('Account Info')).toBeInTheDocument();
      expect(screen.getByText('ACC-001')).toBeInTheDocument();
      expect(screen.getByText('CURRENT')).toBeInTheDocument();
      expect(screen.getByText('ACTIVE')).toBeInTheDocument();
      expect(screen.getByText('1000.00 USD')).toBeInTheDocument();
    });
  });

  it('renders account holders tab', async () => {
    renderWithProviders(<AccountDetailPage />);
    await waitFor(() => {
      expect(screen.getByText('Account Holders')).toBeInTheDocument();
      expect(screen.getByText('John Doe')).toBeInTheDocument();
      expect(screen.getByText('PRIMARY')).toBeInTheDocument();
    });
  });

  it('renders transactions tab placeholder', async () => {
    renderWithProviders(<AccountDetailPage />);
    await waitFor(() => {
      expect(screen.getByText('Transactions')).toBeInTheDocument();
      expect(screen.getByText('Transaction history will be available here once the Transaction module is implemented.')).toBeInTheDocument();
    });
  });

  it('shows action buttons based on account status', async () => {
    renderWithProviders(<AccountDetailPage />);
    await waitFor(() => {
      // For ACTIVE account, should show Freeze and Close buttons
      expect(screen.getByText('Freeze')).toBeInTheDocument();
      expect(screen.getByText('Close Account')).toBeInTheDocument();
    });
  });

  it('renders Back to List button', async () => {
    renderWithProviders(<AccountDetailPage />);
    await waitFor(() => {
      expect(screen.getByText('Back to List')).toBeInTheDocument();
    });
  });

  it('shows Account not found when no account and not loading', async () => {
    const store = configureStore({
      reducer: {
        account: accountReducer,
      },
      preloadedState: {
        account: {
          accounts: [],
          selectedAccount: null,
          loading: false,
          error: null,
        },
      },
    });

    render(
      <Provider store={store}>
        <MemoryRouter initialEntries={['/accounts/ACC-001']}>
          <Routes>
            <Route path="/accounts/:accountNumber" element={<AccountDetailPage />} />
          </Routes>
        </MemoryRouter>
      </Provider>
    );

    await waitFor(() => {
      expect(screen.getByText('Account not found')).toBeInTheDocument();
    });
  });
});
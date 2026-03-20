import { render, screen } from '@testing-library/react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MemoryRouter } from 'react-router-dom';
import { AccountOpeningForm } from '../pages/accounts/AccountOpeningForm';
import accountReducer from '../store/slices/accountSlice';
import { describe, it, expect, beforeEach } from 'vitest';

const createTestStore = () => {
  return configureStore({
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

describe('AccountOpeningForm', () => {
  beforeEach(() => {
    localStorage.setItem('authToken', 'test-token');
  });

  it('renders account opening form', () => {
    renderWithProviders(<AccountOpeningForm />);
    expect(screen.getByText('Open New Account')).toBeInTheDocument();
  });

  it('has required input fields', () => {
    renderWithProviders(<AccountOpeningForm />);
    // Main customer ID field
    expect(screen.getByLabelText(/customer id/i)).toHaveAttribute('aria-required', 'true');
    // Only one field with label "Customer ID" (the main one)
    expect(screen.getAllByLabelText(/customer id/i)).toHaveLength(1);
  });

  it('renders Product Code input', () => {
    renderWithProviders(<AccountOpeningForm />);
    expect(screen.getByPlaceholderText(/e\.g\., PROD-CHECKING/i)).toBeInTheDocument();
  });

  it('has account type dropdown', () => {
    renderWithProviders(<AccountOpeningForm />);
    expect(screen.getByLabelText(/account type/i)).toBeInTheDocument();
  });

  it('has currency dropdown', () => {
    renderWithProviders(<AccountOpeningForm />);
    expect(screen.getByLabelText(/currency/i)).toBeInTheDocument();
  });

  it('renders Initial Deposit input', () => {
    renderWithProviders(<AccountOpeningForm />);
    expect(screen.getByLabelText(/initial deposit/i)).toBeInTheDocument();
  });

  it('has add holder functionality', () => {
    renderWithProviders(<AccountOpeningForm />);
    expect(screen.getByText(/add holder/i)).toBeInTheDocument();
    // Customer ID for holder (input with placeholder)
    expect(screen.getAllByPlaceholderText(/customer id/i)).toHaveLength(2);
    // Role dropdown exists
    expect(screen.getByLabelText(/role/i)).toBeInTheDocument();
  });

  it('has submit and cancel buttons', () => {
    renderWithProviders(<AccountOpeningForm />);
    expect(screen.getByText(/open account/i)).toBeInTheDocument();
    expect(screen.getByText(/cancel/i)).toBeInTheDocument();
  });
});
import { render, screen } from '@testing-library/react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MemoryRouter } from 'react-router-dom';
import { CustomerListPage } from '../pages/customers/CustomerListPage';
import customerReducer from '../store/slices/customerSlice';
import { describe, it, expect, beforeEach } from 'vitest';

const createTestStore = () => {
  return configureStore({
    reducer: {
      customer: customerReducer,
    },
    preloadedState: {
      customer: {
        customers: [],
        selectedCustomer: null,
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

describe('CustomerListPage', () => {
  beforeEach(() => {
    localStorage.setItem('authToken', 'test-token');
  });

  it('renders customer list page', () => {
    renderWithProviders(<CustomerListPage />);
    expect(screen.getByText('Customers')).toBeInTheDocument();
  });

  it('renders Add Customer button', () => {
    renderWithProviders(<CustomerListPage />);
    expect(screen.getByText('Add Customer')).toBeInTheDocument();
  });

  it('renders search input', () => {
    renderWithProviders(<CustomerListPage />);
    expect(screen.getByPlaceholderText(/search by name/i)).toBeInTheDocument();
  });

  it('renders customer type filter', () => {
    renderWithProviders(<CustomerListPage />);
    expect(screen.getByText('Customer Type')).toBeInTheDocument();
  });

  it('renders status filter', () => {
    renderWithProviders(<CustomerListPage />);
    expect(screen.getAllByText('Status').length).toBeGreaterThan(0);
  });
});

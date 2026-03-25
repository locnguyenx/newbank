import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen } from '@testing-library/react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MemoryRouter } from 'react-router-dom';
import { CustomerListPage } from './CustomerListPage';

const mockDispatch = vi.fn();
vi.mock('@/hooks/useRedux', () => ({
  useAppDispatch: () => mockDispatch,
  useAppSelector: vi.fn((fn) => fn({ customer: { customers: [], loading: false } })),
}));

vi.mock('@/store/slices/customerSlice', () => ({
  fetchCustomers: vi.fn(() => ({ type: 'fetchCustomers' })),
}));

describe('CustomerListPage', () => {
  const createStore = () =>
    configureStore({
      reducer: {
        customer: () => ({ customers: [], loading: false, error: null, selectedCustomer: null }),
      },
    });

  beforeEach(() => {
    vi.clearAllMocks();
    mockDispatch.mockClear();
  });

  it('should display customers title', () => {
    render(
      <Provider store={createStore()}>
        <MemoryRouter>
          <CustomerListPage />
        </MemoryRouter>
      </Provider>
    );
    expect(screen.getByText('Customers')).toBeInTheDocument();
  });

  it('should display add customer button', () => {
    render(
      <Provider store={createStore()}>
        <MemoryRouter>
          <CustomerListPage />
        </MemoryRouter>
      </Provider>
    );
    expect(screen.getByText('Add Customer')).toBeInTheDocument();
  });

  it('should display search input', () => {
    render(
      <Provider store={createStore()}>
        <MemoryRouter>
          <CustomerListPage />
        </MemoryRouter>
      </Provider>
    );
    expect(screen.getByPlaceholderText('Search by name, number, or email')).toBeInTheDocument();
  });
});

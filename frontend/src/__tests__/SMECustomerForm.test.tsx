import { render, screen } from '@testing-library/react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MemoryRouter } from 'react-router-dom';
import { SMECustomerForm } from '../pages/customers/SMECustomerForm';
import customerReducer from '../store/slices/customerSlice';
import { describe, it, expect } from 'vitest';

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

describe('SMECustomerForm', () => {
  it('renders SME customer form', () => {
    renderWithProviders(<SMECustomerForm />);
    expect(screen.getByText('New SME Customer')).toBeInTheDocument();
  });

  it('renders form fields', () => {
    renderWithProviders(<SMECustomerForm />);
    expect(screen.getByLabelText('Company Name')).toBeInTheDocument();
    expect(screen.getByLabelText('Registration Number')).toBeInTheDocument();
    expect(screen.getByLabelText('Incorporation Date')).toBeInTheDocument();
    expect(screen.getByLabelText('Industry')).toBeInTheDocument();
    expect(screen.getByLabelText('Number of Employees')).toBeInTheDocument();
    expect(screen.getByLabelText('Email')).toBeInTheDocument();
    expect(screen.getByLabelText('Phone Number')).toBeInTheDocument();
  });

  it('renders submit button', () => {
    renderWithProviders(<SMECustomerForm />);
    expect(screen.getByText('Create')).toBeInTheDocument();
  });
});

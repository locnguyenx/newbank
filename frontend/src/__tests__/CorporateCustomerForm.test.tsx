import { render, screen } from '@testing-library/react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MemoryRouter } from 'react-router-dom';
import { CorporateCustomerForm } from '../pages/customers/CorporateCustomerForm';
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

describe('CorporateCustomerForm', () => {
  it('renders corporate customer form', () => {
    renderWithProviders(<CorporateCustomerForm />);
    expect(screen.getByText('New Corporate Customer')).toBeInTheDocument();
  });

  it('renders form fields', () => {
    renderWithProviders(<CorporateCustomerForm />);
    expect(screen.getByLabelText('Company Name')).toBeInTheDocument();
    expect(screen.getByLabelText('Registration Number')).toBeInTheDocument();
    expect(screen.getByLabelText('Incorporation Date')).toBeInTheDocument();
    expect(screen.getByLabelText('Industry')).toBeInTheDocument();
    expect(screen.getByLabelText('Employee Count Range')).toBeInTheDocument();
    expect(screen.getByLabelText('Annual Revenue')).toBeInTheDocument();
    expect(screen.getByLabelText('Revenue Currency')).toBeInTheDocument();
    expect(screen.getByLabelText('Email')).toBeInTheDocument();
    expect(screen.getByLabelText('Phone Number')).toBeInTheDocument();
  });

  it('renders submit button', () => {
    renderWithProviders(<CorporateCustomerForm />);
    expect(screen.getByText('Create')).toBeInTheDocument();
  });
});

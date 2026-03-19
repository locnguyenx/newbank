import { render, screen } from '@testing-library/react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MemoryRouter } from 'react-router-dom';
import { IndividualCustomerForm } from '../pages/customers/IndividualCustomerForm';
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

describe('IndividualCustomerForm', () => {
  it('renders individual customer form', () => {
    renderWithProviders(<IndividualCustomerForm />);
    expect(screen.getByText('New Individual Customer')).toBeInTheDocument();
  });

  it('renders form fields', () => {
    renderWithProviders(<IndividualCustomerForm />);
    expect(screen.getByLabelText('First Name')).toBeInTheDocument();
    expect(screen.getByLabelText('Last Name')).toBeInTheDocument();
    expect(screen.getByLabelText('Date of Birth')).toBeInTheDocument();
    expect(screen.getByLabelText('ID Number')).toBeInTheDocument();
    expect(screen.getByLabelText('Email')).toBeInTheDocument();
    expect(screen.getByLabelText('Phone Number')).toBeInTheDocument();
  });

  it('renders submit button', () => {
    renderWithProviders(<IndividualCustomerForm />);
    expect(screen.getByText('Create')).toBeInTheDocument();
  });

  it('renders back button', () => {
    renderWithProviders(<IndividualCustomerForm />);
    expect(screen.getByText('Back')).toBeInTheDocument();
  });
});

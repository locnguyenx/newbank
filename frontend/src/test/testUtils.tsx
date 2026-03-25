import React, { ReactNode } from 'react';
import { Provider } from 'react-redux';
import { configureStore, createSlice, EnhancedStore } from '@reduxjs/toolkit';
import type { RenderOptions } from '@testing-library/react';
import { BrowserRouter, MemoryRouter } from 'react-router-dom';

export const createTestStore = (preloadedState: Record<string, any> = {}) => {
  const rootReducer = Object.keys(preloadedState).reduce((acc, key) => {
    acc[key] = (state: any) => state?.[key] || preloadedState[key];
    return acc;
  }, {} as Record<string, any>);

  return configureStore({
    reducer: rootReducer,
  });
};

export const mockAuthState = {
  user: { email: 'test@bank.com', roles: ['SYSTEM_ADMIN'], mfaEnabled: false },
  isAuthenticated: true,
  loading: false,
  error: null,
  mfaRequired: false,
};

export const mockCustomersState = {
  customers: [],
  selectedCustomer: null,
  loading: false,
  error: null,
};

export const mockAccountsState = {
  accounts: [],
  selectedAccount: null,
  loading: false,
  error: null,
};

export const mockProductsState = {
  products: [],
  selectedProduct: null,
  loading: false,
  error: null,
};

export const mockLimitsState = {
  limits: [],
  loading: false,
  error: null,
};

export const mockChargesState = {
  charges: [],
  loading: false,
  error: null,
};

export const mockEmploymentState = {
  employees: [],
  loading: false,
  error: null,
};

export interface WrapperProps {
  children: ReactNode;
}

export const createMockStore = (state: Record<string, any> = {}) => {
  const slices = Object.entries(state).reduce((acc, [key, sliceState]) => {
    acc[key] = createSlice({
      name: key,
      initialState: sliceState,
      reducers: {},
    }).reducer;
    return acc;
  }, {} as Record<string, any>);

  return configureStore({
    reducer: slices,
  });
};

export const renderWithProviders = (
  ui: React.ReactElement,
  {
    preloadedState = {},
    store = createMockStore(preloadedState),
    ...renderOptions
  }: {
    preloadedState?: Record<string, any>;
    store?: EnhancedStore;
    renderOptions?: Omit<RenderOptions, 'wrapper'>;
  } = {}
) => {
  const Wrapper = ({ children }: WrapperProps) => (
    <Provider store={store}>
      <BrowserRouter>{children}</BrowserRouter>
    </Provider>
  );

  return {
    store,
    ...require('@testing-library/react').render(ui, { wrapper: Wrapper, ...renderOptions }),
  };
};

export const renderWithRouter = (
  ui: React.ReactElement,
  { route = '/' } = {}
) => {
  window.history.pushState({}, 'Test', route);
  return {
    ...require('@testing-library/react').render(
      <MemoryRouter initialEntries={[route]}>{ui}</MemoryRouter>
    ),
  };
};

import '@testing-library/jest-dom';
import { vi } from 'vitest';

// Polyfill matchMedia for Antd responsive components (plain function, not a vi.fn mock)
window.matchMedia = (query) => ({
  matches: false,
  media: query,
  onchange: null,
  addListener: () => {},
  removeListener: () => {},
  addEventListener: () => {},
  removeEventListener: () => {},
  dispatchEvent: () => false,
});

class ResizeObserver {
  observe() {}
  unobserve() {}
  disconnect() {}
}
window.ResizeObserver = ResizeObserver;

// Mock Redux store for testing
const createMockStore = () => {
  return {
    dispatch: vi.fn(),
    getState: vi.fn(() => ({})),
    subscribe: vi.fn(),
  };
};

// Default mock state - must match Redux store keys
const defaultState = {
  account: {
    accounts: [],
    selectedAccount: null,
    pagination: { page: 1, size: 10, total: 0, totalElements: 0 },
    loading: false,
    error: null,
  },
  customer: {
    customers: [],
    selectedCustomer: null,
    pagination: { page: 1, size: 10, total: 0, totalElements: 0 },
    loading: false,
    error: null,
  },
  // Must match store key names (plural form)
  products: {
    products: [],
    selectedProduct: null,
    pagination: { page: 1, size: 10, total: 0, totalElements: 0 },
    loading: false,
    error: null,
  },
  masterData: {
    currencies: [],
    countries: [],
    industries: [],
    exchangeRates: [],
    holidays: [],
    loading: false,
    error: null,
  },
  limits: {
    limits: [],
    loading: false,
    error: null,
  },
  charges: {
    charges: [],
    chargeRules: [],
    productCharges: [],
    customerOverrides: [],
    loading: false,
    error: null,
  },
  employment: {
    employees: [],
    loading: false,
    error: null,
  },
  auth: {
    user: null,
    isAuthenticated: false,
    loading: false,
    error: null,
    mfaRequired: false,
  },
};

// Global mock for useAppSelector
vi.mock('@/hooks/useRedux', () => ({
  useAppDispatch: () => vi.fn(),
  useAppSelector: vi.fn((selector) => {
    const state = defaultState;
    return selector(state);
  }),
}));

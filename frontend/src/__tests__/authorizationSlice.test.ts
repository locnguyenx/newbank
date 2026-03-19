import { describe, it, expect } from 'vitest';
import authorizationReducer, {
  setLoading,
  setError,
  setCurrentAuthorization,
  clearError,
} from '../store/slices/authorizationSlice';
import type { Authorization } from '../types/authorization.types';

describe('authorizationSlice', () => {
  const initialState = {
    authorizations: [],
    currentAuthorization: null,
    loading: false,
    error: null,
  };

  it('should return the initial state', () => {
    expect(authorizationReducer(undefined, { type: 'unknown' })).toEqual(initialState);
  });

  it('should handle setLoading', () => {
    const actual = authorizationReducer(initialState, setLoading(true));
    expect(actual.loading).toBe(true);
  });

  it('should handle setError', () => {
    const actual = authorizationReducer(initialState, setError('Error message'));
    expect(actual.error).toBe('Error message');
  });

  it('should handle setCurrentAuthorization', () => {
    const authorization: Authorization = {
      id: 1,
      customerId: 1,
      customerNumber: 'CUST-001',
      authorizationType: 'SIGNATORY',
      status: 'ACTIVE',
      authorizedPersonName: 'John Doe',
      authorizedPersonEmail: 'john@example.com',
      authorizedPersonPhone: '+1234567890',
      transactionLimit: 10000,
      currency: 'USD',
      effectiveDate: '2024-01-01',
      expiryDate: null,
      documents: [],
      notes: null,
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    };
    const actual = authorizationReducer(initialState, setCurrentAuthorization(authorization));
    expect(actual.currentAuthorization).toEqual(authorization);
  });

  it('should handle clearError', () => {
    const stateWithError = { ...initialState, error: 'Some error' };
    const actual = authorizationReducer(stateWithError, clearError());
    expect(actual.error).toBeNull();
  });
});

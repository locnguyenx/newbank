import { describe, it, expect } from 'vitest';
import kycReducer, {
  setLoading,
  setError,
  setCurrentKYC,
} from '../store/slices/kycSlice';
import type { KYC } from '../types/kyc.types';

describe('kycSlice', () => {
  const initialState = {
    currentKYC: null,
    pendingReviews: [],
    loading: false,
    error: null,
  };

  it('should return the initial state', () => {
    expect(kycReducer(undefined, { type: 'unknown' })).toEqual(initialState);
  });

  it('should handle setLoading', () => {
    const actual = kycReducer(initialState, setLoading(true));
    expect(actual.loading).toBe(true);
  });

  it('should handle setError', () => {
    const actual = kycReducer(initialState, setError('Error message'));
    expect(actual.error).toBe('Error message');
  });

  it('should handle setCurrentKYC', () => {
    const kyc: KYC = {
      id: 1,
      customerId: 1,
      customerNumber: 'CUST-001',
      status: 'PENDING',
      documents: [],
      sanctionsScreening: null,
      reviewTimeline: [],
      createdAt: '2024-01-01T00:00:00Z',
      updatedAt: '2024-01-01T00:00:00Z',
    };
    const actual = kycReducer(initialState, setCurrentKYC(kyc));
    expect(actual.currentKYC).toEqual(kyc);
  });
});

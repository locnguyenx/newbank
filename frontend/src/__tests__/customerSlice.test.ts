import { describe, it, expect } from 'vitest';
import customerReducer, {
  setLoading,
  setError,
  setCustomers,
  setSelectedCustomer,
} from '../store/slices/customerSlice';
import type { CustomerVariant } from '../types';

describe('customerSlice', () => {
  const initialState = {
    customers: [],
    selectedCustomer: null,
    loading: false,
    error: null,
  };

  it('should return the initial state', () => {
    expect(customerReducer(undefined, { type: 'unknown' })).toEqual(initialState);
  });

  it('should handle setLoading', () => {
    const actual = customerReducer(initialState, setLoading(true));
    expect(actual.loading).toBe(true);
  });

  it('should handle setError', () => {
    const actual = customerReducer(initialState, setError('Error message'));
    expect(actual.error).toBe('Error message');
  });

  it('should handle setCustomers', () => {
    const customers: CustomerVariant[] = [
      {
        id: 1,
        customerNumber: 'CUST-001',
        customerType: 'INDIVIDUAL',
        status: 'ACTIVE',
        firstName: 'John',
        lastName: 'Doe',
        dateOfBirth: '1990-01-15',
        email: 'john@example.com',
        phoneNumber: '+1234567890',
        idNumber: 'ID123',
        createdAt: '2024-01-01',
        createdBy: 'admin',
        updatedAt: '2024-01-01',
        updatedBy: 'admin',
      },
    ];
    const actual = customerReducer(initialState, setCustomers(customers));
    expect(actual.customers).toEqual(customers);
  });

  it('should handle setSelectedCustomer', () => {
    const customer: CustomerVariant = {
      id: 1,
      customerNumber: 'CUST-001',
      customerType: 'INDIVIDUAL',
      status: 'ACTIVE',
      firstName: 'John',
      lastName: 'Doe',
      dateOfBirth: '1990-01-15',
      email: 'john@example.com',
      phoneNumber: '+1234567890',
      idNumber: 'ID123',
      createdAt: '2024-01-01',
      createdBy: 'admin',
      updatedAt: '2024-01-01',
      updatedBy: 'admin',
    };
    const actual = customerReducer(initialState, setSelectedCustomer(customer));
    expect(actual.selectedCustomer).toEqual(customer);
  });
});

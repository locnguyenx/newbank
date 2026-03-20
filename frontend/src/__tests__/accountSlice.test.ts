import { describe, it, expect } from 'vitest';
import accountReducer, {
  clearSelectedAccount,
  clearError,
  fetchAccounts,
  fetchAccountByNumber,
  closeAccount,
  freezeAccount,
  unfreezeAccount,
} from '../store/slices/accountSlice';
import type { AccountResponse, AccountDetails } from '../types/account.types';

describe('accountSlice', () => {
  const initialState = {
    accounts: [],
    selectedAccount: null,
    loading: false,
    error: null,
  };

  it('should return the initial state', () => {
    expect(accountReducer(undefined, { type: 'unknown' })).toEqual(initialState);
  });

  it('should handle clearSelectedAccount', () => {
    const stateWithSelected = {
      ...initialState,
      selectedAccount: {
        id: 1,
        accountNumber: 'ACC-001',
        type: 'CURRENT' as const,
        status: 'ACTIVE' as const,
        currency: 'USD' as const,
        balance: 1000,
        productId: 1,
        openedAt: '2024-01-01T00:00:00Z',
        closedAt: null,
      } as AccountDetails,
    };
    const actual = accountReducer(stateWithSelected, clearSelectedAccount());
    expect(actual.selectedAccount).toBeNull();
  });

  it('should handle clearError', () => {
    const stateWithError = {
      ...initialState,
      error: 'Some error message',
    };
    const actual = accountReducer(stateWithError, clearError());
    expect(actual.error).toBeNull();
  });

  describe('async thunk reducers', () => {
    const mockAccount: AccountResponse = {
      id: 1,
      accountNumber: 'ACC-001',
      type: 'CURRENT',
      status: 'ACTIVE',
      currency: 'USD',
      balance: 1000,
      productId: 1,
      openedAt: '2024-01-01T00:00:00Z',
      closedAt: null,
    };

    const mockAccountDetails: AccountDetails = {
      ...mockAccount,
      accountBalance: {
        availableBalance: 1000,
        ledgerBalance: 1000,
        holdAmount: 0,
        currency: 'USD',
      },
      holders: [],
    };

    it('should handle fetchAccounts.fulfilled', () => {
      const action = {
        type: fetchAccounts.fulfilled.type,
        payload: [mockAccount],
      };
      const actual = accountReducer(initialState, action);
      expect(actual.loading).toBe(false);
      expect(actual.accounts).toEqual([mockAccount]);
    });

    it('should handle fetchAccountByNumber.fulfilled', () => {
      const action = {
        type: fetchAccountByNumber.fulfilled.type,
        payload: mockAccountDetails,
      };
      const actual = accountReducer(initialState, action);
      expect(actual.loading).toBe(false);
      expect(actual.selectedAccount).toEqual(mockAccountDetails);
    });

    it('should handle closeAccount.fulfilled', () => {
      const stateWithAccount = {
        ...initialState,
        accounts: [mockAccount],
        selectedAccount: mockAccountDetails,
      };
      const action = {
        type: closeAccount.fulfilled.type,
        meta: { arg: 'ACC-001' },
      };
      const actual = accountReducer(stateWithAccount, action);
      expect(actual.accounts[0].status).toBe('CLOSED');
      expect(actual.selectedAccount?.status).toBe('CLOSED');
    });

    it('should handle freezeAccount.fulfilled', () => {
      const stateWithAccount = {
        ...initialState,
        accounts: [mockAccount],
        selectedAccount: mockAccountDetails,
      };
      const action = {
        type: freezeAccount.fulfilled.type,
        meta: { arg: 'ACC-001' },
      };
      const actual = accountReducer(stateWithAccount, action);
      expect(actual.accounts[0].status).toBe('FROZEN');
      expect(actual.selectedAccount?.status).toBe('FROZEN');
    });

    it('should handle unfreezeAccount.fulfilled', () => {
      const frozenAccount = { ...mockAccount, status: 'FROZEN' as const };
      const frozenDetails = { ...mockAccountDetails, status: 'FROZEN' as const };
      const stateWithFrozenAccount = {
        ...initialState,
        accounts: [frozenAccount],
        selectedAccount: frozenDetails,
      };
      const action = {
        type: unfreezeAccount.fulfilled.type,
        meta: { arg: 'ACC-001' },
      };
      const actual = accountReducer(stateWithFrozenAccount, action);
      expect(actual.accounts[0].status).toBe('ACTIVE');
      expect(actual.selectedAccount?.status).toBe('ACTIVE');
    });
  });
});

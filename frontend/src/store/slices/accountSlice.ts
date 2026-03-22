import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import type { AccountResponse, AccountDetails, AccountOpeningRequest } from '@/types/account.types';
import { accountService } from '@/services/accountService';

interface PaginationMetadata {
  totalElements: number;
  totalPages: number;
}

interface AccountState {
  accounts: AccountResponse[];
  pagination: PaginationMetadata;
  selectedAccount: AccountDetails | null;
  loading: boolean;
  error: string | null;
}

const initialState: AccountState = {
  accounts: [],
  pagination: {
    totalElements: 0,
    totalPages: 0,
  },
  selectedAccount: null,
  loading: false,
  error: null,
};

interface FetchAccountsParams {
  search?: string;
  type?: string;
  status?: string;
  customerId?: number;
  page?: number;
  size?: number;
}

export const fetchAccounts = createAsyncThunk<{ content: AccountResponse[]; totalElements: number; totalPages: number }, FetchAccountsParams>(
  'account/fetchAll',
  async (params = {}) => {
    return await accountService.getAll(params);
  }
);

export const fetchAccountByNumber = createAsyncThunk<AccountDetails, string>(
  'account/fetchByNumber',
  async (accountNumber) => {
    return await accountService.getByAccountNumber(accountNumber);
  }
);

export const openAccount = createAsyncThunk<AccountResponse, AccountOpeningRequest>(
  'account/open',
  async (data) => {
    return await accountService.openAccount(data);
  }
);

export const closeAccount = createAsyncThunk<void, string>(
  'account/close',
  async (accountNumber, { rejectWithValue }) => {
    try {
      await accountService.closeAccount(accountNumber);
    } catch (error: any) {
      const message = error.response?.data?.message || error.message || 'Failed to close account';
      return rejectWithValue(message);
    }
  }
);

export const freezeAccount = createAsyncThunk<void, string>(
  'account/freeze',
  async (accountNumber, { rejectWithValue }) => {
    try {
      await accountService.freezeAccount(accountNumber);
    } catch (error: any) {
      const message = error.response?.data?.message || error.message || 'Failed to freeze account';
      return rejectWithValue(message);
    }
  }
);

export const unfreezeAccount = createAsyncThunk<void, string>(
  'account/unfreeze',
  async (accountNumber, { rejectWithValue }) => {
    try {
      await accountService.unfreezeAccount(accountNumber);
    } catch (error: any) {
      const message = error.response?.data?.message || error.message || 'Failed to unfreeze account';
      return rejectWithValue(message);
    }
  }
);

export const addAccountHolder = createAsyncThunk<void, { accountNumber: string; holderData: { customerId: number; role: string } }>(
  'account/addHolder',
  async ({ accountNumber, holderData }) => {
    await accountService.addHolder(accountNumber, holderData);
  }
);

export const removeAccountHolder = createAsyncThunk<void, { accountNumber: string; holderId: number }>(
  'account/removeHolder',
  async ({ accountNumber, holderId }) => {
    await accountService.removeHolder(accountNumber, holderId);
  }
);

const accountSlice = createSlice({
  name: 'account',
  initialState,
  reducers: {
    clearSelectedAccount: (state) => {
      state.selectedAccount = null;
    },
    clearError: (state) => {
      state.error = null;
    }
  },
  extraReducers: (builder) => {
    builder
      // fetchAccounts
      .addCase(fetchAccounts.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchAccounts.fulfilled, (state, action) => {
        state.loading = false;
        state.accounts = action.payload.content;
        state.pagination.totalElements = action.payload.totalElements;
        state.pagination.totalPages = action.payload.totalPages;
      })
      .addCase(fetchAccounts.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch accounts';
      })
      // fetchAccountByNumber
      .addCase(fetchAccountByNumber.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchAccountByNumber.fulfilled, (state, action) => {
        state.loading = false;
        state.selectedAccount = action.payload;
      })
      .addCase(fetchAccountByNumber.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch account';
      })
      // openAccount
      .addCase(openAccount.fulfilled, (state, action) => {
        const existingIndex = state.accounts.findIndex(a => a.id === action.payload.id);
        if (existingIndex !== -1) {
          state.accounts[existingIndex] = action.payload; // Replace if exists
        } else {
          state.accounts.push(action.payload);
        }
      })
      // closeAccount, freezeAccount, unfreezeAccount: just update local state optimistically
      .addCase(closeAccount.fulfilled, (state, action) => {
        // The account number is in action.meta.arg
        const accountNumber = action.meta.arg;
        const idx = state.accounts.findIndex(a => a.accountNumber === accountNumber);
        if (idx !== -1) {
          state.accounts[idx].status = 'CLOSED';
        }
        if (state.selectedAccount?.accountNumber === accountNumber) {
          state.selectedAccount.status = 'CLOSED';
        }
      })
      .addCase(freezeAccount.fulfilled, (state, action) => {
        const accountNumber = action.meta.arg;
        const idx = state.accounts.findIndex(a => a.accountNumber === accountNumber);
        if (idx !== -1) {
          state.accounts[idx].status = 'FROZEN';
        }
        if (state.selectedAccount?.accountNumber === accountNumber) {
          state.selectedAccount.status = 'FROZEN';
        }
      })
      .addCase(unfreezeAccount.fulfilled, (state, action) => {
        const accountNumber = action.meta.arg;
        const idx = state.accounts.findIndex(a => a.accountNumber === accountNumber);
        if (idx !== -1) {
          state.accounts[idx].status = 'ACTIVE';
        }
        if (state.selectedAccount?.accountNumber === accountNumber) {
          state.selectedAccount.status = 'ACTIVE';
        }
      })
      // addAccountHolder: refresh selected account to show new holder
      .addCase(addAccountHolder.fulfilled, (state) => {
        if (state.selectedAccount) {
          // In real app, we'd refetch; for now, leave as is
        }
      })
      // removeAccountHolder
      .addCase(removeAccountHolder.fulfilled, (state, action) => {
        const holderId = action.meta.arg.holderId;
        if (state.selectedAccount?.holders) {
          state.selectedAccount.holders = state.selectedAccount.holders.filter(h => h.id !== holderId);
        }
      });
  }
});

export const { clearSelectedAccount, clearError } = accountSlice.actions;
export default accountSlice.reducer;

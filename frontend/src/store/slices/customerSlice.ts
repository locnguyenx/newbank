import { createSlice, type PayloadAction } from '@reduxjs/toolkit';
import type { CustomerVariant } from '@/types';

interface CustomerState {
  customers: CustomerVariant[];
  selectedCustomer: CustomerVariant | null;
  loading: boolean;
  error: string | null;
}

const initialState: CustomerState = {
  customers: [],
  selectedCustomer: null,
  loading: false,
  error: null,
};

const customerSlice = createSlice({
  name: 'customer',
  initialState,
  reducers: {
    setLoading: (state, action: PayloadAction<boolean>) => {
      state.loading = action.payload;
    },
    setError: (state, action: PayloadAction<string | null>) => {
      state.error = action.payload;
    },
    setCustomers: (state, action: PayloadAction<CustomerVariant[]>) => {
      state.customers = action.payload;
    },
    setSelectedCustomer: (state, action: PayloadAction<CustomerVariant | null>) => {
      state.selectedCustomer = action.payload;
    },
  },
});

export const { setLoading, setError, setCustomers, setSelectedCustomer } = customerSlice.actions;
export default customerSlice.reducer;

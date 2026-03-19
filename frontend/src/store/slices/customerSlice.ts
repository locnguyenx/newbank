import { createSlice, createAsyncThunk, type PayloadAction } from '@reduxjs/toolkit';
import type { CustomerVariant } from '@/types';
import { customerService } from '@/services/customerService';
import { demoCustomers } from '@/data/demoCustomers';

interface CustomerState {
  customers: CustomerVariant[];
  selectedCustomer: CustomerVariant | null;
  loading: boolean;
  error: string | null;
}

const initialState: CustomerState = {
  customers: demoCustomers,  // Use demo data as initial state
  selectedCustomer: null,
  loading: false,
  error: null,
};

export const fetchCustomers = createAsyncThunk<CustomerVariant[], void>(
  'customer/fetchAll',
  async () => {
    try {
      return await customerService.getAll();
    } catch {
      // Return demo data if API fails
      return demoCustomers;
    }
  }
);

export const fetchCustomerById = createAsyncThunk<CustomerVariant, number>(
  'customer/fetchById',
  async (id) => {
    try {
      return await customerService.getById(id);
    } catch {
      // Return demo data if API fails
      return demoCustomers.find(c => c.id === id) || demoCustomers[0];
    }
  }
);

interface CreateIndividualPayload {
  customerType: 'INDIVIDUAL';
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  email: string;
  phoneNumber: string;
  idNumber: string;
}

interface CreateSMEPayload {
  customerType: 'SME';
  companyName: string;
  registrationNumber: string;
  incorporationDate: string;
  email: string;
  phoneNumber: string;
  industry: string;
  numberOfEmployees: number;
}

interface CreateCorporatePayload {
  customerType: 'CORPORATE';
  companyName: string;
  registrationNumber: string;
  incorporationDate: string;
  email: string;
  phoneNumber: string;
  industry: string;
  employeeCountRange: string;
  annualRevenue: number;
  revenueCurrency: string;
}

export type CreateCustomerPayload = CreateIndividualPayload | CreateSMEPayload | CreateCorporatePayload;

export const createCustomer = createAsyncThunk<CustomerVariant, CreateCustomerPayload>(
  'customer/create',
  async (data) => {
    return customerService.create(data as unknown as Record<string, unknown>);
  }
);

interface UpdateIndividualPayload {
  id: number;
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  email: string;
  phoneNumber: string;
  idNumber: string;
}

interface UpdateSMEPayload {
  id: number;
  companyName: string;
  registrationNumber: string;
  incorporationDate: string;
  email: string;
  phoneNumber: string;
  industry: string;
  numberOfEmployees: number;
}

interface UpdateCorporatePayload {
  id: number;
  companyName: string;
  registrationNumber: string;
  incorporationDate: string;
  email: string;
  phoneNumber: string;
  industry: string;
  employeeCountRange: string;
  annualRevenue: number;
  revenueCurrency: string;
}

export type UpdateCustomerPayload = UpdateIndividualPayload | UpdateSMEPayload | UpdateCorporatePayload;

export const updateCustomer = createAsyncThunk<CustomerVariant, UpdateCustomerPayload>(
  'customer/update',
  async (data) => {
    const id = 'id' in data ? data.id : 0;
    return customerService.update(id, data as unknown as Record<string, unknown>);
  }
);

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
  extraReducers: (builder) => {
    builder
      .addCase(fetchCustomers.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchCustomers.fulfilled, (state, action) => {
        state.loading = false;
        state.customers = action.payload;
      })
      .addCase(fetchCustomers.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch customers';
      })
      .addCase(fetchCustomerById.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchCustomerById.fulfilled, (state, action) => {
        state.loading = false;
        state.selectedCustomer = action.payload;
      })
      .addCase(fetchCustomerById.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch customer';
      })
      .addCase(createCustomer.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(createCustomer.fulfilled, (state, action) => {
        state.loading = false;
        state.customers.push(action.payload);
      })
      .addCase(createCustomer.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to create customer';
      })
      .addCase(updateCustomer.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(updateCustomer.fulfilled, (state, action) => {
        state.loading = false;
        const index = state.customers.findIndex((c) => c.id === action.payload.id);
        if (index !== -1) {
          state.customers[index] = action.payload;
        }
        state.selectedCustomer = action.payload;
      })
      .addCase(updateCustomer.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to update customer';
      });
  },
});

export const { setLoading, setError, setCustomers, setSelectedCustomer } = customerSlice.actions;
export default customerSlice.reducer;

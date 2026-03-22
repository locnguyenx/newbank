import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import type {
  LimitDefinition,
  ProductLimit,
  CustomerLimit,
  AccountLimit,
  ApprovalRequest,
  LimitCheckRequest,
  LimitCheckResponse,
  EffectiveLimitResponse,
  CreateLimitDefinitionRequest,
  AssignLimitRequest,
  LimitStatus,
} from '@/types/limit.types';
import type { PaginatedResponse } from '@/types/product.types';
import { limitService } from '@/services/limitService';

interface LimitState {
  limits: LimitDefinition[];
  productLimits: ProductLimit[];
  customerLimits: CustomerLimit[];
  accountLimits: AccountLimit[];
  approvals: ApprovalRequest[];
  pagination: {
    totalElements: number;
    totalPages: number;
  };
  lastCheckResult: LimitCheckResponse | null;
  effectiveLimits: EffectiveLimitResponse[];
  loading: boolean;
  error: string | null;
}

const initialState: LimitState = {
  limits: [],
  productLimits: [],
  customerLimits: [],
  accountLimits: [],
  approvals: [],
  pagination: {
    totalElements: 0,
    totalPages: 0,
  },
  lastCheckResult: null,
  effectiveLimits: [],
  loading: false,
  error: null,
};

export const fetchLimitDefinitions = createAsyncThunk<LimitDefinition[], LimitStatus | undefined>(
  'limits/fetchDefinitions',
  async (status) => {
    return await limitService.getLimitDefinitions(status);
  }
);

export const createLimitDefinition = createAsyncThunk<LimitDefinition, CreateLimitDefinitionRequest>(
  'limits/createDefinition',
  async (data) => {
    return await limitService.createLimitDefinition(data);
  }
);

export const updateLimitDefinition = createAsyncThunk<LimitDefinition, { id: number; data: CreateLimitDefinitionRequest }>(
  'limits/updateDefinition',
  async ({ id, data }) => {
    return await limitService.updateLimitDefinition(id, data);
  }
);

export const activateLimit = createAsyncThunk<LimitDefinition, number>(
  'limits/activate',
  async (id) => {
    return await limitService.activateLimit(id);
  }
);

export const deactivateLimit = createAsyncThunk<LimitDefinition, number>(
  'limits/deactivate',
  async (id) => {
    return await limitService.deactivateLimit(id);
  }
);

export const assignToProduct = createAsyncThunk<ProductLimit, AssignLimitRequest>(
  'limits/assignToProduct',
  async (data) => {
    return await limitService.assignToProduct(data);
  }
);

export const assignToCustomer = createAsyncThunk<CustomerLimit, AssignLimitRequest>(
  'limits/assignToCustomer',
  async (data) => {
    return await limitService.assignToCustomer(data);
  }
);

export const assignToAccount = createAsyncThunk<AccountLimit, AssignLimitRequest>(
  'limits/assignToAccount',
  async (data) => {
    return await limitService.assignToAccount(data);
  }
);

export const checkLimit = createAsyncThunk<LimitCheckResponse, LimitCheckRequest>(
  'limits/check',
  async (data) => {
    return await limitService.checkLimit(data);
  }
);

export const getEffectiveLimits = createAsyncThunk<EffectiveLimitResponse[], { accountNumber: string; customerId?: number; productCode?: string; currency?: string }>(
  'limits/getEffectiveLimits',
  async ({ accountNumber, customerId, productCode, currency }) => {
    return await limitService.getEffectiveLimits(accountNumber, customerId, productCode, currency);
  }
);

export const fetchPendingApprovals = createAsyncThunk<PaginatedResponse<ApprovalRequest>, { page?: number; size?: number } | undefined>(
  'limits/fetchPendingApprovals',
  async ({ page, size } = {}) => {
    return await limitService.getPendingApprovals(page, size);
  }
);

export const approveApproval = createAsyncThunk<ApprovalRequest, number>(
  'limits/approveApproval',
  async (id) => {
    return await limitService.approveApproval(id);
  }
);

export const rejectApproval = createAsyncThunk<ApprovalRequest, { id: number; reason?: string }>(
  'limits/rejectApproval',
  async ({ id, reason }) => {
    return await limitService.rejectApproval(id, reason);
  }
);

const limitSlice = createSlice({
  name: 'limits',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
    clearCheckResult: (state) => {
      state.lastCheckResult = null;
    },
  },
  extraReducers: (builder) => {
    builder
      // fetchLimitDefinitions
      .addCase(fetchLimitDefinitions.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchLimitDefinitions.fulfilled, (state, action) => {
        state.loading = false;
        state.limits = action.payload;
      })
      .addCase(fetchLimitDefinitions.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch limit definitions';
      })
      // createLimitDefinition
      .addCase(createLimitDefinition.fulfilled, (state, action) => {
        state.limits.push(action.payload);
      })
      // updateLimitDefinition
      .addCase(updateLimitDefinition.fulfilled, (state, action) => {
        const idx = state.limits.findIndex((l) => l.id === action.payload.id);
        if (idx !== -1) {
          state.limits[idx] = action.payload;
        }
      })
      // activateLimit
      .addCase(activateLimit.fulfilled, (state, action) => {
        const idx = state.limits.findIndex((l) => l.id === action.payload.id);
        if (idx !== -1) {
          state.limits[idx] = action.payload;
        }
      })
      // deactivateLimit
      .addCase(deactivateLimit.fulfilled, (state, action) => {
        const idx = state.limits.findIndex((l) => l.id === action.payload.id);
        if (idx !== -1) {
          state.limits[idx] = action.payload;
        }
      })
      // assignToProduct
      .addCase(assignToProduct.fulfilled, (state, action) => {
        state.productLimits.push(action.payload);
      })
      // assignToCustomer
      .addCase(assignToCustomer.fulfilled, (state, action) => {
        state.customerLimits.push(action.payload);
      })
      // assignToAccount
      .addCase(assignToAccount.fulfilled, (state, action) => {
        state.accountLimits.push(action.payload);
      })
      // checkLimit
      .addCase(checkLimit.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(checkLimit.fulfilled, (state, action) => {
        state.loading = false;
        state.lastCheckResult = action.payload;
      })
      .addCase(checkLimit.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to check limit';
      })
      // getEffectiveLimits
      .addCase(getEffectiveLimits.fulfilled, (state, action) => {
        state.effectiveLimits = action.payload;
      })
      // fetchPendingApprovals
      .addCase(fetchPendingApprovals.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchPendingApprovals.fulfilled, (state, action) => {
        state.loading = false;
        state.approvals = action.payload.content;
        state.pagination.totalElements = action.payload.totalElements;
        state.pagination.totalPages = action.payload.totalPages;
      })
      .addCase(fetchPendingApprovals.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch pending approvals';
      })
      // approveApproval
      .addCase(approveApproval.fulfilled, (state, action) => {
        state.approvals = state.approvals.filter((a) => a.id !== action.payload.id);
      })
      // rejectApproval
      .addCase(rejectApproval.fulfilled, (state, action) => {
        state.approvals = state.approvals.filter((a) => a.id !== action.payload.id);
      });
  },
});

export const { clearError, clearCheckResult } = limitSlice.actions;
export default limitSlice.reducer;

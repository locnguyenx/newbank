import { createSlice, createAsyncThunk, type PayloadAction } from '@reduxjs/toolkit';
import type { KYC, KYCDocumentType } from '@/types/kyc.types';
import { kycService } from '@/services/kycService';

interface KYCState {
  currentKYC: KYC | null;
  pendingReviews: KYC[];
  loading: boolean;
  error: string | null;
}

const initialState: KYCState = {
  currentKYC: null,
  pendingReviews: [],
  loading: false,
  error: null,
};

export const fetchKYC = createAsyncThunk<KYC, number>(
  'kyc/fetchByCustomerId',
  async (customerId) => {
    return kycService.getByCustomerId(customerId);
  }
);

export const fetchKYCById = createAsyncThunk<KYC, number>(
  'kyc/fetchById',
  async (kycId) => {
    return kycService.getById(kycId);
  }
);

export const submitDocuments = createAsyncThunk<KYC, { customerId: number; documents: Array<{ documentType: KYCDocumentType; file: File }> }>(
  'kyc/submitDocuments',
  async ({ customerId, documents }) => {
    return kycService.submitDocuments(customerId, documents);
  }
);

export const approveKYC = createAsyncThunk<KYC, { kycId: number; notes: string }>(
  'kyc/approve',
  async ({ kycId, notes }) => {
    return kycService.approve(kycId, notes);
  }
);

export const rejectKYC = createAsyncThunk<KYC, { kycId: number; notes: string }>(
  'kyc/reject',
  async ({ kycId, notes }) => {
    return kycService.reject(kycId, notes);
  }
);

export const fetchPendingReviews = createAsyncThunk<KYC[], void>(
  'kyc/fetchPendingReviews',
  async () => {
    return kycService.getPendingReviews();
  }
);

const kycSlice = createSlice({
  name: 'kyc',
  initialState,
  reducers: {
    setLoading: (state, action: PayloadAction<boolean>) => {
      state.loading = action.payload;
    },
    setError: (state, action: PayloadAction<string | null>) => {
      state.error = action.payload;
    },
    setCurrentKYC: (state, action: PayloadAction<KYC | null>) => {
      state.currentKYC = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchKYC.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchKYC.fulfilled, (state, action) => {
        state.loading = false;
        state.currentKYC = action.payload;
      })
      .addCase(fetchKYC.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch KYC';
      })
      .addCase(fetchKYCById.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchKYCById.fulfilled, (state, action) => {
        state.loading = false;
        state.currentKYC = action.payload;
      })
      .addCase(fetchKYCById.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch KYC';
      })
      .addCase(submitDocuments.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(submitDocuments.fulfilled, (state, action) => {
        state.loading = false;
        state.currentKYC = action.payload;
      })
      .addCase(submitDocuments.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to submit documents';
      })
      .addCase(approveKYC.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(approveKYC.fulfilled, (state, action) => {
        state.loading = false;
        state.currentKYC = action.payload;
        state.pendingReviews = state.pendingReviews.filter((k) => k.id !== action.payload.id);
      })
      .addCase(approveKYC.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to approve KYC';
      })
      .addCase(rejectKYC.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(rejectKYC.fulfilled, (state, action) => {
        state.loading = false;
        state.currentKYC = action.payload;
        state.pendingReviews = state.pendingReviews.filter((k) => k.id !== action.payload.id);
      })
      .addCase(rejectKYC.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to reject KYC';
      })
      .addCase(fetchPendingReviews.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchPendingReviews.fulfilled, (state, action) => {
        state.loading = false;
        state.pendingReviews = action.payload;
      })
      .addCase(fetchPendingReviews.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch pending reviews';
      });
  },
});

export const { setLoading, setError, setCurrentKYC } = kycSlice.actions;
export default kycSlice.reducer;

import { createSlice, createAsyncThunk, type PayloadAction } from '@reduxjs/toolkit';
import type { Employment, BulkUploadResult, CreateEmploymentPayload } from '@/types/employment.types';
import { employmentService } from '@/services/employmentService';

interface EmploymentState {
  employments: Employment[];
  loading: boolean;
  error: string | null;
  uploadProgress: number;
  uploadResult: BulkUploadResult | null;
  uploadLoading: boolean;
}

const initialState: EmploymentState = {
  employments: [],
  loading: false,
  error: null,
  uploadProgress: 0,
  uploadResult: null,
  uploadLoading: false,
};

export const fetchEmployments = createAsyncThunk<Employment[], number>(
  'employment/fetchByCustomer',
  async (customerId) => {
    return employmentService.getByCustomerId(customerId);
  }
);

export const createEmployment = createAsyncThunk<Employment, CreateEmploymentPayload>(
  'employment/create',
  async (data) => {
    return employmentService.create(data);
  }
);

interface BulkUploadParams {
  customerId: number;
  file: File;
  onProgress?: (percent: number) => void;
}

export const bulkUpload = createAsyncThunk<BulkUploadResult, BulkUploadParams>(
  'employment/bulkUpload',
  async ({ customerId, file, onProgress }) => {
    return employmentService.bulkUpload(customerId, file, onProgress);
  }
);

const employmentSlice = createSlice({
  name: 'employment',
  initialState,
  reducers: {
    setUploadProgress: (state, action: PayloadAction<number>) => {
      state.uploadProgress = action.payload;
    },
    clearUploadResult: (state) => {
      state.uploadResult = null;
      state.uploadProgress = 0;
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchEmployments.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchEmployments.fulfilled, (state, action) => {
        state.loading = false;
        state.employments = action.payload;
      })
      .addCase(fetchEmployments.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch employees';
      })
      .addCase(createEmployment.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(createEmployment.fulfilled, (state, action) => {
        state.loading = false;
        state.employments.push(action.payload);
      })
      .addCase(createEmployment.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to create employee';
      })
      .addCase(bulkUpload.pending, (state) => {
        state.uploadLoading = true;
        state.uploadResult = null;
        state.error = null;
        state.uploadProgress = 0;
      })
      .addCase(bulkUpload.fulfilled, (state, action) => {
        state.uploadLoading = false;
        state.uploadResult = action.payload;
        state.uploadProgress = 100;
      })
      .addCase(bulkUpload.rejected, (state, action) => {
        state.uploadLoading = false;
        state.error = action.error.message || 'Bulk upload failed';
        state.uploadProgress = 0;
      });
  },
});

export const { setUploadProgress, clearUploadResult } = employmentSlice.actions;
export default employmentSlice.reducer;

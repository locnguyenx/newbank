import { createSlice, createAsyncThunk, type PayloadAction } from '@reduxjs/toolkit';
import type { Authorization, CreateAuthorizationRequest, UpdateAuthorizationRequest, AuthorizationDocumentType } from '@/types/authorization.types';
import { authorizationService } from '@/services/authorizationService';

interface AuthorizationState {
  authorizations: Authorization[];
  currentAuthorization: Authorization | null;
  loading: boolean;
  error: string | null;
}

const initialState: AuthorizationState = {
  authorizations: [],
  currentAuthorization: null,
  loading: false,
  error: null,
};

export const fetchAuthorizations = createAsyncThunk<Authorization[], { customerId: number; status?: string }>(
  'authorizations/fetchByCustomerId',
  async ({ customerId, status }) => {
    return authorizationService.getByCustomerId(customerId, status);
  }
);

export const fetchAuthorizationById = createAsyncThunk<Authorization, number>(
  'authorizations/fetchById',
  async (authorizationId) => {
    return authorizationService.getById(authorizationId);
  }
);

export const createAuthorization = createAsyncThunk<Authorization, CreateAuthorizationRequest>(
  'authorizations/create',
  async (request) => {
    return authorizationService.create(request);
  }
);

export const updateAuthorization = createAsyncThunk<Authorization, UpdateAuthorizationRequest>(
  'authorizations/update',
  async (request) => {
    return authorizationService.update(request);
  }
);

export const revokeAuthorization = createAsyncThunk<Authorization, { id: number; reason: string }>(
  'authorizations/revoke',
  async ({ id, reason }) => {
    return authorizationService.revoke(id, reason);
  }
);

export const uploadAuthorizationDocuments = createAsyncThunk<Authorization, { authorizationId: number; documents: Array<{ documentType: AuthorizationDocumentType; file: File }> }>(
  'authorizations/uploadDocuments',
  async ({ authorizationId, documents }) => {
    return authorizationService.uploadDocuments(authorizationId, documents);
  }
);

const authorizationSlice = createSlice({
  name: 'authorizations',
  initialState,
  reducers: {
    setLoading: (state, action: PayloadAction<boolean>) => {
      state.loading = action.payload;
    },
    setError: (state, action: PayloadAction<string | null>) => {
      state.error = action.payload;
    },
    setCurrentAuthorization: (state, action: PayloadAction<Authorization | null>) => {
      state.currentAuthorization = action.payload;
    },
    clearError: (state) => {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchAuthorizations.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchAuthorizations.fulfilled, (state, action) => {
        state.loading = false;
        state.authorizations = action.payload;
      })
      .addCase(fetchAuthorizations.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch authorizations';
      })
      .addCase(fetchAuthorizationById.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchAuthorizationById.fulfilled, (state, action) => {
        state.loading = false;
        state.currentAuthorization = action.payload;
      })
      .addCase(fetchAuthorizationById.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch authorization';
      })
      .addCase(createAuthorization.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(createAuthorization.fulfilled, (state, action) => {
        state.loading = false;
        state.authorizations.push(action.payload);
        state.currentAuthorization = action.payload;
      })
      .addCase(createAuthorization.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to create authorization';
      })
      .addCase(updateAuthorization.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(updateAuthorization.fulfilled, (state, action) => {
        state.loading = false;
        state.authorizations = state.authorizations.map((a) =>
          a.id === action.payload.id ? action.payload : a
        );
        state.currentAuthorization = action.payload;
      })
      .addCase(updateAuthorization.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to update authorization';
      })
      .addCase(revokeAuthorization.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(revokeAuthorization.fulfilled, (state, action) => {
        state.loading = false;
        state.authorizations = state.authorizations.map((a) =>
          a.id === action.payload.id ? action.payload : a
        );
        state.currentAuthorization = action.payload;
      })
      .addCase(revokeAuthorization.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to revoke authorization';
      })
      .addCase(uploadAuthorizationDocuments.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(uploadAuthorizationDocuments.fulfilled, (state, action) => {
        state.loading = false;
        state.authorizations = state.authorizations.map((a) =>
          a.id === action.payload.id ? action.payload : a
        );
        state.currentAuthorization = action.payload;
      })
      .addCase(uploadAuthorizationDocuments.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to upload documents';
      });
  },
});

export const { setLoading, setError, setCurrentAuthorization, clearError } = authorizationSlice.actions;
export default authorizationSlice.reducer;

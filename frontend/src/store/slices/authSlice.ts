import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import type { AuthState, LoginRequest, TokenResponse, RefreshTokenRequest, MfaEnrollResponse, MfaVerifyRequest, User } from '@/types/auth.types';
import { authService } from '@/services/authService';

const initialState: AuthState = {
  user: null,
  accessToken: localStorage.getItem('accessToken'),
  refreshToken: localStorage.getItem('refreshToken'),
  isAuthenticated: !!localStorage.getItem('accessToken'),
  mfaRequired: false,
  mfaToken: null,
  loading: false,
  error: null,
};

export const login = createAsyncThunk<TokenResponse, LoginRequest>(
  'auth/login',
  async (request, { rejectWithValue }) => {
    try {
      const response = await authService.login(request);
      if (!response.mfaRequired) {
        localStorage.setItem('accessToken', response.accessToken);
        localStorage.setItem('refreshToken', response.refreshToken);
      }
      return response;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Login failed');
    }
  }
);

export const refreshToken = createAsyncThunk<TokenResponse, RefreshTokenRequest>(
  'auth/refresh',
  async (request, { rejectWithValue }) => {
    try {
      const response = await authService.refresh(request);
      localStorage.setItem('accessToken', response.accessToken);
      localStorage.setItem('refreshToken', response.refreshToken);
      return response;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Token refresh failed');
    }
  }
);

export const logout = createAsyncThunk(
  'auth/logout',
  async (_, { rejectWithValue }) => {
    try {
      await authService.logout();
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
    } catch (error: any) {
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      return rejectWithValue(error.response?.data?.message || 'Logout failed');
    }
  }
);

export const enrollMfa = createAsyncThunk<MfaEnrollResponse>(
  'auth/enrollMfa',
  async (_, { rejectWithValue }) => {
    try {
      return await authService.enrollMfa();
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'MFA enrollment failed');
    }
  }
);

export const verifyMfa = createAsyncThunk<TokenResponse, MfaVerifyRequest>(
  'auth/verifyMfa',
  async (request, { rejectWithValue }) => {
    try {
      const response = await authService.verifyMfa(request);
      localStorage.setItem('accessToken', response.accessToken);
      localStorage.setItem('refreshToken', response.refreshToken);
      return response;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'MFA verification failed');
    }
  }
);

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    setUser: (state, action: PayloadAction<User>) => {
      state.user = action.payload;
    },
    clearError: (state) => {
      state.error = null;
    },
    setMfaRequired: (state, action: PayloadAction<boolean>) => {
      state.mfaRequired = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(login.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(login.fulfilled, (state, action) => {
        state.loading = false;
        if (action.payload.mfaRequired) {
          state.mfaRequired = true;
          state.mfaToken = action.payload.mfaToken || null;
        } else {
          state.accessToken = action.payload.accessToken;
          state.refreshToken = action.payload.refreshToken;
          state.isAuthenticated = true;
          state.mfaRequired = false;
        }
      })
      .addCase(login.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      .addCase(refreshToken.fulfilled, (state, action) => {
        state.accessToken = action.payload.accessToken;
        state.refreshToken = action.payload.refreshToken;
      })
      .addCase(refreshToken.rejected, (state) => {
        state.isAuthenticated = false;
        state.accessToken = null;
        state.refreshToken = null;
      })
      .addCase(logout.fulfilled, (state) => {
        state.isAuthenticated = false;
        state.accessToken = null;
        state.refreshToken = null;
        state.user = null;
        state.mfaRequired = false;
        state.mfaToken = null;
      })
      .addCase(enrollMfa.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(enrollMfa.fulfilled, (state) => {
        state.loading = false;
      })
      .addCase(enrollMfa.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      .addCase(verifyMfa.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(verifyMfa.fulfilled, (state, action) => {
        state.loading = false;
        state.accessToken = action.payload.accessToken;
        state.refreshToken = action.payload.refreshToken;
        state.isAuthenticated = true;
        state.mfaRequired = false;
        state.mfaToken = null;
      })
      .addCase(verifyMfa.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });
  },
});

export const { setUser, clearError, setMfaRequired } = authSlice.actions;
export default authSlice.reducer;

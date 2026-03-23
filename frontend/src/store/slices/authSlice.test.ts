import { describe, it, expect, vi, beforeEach } from 'vitest';
import authReducer, { 
  login, 
  refreshToken, 
  logout, 
  enrollMfa, 
  verifyMfa,
  setUser,
  clearError,
  setMfaRequired
} from './authSlice';
import type { AuthState, LoginRequest, TokenResponse, RefreshTokenRequest, MfaEnrollResponse, MfaVerifyRequest } from '@/types/auth.types';

const initialState: AuthState = {
  user: null,
  accessToken: null,
  refreshToken: null,
  isAuthenticated: false,
  mfaRequired: false,
  mfaToken: null,
  loading: false,
  error: null,
};

describe('authSlice', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    localStorage.clear();
  });

  describe('reducers', () => {
    it('should handle initial state', () => {
      expect(authReducer(undefined, { type: 'unknown' })).toEqual(initialState);
    });

    it('should handle setUser', () => {
      const user = { id: 1, email: 'test@example.com', firstName: 'Test', lastName: 'User', roles: ['USER'], mfaEnabled: false };
      const state = authReducer(initialState, setUser(user));
      expect(state.user).toEqual(user);
    });

    it('should handle clearError', () => {
      const stateWithError = { ...initialState, error: 'Some error' };
      const state = authReducer(stateWithError, clearError());
      expect(state.error).toBeNull();
    });

    it('should handle setMfaRequired', () => {
      const state = authReducer(initialState, setMfaRequired(true));
      expect(state.mfaRequired).toBe(true);
    });
  });

  describe('login', () => {
    it('should handle login pending', () => {
      const action = { type: login.pending.type };
      const state = authReducer(initialState, action);
      expect(state.loading).toBe(true);
      expect(state.error).toBeNull();
    });

    it('should handle login fulfilled without MFA', () => {
      const payload: TokenResponse = {
        accessToken: 'access-token',
        refreshToken: 'refresh-token',
        tokenType: 'Bearer',
        expiresIn: 3600,
      };
      const action = { type: login.fulfilled.type, payload };
      const state = authReducer(initialState, action);
      expect(state.loading).toBe(false);
      expect(state.accessToken).toBe('access-token');
      expect(state.refreshToken).toBe('refresh-token');
      expect(state.isAuthenticated).toBe(true);
      expect(state.mfaRequired).toBe(false);
    });

    it('should handle login fulfilled with MFA required', () => {
      const payload: TokenResponse = {
        accessToken: 'access-token',
        refreshToken: 'refresh-token',
        tokenType: 'Bearer',
        expiresIn: 3600,
        mfaRequired: true,
        mfaToken: 'mfa-token',
      };
      const action = { type: login.fulfilled.type, payload };
      const state = authReducer(initialState, action);
      expect(state.loading).toBe(false);
      expect(state.accessToken).toBeNull(); // Not set when MFA required
      expect(state.refreshToken).toBeNull(); // Not set when MFA required
      expect(state.isAuthenticated).toBe(false); // Not set when MFA required
      expect(state.mfaRequired).toBe(true);
      expect(state.mfaToken).toBe('mfa-token');
    });

    it('should handle login rejected', () => {
      const action = { type: login.rejected.type, payload: 'Invalid credentials' };
      const state = authReducer(initialState, action);
      expect(state.loading).toBe(false);
      expect(state.error).toBe('Invalid credentials');
    });
  });

  describe('refreshToken', () => {
    it('should handle refreshToken fulfilled', () => {
      const payload: TokenResponse = {
        accessToken: 'new-access-token',
        refreshToken: 'new-refresh-token',
        tokenType: 'Bearer',
        expiresIn: 3600,
      };
      const stateWithToken = { ...initialState, accessToken: 'old-token', refreshToken: 'old-refresh' };
      const action = { type: refreshToken.fulfilled.type, payload };
      const state = authReducer(stateWithToken, action);
      expect(state.accessToken).toBe('new-access-token');
      expect(state.refreshToken).toBe('new-refresh-token');
    });

    it('should handle refreshToken rejected', () => {
      const action = { type: refreshToken.rejected.type };
      const state = authReducer(initialState, action);
      expect(state.isAuthenticated).toBe(false);
      expect(state.accessToken).toBeNull();
      expect(state.refreshToken).toBeNull();
    });
  });

  describe('logout', () => {
    it('should handle logout fulfilled', () => {
      const stateWithTokens = { 
        ...initialState, 
        isAuthenticated: true, 
        accessToken: 'token', 
        refreshToken: 'refresh',
        user: { id: 1, email: 'test@example.com', firstName: 'Test', lastName: 'User', roles: ['USER'], mfaEnabled: false }
      };
      const action = { type: logout.fulfilled.type };
      const state = authReducer(stateWithTokens, action);
      expect(state.isAuthenticated).toBe(false);
      expect(state.accessToken).toBeNull();
      expect(state.refreshToken).toBeNull();
      expect(state.user).toBeNull();
      expect(state.mfaRequired).toBe(false);
      expect(state.mfaToken).toBeNull();
    });
  });

  describe('enrollMfa', () => {
    it('should handle enrollMfa pending', () => {
      const action = { type: enrollMfa.pending.type };
      const state = authReducer(initialState, action);
      expect(state.loading).toBe(true);
      expect(state.error).toBeNull();
    });

    it('should handle enrollMfa fulfilled', () => {
      const action = { type: enrollMfa.fulfilled.type };
      const state = authReducer(initialState, action);
      expect(state.loading).toBe(false);
    });

    it('should handle enrollMfa rejected', () => {
      const action = { type: enrollMfa.rejected.type, payload: 'Enrollment failed' };
      const state = authReducer(initialState, action);
      expect(state.loading).toBe(false);
      expect(state.error).toBe('Enrollment failed');
    });
  });

  describe('verifyMfa', () => {
    it('should handle verifyMfa pending', () => {
      const action = { type: verifyMfa.pending.type };
      const state = authReducer(initialState, action);
      expect(state.loading).toBe(true);
      expect(state.error).toBeNull();
    });

    it('should handle verifyMfa fulfilled', () => {
      const payload: TokenResponse = {
        accessToken: 'access-token',
        refreshToken: 'refresh-token',
        tokenType: 'Bearer',
        expiresIn: 3600,
      };
      const stateWithMfa = { 
        ...initialState, 
        mfaRequired: true, 
        mfaToken: 'temp-token' 
      };
      const action = { type: verifyMfa.fulfilled.type, payload };
      const state = authReducer(stateWithMfa, action);
      expect(state.loading).toBe(false);
      expect(state.accessToken).toBe('access-token');
      expect(state.refreshToken).toBe('refresh-token');
      expect(state.isAuthenticated).toBe(true);
      expect(state.mfaRequired).toBe(false);
      expect(state.mfaToken).toBeNull();
    });

    it('should handle verifyMfa rejected', () => {
      const action = { type: verifyMfa.rejected.type, payload: 'Invalid code' };
      const state = authReducer(initialState, action);
      expect(state.loading).toBe(false);
      expect(state.error).toBe('Invalid code');
    });
  });
});
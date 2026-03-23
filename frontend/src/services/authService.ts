import axios from 'axios';
import type { LoginRequest, TokenResponse, RefreshTokenRequest, MfaEnrollResponse, MfaVerifyRequest } from '@/types/auth.types';

const API_BASE = '/api/auth';

const authAxios = axios.create({
  baseURL: API_BASE,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const authService = {
  login: async (request: LoginRequest): Promise<TokenResponse> => {
    const response = await authAxios.post('/login', request);
    return response.data;
  },

  refresh: async (request: RefreshTokenRequest): Promise<TokenResponse> => {
    const response = await authAxios.post('/refresh', request);
    return response.data;
  },

  logout: async (): Promise<void> => {
    await authAxios.post('/logout');
  },

  enrollMfa: async (): Promise<MfaEnrollResponse> => {
    const response = await authAxios.post('/mfa/enroll');
    return response.data;
  },

  verifyMfa: async (request: MfaVerifyRequest): Promise<TokenResponse> => {
    const response = await authAxios.post('/mfa/verify', request);
    return response.data;
  },
};

export interface LoginRequest {
  email: string;
  password: string;
}

export interface TokenResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  expiresIn: number;
  mfaRequired?: boolean;
  mfaToken?: string;
}

export interface RefreshTokenRequest {
  refreshToken: string;
}

export interface MfaEnrollResponse {
  secret: string;
  qrCodeUrl: string;
}

export interface MfaVerifyRequest {
  userId: number;
  code: string;
}

export interface User {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  roles: string[];
  mfaEnabled: boolean;
}

export interface AuthState {
  user: User | null;
  accessToken: string | null;
  refreshToken: string | null;
  isAuthenticated: boolean;
  mfaRequired: boolean;
  mfaToken: string | null;
  loading: boolean;
  error: string | null;
}

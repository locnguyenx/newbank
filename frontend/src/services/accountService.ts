import apiClient from './apiClient';
import type { AccountResponse, AccountDetails } from '@/types/account.types';

interface PaginationParams {
  search?: string;
  type?: string;
  status?: string;
  page?: number;
  size?: number;
}

interface PaginatedResponse {
  content: AccountResponse[];
  totalElements: number;
  totalPages: number;
}

export const accountService = {
  getAll: async (params?: PaginationParams): Promise<PaginatedResponse> => {
    const response = await apiClient.get('/accounts', { params });
    return response.data;
  },

  getByAccountNumber: async (accountNumber: string): Promise<AccountDetails> => {
    const response = await apiClient.get(`/accounts/${accountNumber}`);
    return response.data;
  },

  getBalance: async (accountNumber: string): Promise<{ availableBalance: string; ledgerBalance: string; holdAmount: string; currency: string }> => {
    const response = await apiClient.get(`/accounts/${accountNumber}/balance`);
    return response.data;
  },

  getStatement: async (accountNumber: string, fromDate: string, toDate: string) => {
    const response = await apiClient.get(`/accounts/${accountNumber}/statement`, {
      params: { fromDate, toDate }
    });
    return response.data;
  },

  openAccount: async (data: {
    customerId: number;
    productCode: string;
    type: string;
    currency: string;
    initialDeposit: number;
    holders: { customerId: number; role: string }[];
  }): Promise<AccountResponse> => {
    const response = await apiClient.post('/accounts', data);
    return response.data;
  },

  closeAccount: async (accountNumber: string): Promise<void> => {
    await apiClient.put(`/accounts/${accountNumber}/close`);
  },

  freezeAccount: async (accountNumber: string): Promise<void> => {
    await apiClient.put(`/accounts/${accountNumber}/freeze`);
  },

  unfreezeAccount: async (accountNumber: string): Promise<void> => {
    await apiClient.put(`/accounts/${accountNumber}/unfreeze`);
  },

  addHolder: async (accountNumber: string, holderData: { customerId: number; role: string }): Promise<void> => {
    await apiClient.post(`/accounts/${accountNumber}/holders`, holderData);
  },

  removeHolder: async (accountNumber: string, holderId: number): Promise<void> => {
    await apiClient.delete(`/accounts/${accountNumber}/holders/${holderId}`);
  }
};

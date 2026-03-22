import { Configuration } from '@/api/configuration';
import { AccountControllerApi, AccountHolderControllerApi } from '@/api/api';
import type { AccountResponse, AccountDetails } from '@/types/account.types';
import axios from 'axios';

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

const createAuthAxios = () => {
  const instance = axios.create();
  instance.interceptors.request.use((config: any) => {
    if (!config.headers['X-Username']) {
      config.headers = config.headers || {};
      config.headers['X-Username'] = localStorage.getItem('username') || 'system';
    }
    return config;
  });
  return instance;
};

const config = new Configuration();
config.baseOptions = { axios: createAuthAxios() };
config.basePath = '';  // Use relative paths to go through Vite proxy

const accountAPI = new AccountControllerApi(config);
const accountHolderAPI = new AccountHolderControllerApi(config);

export const accountService = {
  getAll: async (params?: PaginationParams): Promise<PaginatedResponse> => {
    const response = await accountAPI.getAccounts(
      params?.search,
      params?.type as any,
      params?.status as any,
      undefined, // customerId - not used in this call
      params?.page ?? 0,
      params?.size ?? 20
    );
    const data = response.data as any;
    return { content: data.content || data, totalElements: data.totalElements, totalPages: data.totalPages } as PaginatedResponse;
  },

  getByAccountNumber: async (accountNumber: string): Promise<AccountDetails> => {
    const response = await accountAPI.getAccountDetails(accountNumber);
    return response.data as AccountDetails;
  },

  getBalance: async (accountNumber: string): Promise<{ availableBalance: string; ledgerBalance: string; holdAmount: string; currency: string }> => {
    const response = await accountAPI.getAccountBalance(accountNumber);
    return response.data as any;
  },

  getStatement: async (accountNumber: string, fromDate: string, toDate: string) => {
    const response = await accountAPI.getAccountStatement(accountNumber);
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
    const response = await accountAPI.openAccount({
      customerId: data.customerId,
      productCode: data.productCode,
      type: data.type as any,
      currency: data.currency,
      initialDeposit: data.initialDeposit,
      holders: data.holders as any,
    });
    return response.data as AccountResponse;
  },

  closeAccount: async (accountNumber: string): Promise<void> => {
    await accountAPI.closeAccount(accountNumber).then(() => undefined);
  },

  freezeAccount: async (accountNumber: string): Promise<void> => {
    await accountAPI.freezeAccount(accountNumber).then(() => undefined);
  },

  unfreezeAccount: async (accountNumber: string): Promise<void> => {
    await accountAPI.unfreezeAccount(accountNumber).then(() => undefined);
  },

  addHolder: async (accountNumber: string, data: { customerId: number; role: string }): Promise<void> => {
    await accountHolderAPI.addHolder(accountNumber, {
      customerId: data.customerId,
      role: data.role as any,
    }).then(() => undefined);
  },

  removeHolder: async (accountNumber: string, holderId: number): Promise<void> => {
    await accountHolderAPI.removeHolder(accountNumber, holderId).then(() => undefined);
  },
};

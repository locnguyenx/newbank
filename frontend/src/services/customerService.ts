import apiClient from './apiClient';
import type { CustomerVariant } from '@/types';

export const customerService = {
  getAll: async (): Promise<CustomerVariant[]> => {
    const response = await apiClient.get('/customers');
    return response.data;
  },

  getById: async (id: number): Promise<CustomerVariant> => {
    const response = await apiClient.get(`/customers/${id}`);
    return response.data;
  },
};

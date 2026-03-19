import apiClient from './apiClient';
import type { CustomerVariant } from '@/types/customer.types';

export const customerService = {
  getAll: async (): Promise<CustomerVariant[]> => {
    const response = await apiClient.get('/customers');
    return response.data;
  },

  getById: async (id: number): Promise<CustomerVariant> => {
    const response = await apiClient.get(`/customers/${id}`);
    return response.data;
  },

  create: async (data: Record<string, unknown>): Promise<CustomerVariant> => {
    const response = await apiClient.post('/customers', data);
    return response.data;
  },

  update: async (id: number, data: Record<string, unknown>): Promise<CustomerVariant> => {
    const response = await apiClient.put(`/customers/${id}`, data);
    return response.data;
  },
};

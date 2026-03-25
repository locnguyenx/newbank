import apiClient from './apiClient';

export interface Threshold {
  id: number;
  role: string;
  amount: number;
  currency: string;
  type: string;
}

export interface CreateThresholdRequest {
  role: string;
  amount: number;
  currency: string;
  type: string;
}

export const thresholdService = {
  getThresholds: async () => {
    const response = await apiClient.get('/iam/thresholds');
    return response.data;
  },

  getThreshold: async (id: number) => {
    const response = await apiClient.get(`/iam/thresholds/${id}`);
    return response.data;
  },

  createThreshold: async (data: CreateThresholdRequest) => {
    const response = await apiClient.post('/iam/thresholds', data);
    return response.data;
  },

  updateThreshold: async (id: number, amount: number) => {
    const response = await apiClient.put(`/iam/thresholds/${id}`, null, { params: { threshold: amount } });
    return response.data;
  },

  deleteThreshold: async (id: number) => {
    const response = await apiClient.delete(`/iam/thresholds/${id}`);
    return response.data;
  },
};

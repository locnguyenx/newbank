import apiClient from './apiClient';
import type { Employment, BulkUploadResult, CreateEmploymentPayload } from '@/types/employment.types';

export const employmentService = {
  getByCustomerId: async (customerId: number): Promise<Employment[]> => {
    const response = await apiClient.get(`/customers/${customerId}/employees`);
    return response.data;
  },

  create: async (data: CreateEmploymentPayload): Promise<Employment> => {
    const response = await apiClient.post(`/customers/${(data as any).customerId || (data as any).employerId}/employees`, data);
    return response.data;
  },

  bulkUpload: async (customerId: number, file: File, onProgress?: (percent: number) => void): Promise<BulkUploadResult> => {
    const formData = new FormData();
    formData.append('file', file);

    const response = await apiClient.post(`/customers/${customerId}/employees/bulk`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
      onUploadProgress: (event) => {
        if (onProgress && event.total) {
          onProgress(Math.round((event.loaded * 100) / event.total));
        }
      },
    });
    return response.data;
  },
};

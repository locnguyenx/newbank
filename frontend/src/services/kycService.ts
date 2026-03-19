import apiClient from './apiClient';
import type { KYC, KYCDocumentType } from '@/types/kyc.types';

export const kycService = {
  getByCustomerId: async (customerId: number): Promise<KYC> => {
    const response = await apiClient.get(`/customers/${customerId}/kyc`);
    return response.data;
  },

  getById: async (id: number): Promise<KYC> => {
    const response = await apiClient.get(`/kyc/${id}`);
    return response.data;
  },

  submitDocuments: async (customerId: number, documents: Array<{ documentType: KYCDocumentType; file: File }>): Promise<KYC> => {
    const formData = new FormData();
    documents.forEach((doc, index) => {
      formData.append(`documents[${index}].documentType`, doc.documentType);
      formData.append(`documents[${index}].file`, doc.file);
    });

    const response = await apiClient.post(`/customers/${customerId}/kyc/documents`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    return response.data;
  },

  approve: async (kycId: number, notes: string): Promise<KYC> => {
    const response = await apiClient.post(`/kyc/${kycId}/approve`, { notes });
    return response.data;
  },

  reject: async (kycId: number, notes: string): Promise<KYC> => {
    const response = await apiClient.post(`/kyc/${kycId}/reject`, { notes });
    return response.data;
  },

  getPendingReviews: async (): Promise<KYC[]> => {
    const response = await apiClient.get('/kyc/pending');
    return response.data;
  },
};

import apiClient from './apiClient';
import type { Authorization, CreateAuthorizationRequest, UpdateAuthorizationRequest, AuthorizationDocumentType } from '@/types/authorization.types';

export const authorizationService = {
  getByCustomerId: async (customerId: number, status?: string): Promise<Authorization[]> => {
    const params = status ? { status } : {};
    const response = await apiClient.get(`/customers/${customerId}/authorizations`, { params });
    return response.data;
  },

  getById: async (id: number): Promise<Authorization> => {
    const response = await apiClient.get(`/authorizations/${id}`);
    return response.data;
  },

  create: async (request: CreateAuthorizationRequest): Promise<Authorization> => {
    const response = await apiClient.post('/authorizations', request);
    return response.data;
  },

  update: async (id: number, request: Partial<UpdateAuthorizationRequest>): Promise<Authorization> => {
    const response = await apiClient.put(`/authorizations/${id}`, request);
    return response.data;
  },

  revoke: async (id: number, reason: string): Promise<Authorization> => {
    const response = await apiClient.post(`/authorizations/${id}/revoke`, { reason });
    return response.data;
  },

  uploadDocuments: async (authorizationId: number, documents: Array<{ documentType: AuthorizationDocumentType; file: File }>): Promise<Authorization> => {
    const formData = new FormData();
    documents.forEach((doc, index) => {
      formData.append(`documents[${index}].documentType`, doc.documentType);
      formData.append(`documents[${index}].file`, doc.file);
    });

    const response = await apiClient.post(`/authorizations/${authorizationId}/documents`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    return response.data;
  },
};

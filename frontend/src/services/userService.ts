import apiClient from './apiClient';

export interface User {
  id: number;
  email: string;
  userType: string;
  status: string;
  mfaEnabled: boolean;
  companyId?: number;
  branchId?: number;
  departmentId?: number;
  createdAt: string;
}

export interface CreateUserRequest {
  email: string;
  password: string;
  userType: string;
  role?: string;
  companyId?: number;
  branchId?: number;
  departmentId?: number;
}

export const userService = {
  getUsers: async (params?: Record<string, unknown>) => {
    const response = await apiClient.get('/iam/users', { params });
    return response.data;
  },

  getUser: async (id: number) => {
    const response = await apiClient.get(`/iam/users/${id}`);
    return response.data;
  },

  createUser: async (data: CreateUserRequest) => {
    const response = await apiClient.post('/iam/users', data);
    return response.data;
  },

  updateUser: async (id: number, data: Partial<CreateUserRequest>) => {
    const response = await apiClient.put(`/iam/users/${id}`, data);
    return response.data;
  },

  activateUser: async (id: number) => {
    const response = await apiClient.post(`/iam/users/${id}/activate`);
    return response.data;
  },

  deactivateUser: async (id: number) => {
    const response = await apiClient.post(`/iam/users/${id}/deactivate`);
    return response.data;
  },

  getActiveUsers: async () => {
    const response = await apiClient.get('/iam/users/active');
    return response.data;
  },
};

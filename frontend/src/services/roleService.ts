import apiClient from './apiClient';

export interface Permission {
  id: number;
  name: string;
  description: string;
}

export interface Role {
  id: number;
  name: string;
  description: string;
  type: 'SYSTEM' | 'CUSTOM';
  permissions: string[];
}

export interface CreateRoleRequest {
  name: string;
  description: string;
  permissions?: string[];
}

export const roleService = {
  getRoles: async () => {
    const response = await apiClient.get('/iam/roles');
    return response.data;
  },

  getRole: async (id: number) => {
    const response = await apiClient.get(`/iam/roles/${id}`);
    return response.data;
  },

  createRole: async (data: CreateRoleRequest) => {
    const response = await apiClient.post('/iam/roles', data);
    return response.data;
  },

  updateRole: async (id: number, data: CreateRoleRequest) => {
    const response = await apiClient.put(`/iam/roles/${id}`, data);
    return response.data;
  },

  deleteRole: async (id: number) => {
    const response = await apiClient.delete(`/iam/roles/${id}`);
    return response.data;
  },

  assignPermissions: async (id: number, permissions: string[]) => {
    const response = await apiClient.put(`/iam/roles/${id}/permissions`, permissions);
    return response.data;
  },

  getPermissions: async () => {
    const response = await apiClient.get('/iam/permissions');
    return response.data;
  },
};

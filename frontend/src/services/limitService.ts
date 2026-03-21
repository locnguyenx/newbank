import apiClient from './apiClient';
import type {
  LimitDefinition,
  ProductLimit,
  CustomerLimit,
  AccountLimit,
  ApprovalRequest,
  LimitCheckRequest,
  LimitCheckResponse,
  EffectiveLimitResponse,
  CreateLimitDefinitionRequest,
  AssignLimitRequest,
  ApprovalActionRequest,
  PaginatedResponse,
  LimitStatus,
} from '@/types/limit.types';

export const limitService = {
  getLimitDefinitions(status?: LimitStatus): Promise<LimitDefinition[]> {
    return apiClient.get('/limits/definitions', { params: { status } }).then((res) => res.data);
  },

  getLimitDefinitionById(id: number): Promise<LimitDefinition> {
    return apiClient.get(`/limits/definitions/${id}`).then((res) => res.data);
  },

  createLimitDefinition(data: CreateLimitDefinitionRequest): Promise<LimitDefinition> {
    return apiClient.post('/limits/definitions', data).then((res) => res.data);
  },

  updateLimitDefinition(id: number, data: CreateLimitDefinitionRequest): Promise<LimitDefinition> {
    return apiClient.put(`/limits/definitions/${id}`, data).then((res) => res.data);
  },

  activateLimit(id: number): Promise<LimitDefinition> {
    return apiClient.put(`/limits/definitions/${id}/activate`).then((res) => res.data);
  },

  deactivateLimit(id: number): Promise<LimitDefinition> {
    return apiClient.put(`/limits/definitions/${id}/deactivate`).then((res) => res.data);
  },

  assignToProduct(data: AssignLimitRequest): Promise<ProductLimit> {
    return apiClient.post('/limits/assignments/product', data).then((res) => res.data);
  },

  assignToCustomer(data: AssignLimitRequest): Promise<CustomerLimit> {
    return apiClient.post('/limits/assignments/customer', data).then((res) => res.data);
  },

  assignToAccount(data: AssignLimitRequest): Promise<AccountLimit> {
    return apiClient.post('/limits/assignments/account', data).then((res) => res.data);
  },

  getProductLimits(productCode: string): Promise<ProductLimit[]> {
    return apiClient.get(`/limits/assignments/product/${productCode}`).then((res) => res.data);
  },

  getCustomerLimits(customerId: number): Promise<CustomerLimit[]> {
    return apiClient.get(`/limits/assignments/customer/${customerId}`).then((res) => res.data);
  },

  getAccountLimits(accountNumber: string): Promise<AccountLimit[]> {
    return apiClient.get(`/limits/assignments/account/${accountNumber}`).then((res) => res.data);
  },

  checkLimit(data: LimitCheckRequest): Promise<LimitCheckResponse> {
    return apiClient.post('/limits/check', data).then((res) => res.data);
  },

  getEffectiveLimits(accountNumber: string, customerId?: number, productCode?: string, currency?: string): Promise<EffectiveLimitResponse[]> {
    return apiClient.get('/limits/check/effective', {
      params: { accountNumber, customerId, productCode, currency },
    }).then((res) => res.data);
  },

  getPendingApprovals(page?: number, size?: number): Promise<PaginatedResponse<ApprovalRequest>> {
    return apiClient.get('/limits/approvals/pending', { params: { page, size } }).then((res) => res.data);
  },

  approveApproval(id: number): Promise<ApprovalRequest> {
    return apiClient.post(`/limits/approvals/${id}/approve`).then((res) => res.data);
  },

  rejectApproval(id: number, reason?: string): Promise<ApprovalRequest> {
    return apiClient.post(`/limits/approvals/${id}/reject`, { reason } as ApprovalActionRequest).then((res) => res.data);
  },
};

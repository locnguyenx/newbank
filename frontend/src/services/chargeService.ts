import apiClient from './apiClient';
import type {
  ChargeDefinition,
  ChargeRule,
  ProductCharge,
  CustomerChargeOverride,
  FeeWaiver,
  InterestRate,
  ChargeCalculationRequest,
  ChargeCalculationResponse,
  CreateChargeDefinitionRequest,
  CreateChargeRuleRequest,
  CreateFeeWaiverRequest,
  CreateInterestRateRequest,
} from '@/types/charge.types';

export const chargeService = {
  getChargeDefinitions(params?: { chargeType?: string; status?: string }): Promise<ChargeDefinition[]> {
    return apiClient.get('/charges/definitions', { params }).then((res) => res.data);
  },

  getChargeDefinitionById(id: number): Promise<ChargeDefinition> {
    return apiClient.get(`/charges/definitions/${id}`).then((res) => res.data);
  },

  createChargeDefinition(data: CreateChargeDefinitionRequest): Promise<ChargeDefinition> {
    return apiClient.post('/charges/definitions', data).then((res) => res.data);
  },

  activateCharge(id: number): Promise<ChargeDefinition> {
    return apiClient.put(`/charges/definitions/${id}/activate`).then((res) => res.data);
  },

  deactivateCharge(id: number): Promise<ChargeDefinition> {
    return apiClient.put(`/charges/definitions/${id}/deactivate`).then((res) => res.data);
  },

  getChargeRules(chargeId: number): Promise<ChargeRule[]> {
    return apiClient.get(`/charges/definitions/${chargeId}/rules`).then((res) => res.data);
  },

  addChargeRule(chargeId: number, data: CreateChargeRuleRequest): Promise<ChargeRule> {
    return apiClient.post(`/charges/definitions/${chargeId}/rules`, data).then((res) => res.data);
  },

  getProductCharges(productCode: string): Promise<ProductCharge[]> {
    return apiClient.get('/charges/product-charges', { params: { productCode } }).then((res) => res.data);
  },

  assignProductCharge(data: { chargeId: number; referenceId: string; overrideAmount?: number }): Promise<ProductCharge> {
    return apiClient.post('/charges/product-charges', data).then((res) => res.data);
  },

  getCustomerOverrides(customerId: number): Promise<CustomerChargeOverride[]> {
    return apiClient.get('/charges/customer-overrides', { params: { customerId } }).then((res) => res.data);
  },

  assignCustomerCharge(data: { chargeId: number; referenceId: string; overrideAmount?: number }): Promise<CustomerChargeOverride> {
    return apiClient.post('/charges/customer-overrides', data).then((res) => res.data);
  },

  getWaivers(params?: { scope?: string; referenceId?: string }): Promise<FeeWaiver[]> {
    return apiClient.get('/charges/waivers', { params }).then((res) => res.data);
  },

  createWaiver(data: CreateFeeWaiverRequest): Promise<FeeWaiver> {
    return apiClient.post('/charges/waivers', data).then((res) => res.data);
  },

  removeWaiver(id: number): Promise<void> {
    return apiClient.delete(`/charges/waivers/${id}`);
  },

  getApplicableWaivers(params: { chargeId: number; customerId?: number; accountNumber?: string; productCode?: string; date: string }): Promise<FeeWaiver[]> {
    return apiClient.get('/charges/waivers/applicable', { params }).then((res) => res.data);
  },

  getInterestRatesByProduct(productCode: string): Promise<InterestRate[]> {
    return apiClient.get(`/charges/interest/product/${productCode}`).then((res) => res.data);
  },

  createInterestRate(data: CreateInterestRateRequest): Promise<InterestRate> {
    return apiClient.post('/charges/interest', data).then((res) => res.data);
  },

  calculateCharge(data: ChargeCalculationRequest): Promise<ChargeCalculationResponse> {
    return apiClient.post('/charges/calculate', data).then((res) => res.data);
  },
};
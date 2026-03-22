import { Configuration } from '@/api/configuration';
import { CustomerControllerApi } from '@/api/api';
import type { CreateCorporateCustomerRequest, CreateSMECustomerRequest, CreateIndividualCustomerRequest } from '@/api/api';
import type { CustomerVariant } from '@/types/customer.types';
import axios from 'axios';

interface Pageable {
  page?: number;
  size?: number;
  sort?: string[];
}

const createAuthAxios = () => {
  const instance = axios.create();
  instance.interceptors.request.use((config: any) => {
    if (!config.headers['X-Username']) {
      config.headers = config.headers || {};
      config.headers['X-Username'] = localStorage.getItem('username') || 'system';
    }
    return config;
  });
  return instance;
};

const config = new Configuration();
config.baseOptions = { axios: createAuthAxios() };
config.basePath = '';

const customerAPI = new CustomerControllerApi(config);

export const customerService = {
  getAll: async (): Promise<CustomerVariant[]> => {
    const pageable: Pageable = { page: 0, size: 100 };
    const response = await customerAPI.listCustomers(pageable);
    const data = response.data as any;
    return data.content || data;
  },

  getById: async (id: number): Promise<CustomerVariant> => {
    const response = await customerAPI.getCustomerById(id);
    return response.data as CustomerVariant;
  },

  create: async (data: Record<string, unknown>): Promise<CustomerVariant> => {
    const customerType = data.customerType as string;

    if (customerType === 'CORPORATE') {
      const payload = {
        name: data.companyName,
        taxId: data.taxId || data.registrationNumber,
        registrationNumber: data.registrationNumber,
        industry: data.industry,
        employeeCount: data.employeeCount,
        annualRevenueAmount: data.annualRevenueAmount,
        annualRevenueCurrency: data.annualRevenueCurrency || 'USD',
        website: data.website,
        emails: [data.email].filter(Boolean),
        phoneNumber: data.phoneNumber,
        addresses: [],
      } as any;
      const response = await customerAPI.createCorporate(payload);
      return response.data as CustomerVariant;
    } else if (customerType === 'SME') {
      const payload = {
        name: data.companyName,
        taxId: data.taxId || data.registrationNumber,
        registrationNumber: data.registrationNumber,
        industry: data.industry,
        businessType: data.businessType || 'OTHER',
        annualTurnoverAmount: data.annualTurnoverAmount,
        annualTurnoverCurrency: data.annualTurnoverCurrency || 'USD',
        yearsInOperation: data.yearsInOperation,
        emails: [data.email].filter(Boolean),
        phoneNumber: data.phoneNumber,
        addresses: [],
      } as any;
      const response = await customerAPI.createSME(payload);
      return response.data as CustomerVariant;
    } else {
      const firstName = data.firstName as string || '';
      const lastName = data.lastName as string || '';
      const payload = {
        name: `${firstName} ${lastName}`.trim(),
        taxId: data.taxId || data.idNumber,
        dateOfBirth: data.dateOfBirth,
        nationality: data.nationality || '',
        emails: [data.email].filter(Boolean),
        phoneNumber: data.phoneNumber,
        addresses: [],
      } as any;
      const response = await customerAPI.createIndividual(payload);
      return response.data as CustomerVariant;
    }
  },

  update: async (id: number, data: Record<string, unknown>): Promise<CustomerVariant> => {
    let name: string;
    if (data.companyName !== undefined) {
      name = data.companyName as string;
    } else if (data.firstName !== undefined || data.lastName !== undefined) {
      name = `${data.firstName || ''} ${data.lastName || ''}`.trim();
    } else if (data.name !== undefined) {
      name = data.name as string;
    } else {
      throw new Error('Name is required');
    }

    const updateData: Record<string, unknown> = { name };

    if (data.taxId) updateData.taxId = data.taxId;
    if (data.idNumber) updateData.taxId = data.idNumber;
    if (data.registrationNumber) updateData.registrationNumber = data.registrationNumber;
    if (data.industry) updateData.industry = data.industry;
    if (data.email) updateData.email = data.email;
    if (data.phoneNumber) updateData.phoneNumber = data.phoneNumber;
    if (data.employeeCount) updateData.employeeCount = data.employeeCount;
    if (data.annualRevenueAmount) updateData.annualRevenueAmount = data.annualRevenueAmount;
    if (data.annualRevenueCurrency) updateData.annualRevenueCurrency = data.annualRevenueCurrency;
    if (data.website) updateData.website = data.website;
    if (data.businessType) updateData.businessType = data.businessType;
    if (data.annualTurnoverAmount) updateData.annualTurnoverAmount = data.annualTurnoverAmount;
    if (data.annualTurnoverCurrency) updateData.annualTurnoverCurrency = data.annualTurnoverCurrency;
    if (data.yearsInOperation) updateData.yearsInOperation = data.yearsInOperation;
    if (data.dateOfBirth) updateData.dateOfBirth = data.dateOfBirth;
    if (data.nationality) updateData.nationality = data.nationality;

    const response = await customerAPI.updateCustomer(id, updateData as any);
    return response.data as CustomerVariant;
  },
};

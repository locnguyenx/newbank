import apiClient from './apiClient';
import type {
  Currency,
  Country,
  Industry,
  ExchangeRate,
  Holiday,
  Branch,
  Channel,
  DocumentType,
  CreateCurrencyRequest,
  CreateCountryRequest,
  CreateIndustryRequest,
  CreateExchangeRateRequest,
  CreateHolidayRequest,
  CreateBranchRequest,
  CreateChannelRequest,
  CreateDocumentTypeRequest,
} from '@/types/masterData.types';

const BASE = '/master-data';

export const masterDataService = {
  // Currency
  getCurrencies(activeOnly?: boolean): Promise<Currency[]> {
    return apiClient.get(`${BASE}/currencies`, { params: activeOnly !== undefined ? { activeOnly } : undefined }).then((res) => res.data);
  },

  getCurrency(code: string): Promise<Currency> {
    return apiClient.get(`${BASE}/currencies/${code}`).then((res) => res.data);
  },

  createCurrency(data: CreateCurrencyRequest): Promise<Currency> {
    return apiClient.post(`${BASE}/currencies`, data).then((res) => res.data);
  },

  deactivateCurrency(code: string): Promise<Currency> {
    return apiClient.put(`${BASE}/currencies/${code}/deactivate`).then((res) => res.data);
  },

  // Country
  getCountries(activeOnly?: boolean): Promise<Country[]> {
    return apiClient.get(`${BASE}/countries`, { params: activeOnly !== undefined ? { activeOnly } : undefined }).then((res) => res.data);
  },

  getCountry(isoCode: string): Promise<Country> {
    return apiClient.get(`${BASE}/countries/${isoCode}`).then((res) => res.data);
  },

  createCountry(data: CreateCountryRequest): Promise<Country> {
    return apiClient.post(`${BASE}/countries`, data).then((res) => res.data);
  },

  deactivateCountry(isoCode: string): Promise<Country> {
    return apiClient.put(`${BASE}/countries/${isoCode}/deactivate`).then((res) => res.data);
  },

  // Industry
  getIndustries(activeOnly?: boolean): Promise<Industry[]> {
    return apiClient.get(`${BASE}/industries`, { params: activeOnly !== undefined ? { activeOnly } : undefined }).then((res) => res.data);
  },

  getIndustry(code: string): Promise<Industry> {
    return apiClient.get(`${BASE}/industries/${code}`).then((res) => res.data);
  },

  createIndustry(data: CreateIndustryRequest): Promise<Industry> {
    return apiClient.post(`${BASE}/industries`, data).then((res) => res.data);
  },

  // Exchange Rate
  getLatestRate(base: string, target: string): Promise<ExchangeRate> {
    return apiClient.get(`${BASE}/exchange-rates/latest`, { params: { base, target } }).then((res) => res.data);
  },

  createExchangeRate(data: CreateExchangeRateRequest): Promise<ExchangeRate> {
    return apiClient.post(`${BASE}/exchange-rates`, data).then((res) => res.data);
  },

  convertAmount(base: string, target: string, amount: number): Promise<number> {
    return apiClient.get(`${BASE}/exchange-rates/convert`, { params: { base, target, amount } }).then((res) => res.data);
  },

  // Holiday
  getHolidays(countryCode: string, year: number): Promise<Holiday[]> {
    return apiClient.get(`${BASE}/holidays`, { params: { countryCode, year } }).then((res) => res.data);
  },

  isHoliday(countryCode: string, date: string): Promise<boolean> {
    return apiClient.get(`${BASE}/holidays/check`, { params: { countryCode, date } }).then((res) => res.data);
  },

  createHoliday(data: CreateHolidayRequest): Promise<Holiday> {
    return apiClient.post(`${BASE}/holidays`, data).then((res) => res.data);
  },

  // Branch
  getBranches(countryCode?: string): Promise<Branch[]> {
    return apiClient.get(`${BASE}/branches`, { params: countryCode ? { countryCode } : undefined }).then((res) => res.data);
  },

  getBranch(code: string): Promise<Branch> {
    return apiClient.get(`${BASE}/branches/${code}`).then((res) => res.data);
  },

  createBranch(data: CreateBranchRequest): Promise<Branch> {
    return apiClient.post(`${BASE}/branches`, data).then((res) => res.data);
  },

  deactivateBranch(code: string): Promise<Branch> {
    return apiClient.put(`${BASE}/branches/${code}/deactivate`).then((res) => res.data);
  },

  // Channel
  getChannels(): Promise<Channel[]> {
    return apiClient.get(`${BASE}/channels`).then((res) => res.data);
  },

  createChannel(data: CreateChannelRequest): Promise<Channel> {
    return apiClient.post(`${BASE}/channels`, data).then((res) => res.data);
  },

  deactivateChannel(code: string): Promise<Channel> {
    return apiClient.put(`${BASE}/channels/${code}/deactivate`).then((res) => res.data);
  },

  // Document Type
  getDocumentTypes(category?: string): Promise<DocumentType[]> {
    return apiClient.get(`${BASE}/document-types`, { params: category ? { category } : undefined }).then((res) => res.data);
  },

  createDocumentType(data: CreateDocumentTypeRequest): Promise<DocumentType> {
    return apiClient.post(`${BASE}/document-types`, data).then((res) => res.data);
  },

  deactivateDocumentType(code: string): Promise<DocumentType> {
    return apiClient.put(`${BASE}/document-types/${code}/deactivate`).then((res) => res.data);
  },
};

import apiClient from './apiClient';

interface Currency {
  code?: string;
  name?: string;
  symbol?: string;
  decimalPlaces?: number;
  active?: boolean;
}

interface Country {
  isoCode?: string;
  name?: string;
  region?: string;
  active?: boolean;
}

interface Branch {
  code?: string;
  name?: string;
  countryCode?: string;
  address?: string;
  active?: boolean;
}

interface Channel {
  code?: string;
  name?: string;
  active?: boolean;
}

interface DocumentType {
  code?: string;
  name?: string;
  category?: string;
  active?: boolean;
}

interface Industry {
  code?: string;
  name?: string;
  parentCode?: string;
  active?: boolean;
}

interface ExchangeRate {
  id?: number;
  baseCurrencyCode?: string;
  targetCurrencyCode?: string;
  rate?: number;
  effectiveDate?: string;
}

interface Holiday {
  id?: number;
  countryCode?: string;
  holidayDate?: string;
  description?: string;
}

interface CreateCurrencyRequest {
  code: string;
  name: string;
  symbol?: string;
  decimalPlaces: number;
}

interface UpdateCurrencyRequest {
  name?: string;
  symbol?: string;
}

interface CreateCountryRequest {
  isoCode: string;
  name: string;
  region?: string;
}

interface UpdateCountryRequest {
  name?: string;
  region?: string;
}

interface CreateBranchRequest {
  code: string;
  name: string;
  countryCode: string;
  address?: string;
}

interface UpdateBranchRequest {
  name?: string;
  address?: string;
}

interface CreateChannelRequest {
  code: string;
  name: string;
}

interface UpdateChannelRequest {
  name?: string;
}

interface CreateDocumentTypeRequest {
  code: string;
  name: string;
  category: string;
}

interface UpdateDocumentTypeRequest {
  name?: string;
  category?: string;
}

interface CreateIndustryRequest {
  code: string;
  name: string;
  parentCode?: string;
}

interface CreateExchangeRateRequest {
  baseCurrencyCode: string;
  targetCurrencyCode: string;
  rate: number;
  effectiveDate: string;
}

interface CreateHolidayRequest {
  countryCode: string;
  holidayDate: string;
  description?: string;
}

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

  updateCurrency(code: string, data: UpdateCurrencyRequest): Promise<Currency> {
    return apiClient.put(`${BASE}/currencies/${code}`, data).then((res) => res.data);
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

  updateCountry(isoCode: string, data: UpdateCountryRequest): Promise<Country> {
    return apiClient.put(`${BASE}/countries/${isoCode}`, data).then((res) => res.data);
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

  updateBranch(code: string, data: UpdateBranchRequest): Promise<Branch> {
    return apiClient.put(`${BASE}/branches/${code}`, data).then((res) => res.data);
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

  updateChannel(code: string, data: UpdateChannelRequest): Promise<Channel> {
    return apiClient.put(`${BASE}/channels/${code}`, data).then((res) => res.data);
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

  updateDocumentType(code: string, data: UpdateDocumentTypeRequest): Promise<DocumentType> {
    return apiClient.put(`${BASE}/document-types/${code}`, data).then((res) => res.data);
  },
};

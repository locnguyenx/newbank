export interface Currency {
  code: string;
  name: string;
  symbol: string | null;
  decimalPlaces: number;
  active: boolean;
}

export interface Country {
  isoCode: string;
  name: string;
  region: string | null;
  active: boolean;
}

export interface Industry {
  code: string;
  name: string;
  parentCode: string | null;
  active: boolean;
}

export interface ExchangeRate {
  id: number;
  baseCurrencyCode: string;
  targetCurrencyCode: string;
  rate: number;
  effectiveDate: string;
}

export interface Holiday {
  id: number;
  countryCode: string;
  holidayDate: string;
  description: string | null;
}

export interface Branch {
  code: string;
  name: string;
  countryCode: string;
  address: string | null;
  active: boolean;
}

export interface Channel {
  code: string;
  name: string;
  active: boolean;
}

export interface DocumentType {
  code: string;
  name: string;
  category: string;
  active: boolean;
}

export interface CreateCurrencyRequest {
  code: string;
  name: string;
  symbol?: string;
  decimalPlaces: number;
}

export interface CreateCountryRequest {
  isoCode: string;
  name: string;
  region?: string;
}

export interface CreateIndustryRequest {
  code: string;
  name: string;
  parentCode?: string;
}

export interface CreateExchangeRateRequest {
  baseCurrencyCode: string;
  targetCurrencyCode: string;
  rate: number;
  effectiveDate: string;
}

export interface CreateHolidayRequest {
  countryCode: string;
  holidayDate: string;
  description?: string;
}

export interface CreateBranchRequest {
  code: string;
  name: string;
  countryCode: string;
  address?: string;
}

export interface CreateChannelRequest {
  code: string;
  name: string;
}

export interface CreateDocumentTypeRequest {
  code: string;
  name: string;
  category: string;
}

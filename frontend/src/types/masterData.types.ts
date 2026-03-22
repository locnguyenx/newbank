// Re-export types from API
import type {
  CurrencyResponse,
  CountryResponse,
  BranchResponse,
  ChannelResponse,
  DocumentTypeResponse,
} from '@/api/api';
export type {
  CurrencyResponse,
  CountryResponse,
  BranchResponse,
  ChannelResponse,
  DocumentTypeResponse,
};

// Export type aliases
export type Currency = CurrencyResponse;
export type Country = CountryResponse;
export type Branch = BranchResponse;
export type Channel = ChannelResponse;
export type DocumentType = DocumentTypeResponse;

// Re-export types from API
import type { KYCResponse } from '@/api/api';
export type { KYCResponse } from '@/api/api';
import {
  KYCResponseKycLevelEnum,
  KYCResponseStatusEnum,
} from '@/api/api';
export {
  KYCResponseKycLevelEnum,
  KYCResponseStatusEnum,
};

// Define missing types
export type KYC = KYCResponse;

// Define document type (used in frontend)
export type KYCDocumentType = 
  | 'PASSPORT'
  | 'NATIONAL_ID'
  | 'DRIVERS_LICENSE'
  | 'UTILITY_BILL'
  | 'BANK_STATEMENT'
  | 'EMPLOYMENT_LETTER';

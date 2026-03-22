// Re-export types from API
import type { CustomerResponse } from '@/api/api';
export type { CustomerResponse } from '@/api/api';
import {
  CustomerResponseTypeEnum,
  CustomerResponseStatusEnum,
  CustomerResponseEmploymentStatusEnum,
} from '@/api/api';
export {
  CustomerResponseTypeEnum,
  CustomerResponseStatusEnum,
  CustomerResponseEmploymentStatusEnum,
};

// Re-export enums for convenience
export type CustomerType = CustomerResponseTypeEnum;
export type CustomerStatus = CustomerResponseStatusEnum;

// Define variant types (all share same base structure)
export type CustomerVariant = CustomerResponse;
export type IndividualCustomer = CustomerResponse & { type: 'INDIVIDUAL' };
export type SMECustomer = CustomerResponse & { type: 'SME' };
export type CorporateCustomer = CustomerResponse & { type: 'CORPORATE' };

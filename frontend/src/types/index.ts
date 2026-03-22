// Re-export all generated types from OpenAPI spec
export * from '@/api/api';

// Add union types for discriminated unions
export type { CustomerVariant, CorporateCustomer, SMECustomer, IndividualCustomer } from './customer.types';

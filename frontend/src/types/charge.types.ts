// Re-export types from API
import type {
  ChargeDefinitionResponse,
  ChargeRuleResponse,
  ProductChargeResponse,
  CustomerChargeOverrideResponse,
  FeeWaiverResponse,
  InterestRateResponse,
  ChargeCalculationRequest,
  ChargeCalculationResponse,
  CreateChargeDefinitionRequest,
  CreateChargeRuleRequest,
  CreateFeeWaiverRequest,
  CreateInterestRateRequest,
} from '@/api/api';
export type {
  ChargeDefinitionResponse,
  ChargeRuleResponse,
  ProductChargeResponse,
  CustomerChargeOverrideResponse,
  FeeWaiverResponse,
  InterestRateResponse,
  ChargeCalculationRequest,
  ChargeCalculationResponse,
  CreateChargeDefinitionRequest,
  CreateChargeRuleRequest,
  CreateFeeWaiverRequest,
  CreateInterestRateRequest,
};

// Re-export response types as our main types
export type ChargeDefinition = ChargeDefinitionResponse;
export type ChargeRule = ChargeRuleResponse;
export type ProductCharge = ProductChargeResponse;
export type CustomerChargeOverride = CustomerChargeOverrideResponse;
export type FeeWaiver = FeeWaiverResponse;
export type InterestRate = InterestRateResponse;

// Custom enums not in OpenAPI spec but used in frontend
export const ChargeType = {
  MONTHLY_MAINTENANCE: 'MONTHLY_MAINTENANCE',
  TRANSACTION_FEE: 'TRANSACTION_FEE',
  OVERDRAFT_FEE: 'OVERDRAFT_FEE',
  INTEREST: 'INTEREST',
  WIRE_TRANSFER_FEE: 'WIRE_TRANSFER_FEE',
  STATEMENT_FEE: 'STATEMENT_FEE',
  EARLY_CLOSURE_FEE: 'EARLY_CLOSURE_FEE',
} as const;
export type ChargeType = typeof ChargeType[keyof typeof ChargeType];

export const ChargeStatus = {
  ACTIVE: 'ACTIVE',
  INACTIVE: 'INACTIVE',
} as const;
export type ChargeStatus = typeof ChargeStatus[keyof typeof ChargeStatus];

export const WaiverScope = {
  CUSTOMER: 'CUSTOMER',
  ACCOUNT: 'ACCOUNT',
  PRODUCT: 'PRODUCT',
} as const;
export type WaiverScope = typeof WaiverScope[keyof typeof WaiverScope];

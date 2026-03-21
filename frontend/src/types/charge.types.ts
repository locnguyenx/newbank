export const ChargeType = {
  MONTHLY_MAINTENANCE: 'MONTHLY_MAINTENANCE',
  TRANSACTION_FEE: 'TRANSACTION_FEE',
  OVERDRAFT_FEE: 'OVERDRAFT_FEE',
  INTEREST: 'INTEREST',
  WIRE_TRANSFER_FEE: 'WIRE_TRANSFER_FEE',
  STATEMENT_FEE: 'STATEMENT_FEE',
  EARLY_CLOSURE_FEE: 'EARLY_CLOSURE_FEE',
} as const;
export type ChargeType = (typeof ChargeType)[keyof typeof ChargeType];

export const CalculationMethod = {
  FLAT: 'FLAT',
  PERCENTAGE: 'PERCENTAGE',
  TIERED_VOLUME: 'TIERED_VOLUME',
} as const;
export type CalculationMethod = (typeof CalculationMethod)[keyof typeof CalculationMethod];

export const WaiverScope = {
  CUSTOMER: 'CUSTOMER',
  ACCOUNT: 'ACCOUNT',
  PRODUCT: 'PRODUCT',
} as const;
export type WaiverScope = (typeof WaiverScope)[keyof typeof WaiverScope];

export const ChargeStatus = {
  ACTIVE: 'ACTIVE',
  INACTIVE: 'INACTIVE',
} as const;
export type ChargeStatus = (typeof ChargeStatus)[keyof typeof ChargeStatus];

export const InterestSchedule = {
  DAILY: 'DAILY',
  MONTHLY: 'MONTHLY',
  QUARTERLY: 'QUARTERLY',
} as const;
export type InterestSchedule = (typeof InterestSchedule)[keyof typeof InterestSchedule];

export interface ChargeDefinition {
  id: number;
  name: string;
  chargeType: string;
  currency: string;
  status: string;
  createdAt: string;
  updatedAt: string;
}

export interface ChargeTier {
  id: number;
  chargeRuleId: number;
  tierFrom: number;
  tierTo?: number;
  rate: number;
}

export interface ChargeRule {
  id: number;
  chargeDefinitionId: number;
  calculationMethod: string;
  flatAmount?: number;
  percentageRate?: number;
  minAmount?: number;
  maxAmount?: number;
  tiers: ChargeTier[];
  createdAt: string;
  updatedAt: string;
}

export interface ProductCharge {
  id: number;
  chargeDefinitionId: number;
  chargeName: string;
  productCode: string;
  overrideAmount?: number;
  createdAt: string;
  updatedAt: string;
}

export interface CustomerChargeOverride {
  id: number;
  chargeDefinitionId: number;
  chargeName: string;
  customerId: number;
  overrideAmount?: number;
  createdAt: string;
  updatedAt: string;
}

export interface FeeWaiver {
  id: number;
  chargeDefinitionId: number;
  chargeName: string;
  scope: string;
  referenceId: string;
  waiverPercentage: number;
  validFrom: string;
  validTo?: string;
  createdAt: string;
  updatedAt: string;
}

export interface InterestTier {
  id: number;
  interestRateId: number;
  balanceFrom: number;
  balanceTo?: number;
  rate: number;
}

export interface InterestRate {
  id: number;
  chargeDefinitionId: number;
  productCode: string;
  fixedRate?: number;
  accrualSchedule: string;
  applicationSchedule: string;
  tiers: InterestTier[];
  createdAt: string;
  updatedAt: string;
}

export interface ChargeCalculationRequest {
  productCode?: string;
  customerId?: number;
  accountNumber?: string;
  chargeType: string;
  transactionAmount?: number;
  transactionCount?: number;
  currency: string;
}

export interface ChargeCalculationResponse {
  baseAmount: number;
  waiverAmount: number;
  finalAmount: number;
  waiverApplied: boolean;
  waiverId?: string;
  ruleApplied?: string;
}

export interface CreateChargeDefinitionRequest {
  name: string;
  chargeType: string;
  currency: string;
}

export interface CreateChargeRuleRequest {
  calculationMethod: string;
  flatAmount?: number;
  percentageRate?: number;
  minAmount?: number;
  maxAmount?: number;
  tiers?: { tierFrom: number; tierTo?: number; rate: number }[];
}

export interface CreateFeeWaiverRequest {
  chargeId: number;
  scope: string;
  referenceId: string;
  waiverPercentage: number;
  validFrom: string;
  validTo?: string;
}

export interface CreateInterestRateRequest {
  chargeId: number;
  productCode: string;
  fixedRate?: number;
  accrualSchedule: string;
  applicationSchedule: string;
  tiers?: { balanceFrom: number; balanceTo?: number; rate: number }[];
}
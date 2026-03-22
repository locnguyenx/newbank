// Re-export types from API
import type {
  LimitDefinitionResponse,
  ProductLimitResponse,
  CustomerLimitResponse,
  AccountLimitResponse,
  ApprovalRequestResponse,
  LimitCheckRequest,
  LimitCheckResponse,
  EffectiveLimitResponse,
  CreateLimitDefinitionRequest,
  AssignLimitRequest,
} from '@/api/api';
export type {
  LimitDefinitionResponse,
  ProductLimitResponse,
  CustomerLimitResponse,
  AccountLimitResponse,
  ApprovalRequestResponse,
  LimitCheckRequest,
  LimitCheckResponse,
  EffectiveLimitResponse,
  CreateLimitDefinitionRequest,
  AssignLimitRequest,
};

// Export type aliases
export type LimitDefinition = LimitDefinitionResponse;
export type ProductLimit = ProductLimitResponse;
export type CustomerLimit = CustomerLimitResponse;
export type AccountLimit = AccountLimitResponse;
export type ApprovalRequest = ApprovalRequestResponse;

// Custom enums not in OpenAPI spec but used in frontend
export const LimitType = {
  DAILY: 'DAILY',
  WEEKLY: 'WEEKLY',
  MONTHLY: 'MONTHLY',
  PER_TRANSACTION: 'PER_TRANSACTION',
} as const;
export type LimitType = typeof LimitType[keyof typeof LimitType];

export const LimitStatus = {
  ACTIVE: 'ACTIVE',
  INACTIVE: 'INACTIVE',
} as const;
export type LimitStatus = typeof LimitStatus[keyof typeof LimitStatus];

export const ApprovalStatus = {
  PENDING: 'PENDING',
  APPROVED: 'APPROVED',
  REJECTED: 'REJECTED',
} as const;
export type ApprovalStatus = typeof ApprovalStatus[keyof typeof ApprovalStatus];

export const LimitCheckResult = {
  ALLOWED: 'ALLOWED',
  REJECTED: 'REJECTED',
  REQUIRES_APPROVAL: 'REQUIRES_APPROVAL',
} as const;
export type LimitCheckResult = typeof LimitCheckResult[keyof typeof LimitCheckResult];

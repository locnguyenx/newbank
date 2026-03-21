export const LimitType = {
  DAILY: 'DAILY',
  WEEKLY: 'WEEKLY',
  MONTHLY: 'MONTHLY',
  PER_TRANSACTION: 'PER_TRANSACTION',
} as const;
export type LimitType = (typeof LimitType)[keyof typeof LimitType];

export const LimitStatus = {
  ACTIVE: 'ACTIVE',
  INACTIVE: 'INACTIVE',
} as const;
export type LimitStatus = (typeof LimitStatus)[keyof typeof LimitStatus];

export const LimitCheckResult = {
  ALLOWED: 'ALLOWED',
  REJECTED: 'REJECTED',
  REQUIRES_APPROVAL: 'REQUIRES_APPROVAL',
} as const;
export type LimitCheckResult = (typeof LimitCheckResult)[keyof typeof LimitCheckResult];

export const ApprovalStatus = {
  PENDING: 'PENDING',
  APPROVED: 'APPROVED',
  REJECTED: 'REJECTED',
} as const;
export type ApprovalStatus = (typeof ApprovalStatus)[keyof typeof ApprovalStatus];

export interface LimitDefinition {
  id: number;
  name: string;
  limitType: LimitType;
  amount: number;
  currency: string;
  status: LimitStatus;
  createdAt: string;
  updatedAt: string;
}

export interface ProductLimit {
  id: number;
  limitDefinitionId: number;
  limitName: string;
  productCode: string;
  overrideAmount?: number;
  createdAt: string;
  updatedAt: string;
}

export interface CustomerLimit {
  id: number;
  limitDefinitionId: number;
  limitName: string;
  customerId: number;
  overrideAmount?: number;
  createdAt: string;
  updatedAt: string;
}

export interface AccountLimit {
  id: number;
  limitDefinitionId: number;
  limitName: string;
  accountNumber: string;
  overrideAmount?: number;
  createdAt: string;
  updatedAt: string;
}

export interface ApprovalRequest {
  id: number;
  limitDefinitionId: number;
  transactionReference: string;
  amount: number;
  currency: string;
  accountNumber: string;
  status: ApprovalStatus;
  rejectionReason?: string;
  approvedBy?: string;
  decisionAt?: string;
  createdAt: string;
}

export interface LimitCheckRequest {
  accountNumber: string;
  customerId?: number;
  productCode?: string;
  transactionAmount: number;
  currency: string;
  transactionReference: string;
  limitType: LimitType;
}

export interface LimitCheckResponse {
  result: LimitCheckResult;
  effectiveLimit: number;
  currentUsage: number;
  remainingAmount: number;
  approvalRequired: boolean;
  rejectionReason?: string;
}

export interface EffectiveLimitResponse {
  limitDefinitionId: number;
  limitName: string;
  limitType: LimitType;
  effectiveLimit: number;
  source: string;
  currency: string;
  status: LimitStatus;
}

export interface CreateLimitDefinitionRequest {
  name: string;
  limitType: LimitType;
  amount: number;
  currency: string;
}

export interface AssignLimitRequest {
  limitId: number;
  referenceId: string;
  overrideAmount?: number;
}

export interface ApprovalActionRequest {
  reason?: string;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
}

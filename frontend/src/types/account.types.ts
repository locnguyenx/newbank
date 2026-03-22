// Re-export types from API
import type { AccountResponse, AccountOpeningRequest, AccountHolderRequest } from '@/api/api';
export type { AccountResponse, AccountOpeningRequest, AccountHolderRequest } from '@/api/api';
import {
  AccountResponseTypeEnum,
  AccountResponseStatusEnum,
} from '@/api/api';
export {
  AccountResponseTypeEnum,
  AccountResponseStatusEnum,
};

// Extend generated types with additional fields needed by frontend
export interface AccountBalance {
  availableBalance: number;
  ledgerBalance: number;
  holdAmount: number;
  currency: string;
}

export interface AccountHolderSummary {
  id: number;
  customerId: number;
  customerName?: string;
  role: string;
}

// Account holder role options
export const AccountHolderRole = {
  PRIMARY: 'PRIMARY',
  JOINT: 'JOINT',
  SIGNATORY: 'SIGNATORY',
  BENEFICIARY: 'BENEFICIARY',
} as const;
export type AccountHolderRole = typeof AccountHolderRole[keyof typeof AccountHolderRole];

// Account statement type
export interface AccountStatement {
  id: number;
  accountNumber: string;
  periodStart: string;
  periodEnd: string;
  openingBalance: number;
  closingBalance: number;
  currency: string;
  transactions: AccountTransaction[];
}

export interface AccountTransaction {
  id: number;
  date: string;
  description: string;
  amount: number;
  balance: number;
  reference: string;
}

// Re-export enums for convenience
export type AccountType = typeof AccountResponseTypeEnum[keyof typeof AccountResponseTypeEnum];
export type AccountStatus = typeof AccountResponseStatusEnum[keyof typeof AccountResponseStatusEnum];

// Re-export complex types used in services
export type AccountDetails = AccountResponse & {
  accountBalance?: AccountBalance;
  holders?: AccountHolderSummary[];
};

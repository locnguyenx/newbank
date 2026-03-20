export type AccountType = 'CURRENT' | 'SAVINGS' | 'FIXED_DEPOSIT' | 'LOAN' | 'ESCROW';
export type AccountStatus = 'PENDING' | 'ACTIVE' | 'DORMANT' | 'FROZEN' | 'CLOSED';
export type Currency = 'USD' | 'EUR' | 'GBP' | 'SGD' | 'JPY' | 'CAD' | 'AUD' | 'CHF';
export type AccountHolderRole = 'PRIMARY' | 'JOINT' | 'AUTHORIZED_SIGNATORY' | 'NOMINEE';

export interface AccountResponse {
  id: number;
  accountNumber: string;
  type: AccountType;
  status: AccountStatus;
  currency: Currency;
  balance: number;
  productId: number;
  customerId?: number;
  openedAt: string;
  closedAt: string | null;
}

export interface AccountDetails {
  id: number;
  accountNumber: string;
  type: AccountType;
  status: AccountStatus;
  currency: Currency;
  balance: number;
  productId: number;
  customerId?: number;
  openedAt: string;
  closedAt: string | null;
  accountBalance: {
    availableBalance: number;
    ledgerBalance: number;
    holdAmount: number;
    currency: Currency;
  };
  holders: AccountHolderSummary[];
}

export interface AccountHolderSummary {
  id: number;
  customerName: string;
  role: AccountHolderRole;
  status: string;
  effectiveFrom: string;
}

export interface AccountStatementFilter {
  fromDate: string;
  toDate: string;
  transactionType?: string;
}

export interface AccountStatement {
  accountNumber: string;
  fromDate: string;
  toDate: string;
  openingBalance: number;
  closingBalance: number;
  totalCredits: number;
  totalDebits: number;
  transactions: TransactionEntry[];
}

export interface TransactionEntry {
  id: string;
  date: string;
  description: string;
  amount: number;
  type: 'CREDIT' | 'DEBIT';
}

export interface AccountOpeningRequest {
  customerId: number;
  productCode: string;
  type: AccountType;
  currency: Currency;
  initialDeposit: number;
  holders: { customerId: number; role: AccountHolderRole }[];
}

export interface CustomerAccountSummary {
  customerId: number;
  totalBalance: number;
  accountCount: number;
  currency: string;
}

export const ProductFamily = {
  ACCOUNT: 'ACCOUNT',
  PAYMENT: 'PAYMENT',
  TRADE_FINANCE: 'TRADE_FINANCE',
} as const;
export type ProductFamily = (typeof ProductFamily)[keyof typeof ProductFamily];

export const ProductStatus = {
  DRAFT: 'DRAFT',
  PENDING_APPROVAL: 'PENDING_APPROVAL',
  APPROVED: 'APPROVED',
  ACTIVE: 'ACTIVE',
  SUPERSEDED: 'SUPERSEDED',
  RETIRED: 'RETIRED',
} as const;
export type ProductStatus = (typeof ProductStatus)[keyof typeof ProductStatus];

export const FeeCalculationMethod = {
  FLAT: 'FLAT',
  PERCENTAGE: 'PERCENTAGE',
  TIERED_VOLUME: 'TIERED_VOLUME',
} as const;
export type FeeCalculationMethod = (typeof FeeCalculationMethod)[keyof typeof FeeCalculationMethod];

export interface Product {
  id: number;
  code: string;
  name: string;
  family: ProductFamily;
  description?: string;
}

export interface ProductVersion {
  id: number;
  productId: number;
  versionNumber: number;
  status: ProductStatus;
  submittedBy?: string;
  approvedBy?: string;
  rejectionComment?: string;
  contractCount: number;
  createdAt: string;
}

export interface ProductDetail extends ProductVersion {
  product: Product;
  features: ProductFeature[];
  feeEntries: ProductFeeEntry[];
  segments: ProductSegment[];
}

export interface ProductFeature {
  id: number;
  featureKey: string;
  featureValue: string;
}

export interface ProductFeeTier {
  id: number;
  tierFrom: number;
  tierTo?: number;
  rate: number;
}

export interface ProductFeeEntry {
  id: number;
  feeType: string;
  calculationMethod: FeeCalculationMethod;
  amount?: number;
  rate?: number;
  currency: string;
  tiers: ProductFeeTier[];
}

export interface ProductSegment {
  id: number;
  customerType: string;
}

export interface ProductSearchParams {
  search?: string;
  family?: ProductFamily;
  status?: ProductStatus;
  customerType?: string;
  page?: number;
  size?: number;
}

export interface CreateProductRequest {
  code: string;
  name: string;
  family: ProductFamily;
  description?: string;
}

export interface UpdateProductRequest {
  name?: string;
  description?: string;
}

export interface ProductFeatureRequest {
  featureKey: string;
  featureValue: string;
}

export interface ProductFeeEntryRequest {
  feeType: string;
  calculationMethod: FeeCalculationMethod;
  amount?: number;
  rate?: number;
  currency: string;
  tiers?: { tierFrom: number; tierTo?: number; rate: number }[];
}

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
}

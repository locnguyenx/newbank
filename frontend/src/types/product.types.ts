// Re-export product-related types from generated OpenAPI client
import type {
  ProductResponse as ProductResponseBase,
  ProductDetailResponse as ProductDetailResponseBase,
  ProductFeatureResponse as ProductFeatureResponseBase,
  ProductFeeEntryResponse as ProductFeeEntryResponseBase,
  ProductFeatureRequest as ProductFeatureRequestBase,
  ProductFeeEntryRequest as ProductFeeEntryRequestBase,
  CreateProductRequest as CreateProductRequestBase,
  UpdateProductRequest as UpdateProductRequestBase,
  ProductVersionResponse,
} from '@/api/api';
import {
  CreateProductRequestFamilyEnum,
  CreateProductRequestCustomerTypesEnum,
  ProductFeeEntryRequestCalculationMethodEnum,
} from '@/api/api';
export {
  CreateProductRequestFamilyEnum,
  CreateProductRequestCustomerTypesEnum,
  ProductFeeEntryRequestCalculationMethodEnum,
} from '@/api/api';

// Re-export core types
export type ProductResponse = ProductResponseBase;
export type ProductDetailResponse = ProductDetailResponseBase;
export type ProductFeatureResponse = ProductFeatureResponseBase;
export type ProductFeeEntryResponse = ProductFeeEntryResponseBase;

// Define custom frontend enum for product status (not in OpenAPI spec)
export const ProductStatus = {
  DRAFT: 'DRAFT',
  PENDING_APPROVAL: 'PENDING_APPROVAL',
  APPROVED: 'APPROVED',
  ACTIVE: 'ACTIVE',
  SUPERSEDED: 'SUPERSEDED',
  RETIRED: 'RETIRED',
} as const;

export type ProductStatus = typeof ProductStatus[keyof typeof ProductStatus];

// Export Product (list item type)
export type Product = ProductResponseBase;

// Export Product detail type
export type ProductDetail = ProductDetailResponseBase;

// Export request types
export type CreateProductRequest = CreateProductRequestBase;
export type UpdateProductRequest = UpdateProductRequestBase;
export type ProductFeatureRequest = ProductFeatureRequestBase;
export type ProductFeeEntryRequest = ProductFeeEntryRequestBase;

// Re-export version types
export type ProductVersion = ProductVersionResponse;
export type ProductFeature = ProductFeatureResponse;
export type ProductFeeEntry = ProductFeeEntryResponse;

// Export enums
export { CreateProductRequestFamilyEnum as ProductFamily };
export { CreateProductRequestCustomerTypesEnum as ProductCustomerType };
export { ProductFeeEntryRequestCalculationMethodEnum as FeeCalculationMethod };

// Define frontend-specific search parameters
export interface ProductSearchParams {
  search?: string;
  family?: CreateProductRequestFamilyEnum;
  customerType?: CreateProductRequestCustomerTypesEnum;
  status?: string;
  page?: number;
  size?: number;
}

// Re-export PaginatedResponse from API
export type PaginatedResponse<T = ProductResponseBase> = {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
};

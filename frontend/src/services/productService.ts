import apiClient from './apiClient';
import type {
  Product,
  ProductDetail,
  ProductVersion,
  ProductFeature,
  ProductFeeEntry,
  ProductSearchParams,
  CreateProductRequest,
  UpdateProductRequest,
  ProductFeatureRequest,
  ProductFeeEntryRequest,
  PaginatedResponse,
} from '@/types/product.types';

export const productService = {
  getProducts(params?: ProductSearchParams): Promise<PaginatedResponse<Product>> {
    return apiClient.get('/products', { params }).then((res) => res.data);
  },

  getProductById(id: number): Promise<ProductDetail> {
    return apiClient.get(`/products/${id}`).then((res) => res.data);
  },

  createProduct(data: CreateProductRequest): Promise<Product> {
    return apiClient.post('/products', data).then((res) => res.data);
  },

  updateProduct(id: number, data: UpdateProductRequest): Promise<Product> {
    return apiClient.put(`/products/${id}`, data).then((res) => res.data);
  },

  submitForApproval(productId: number, versionId: number): Promise<ProductVersion> {
    return apiClient.post(`/products/${productId}/versions/${versionId}/submit`).then((res) => res.data);
  },

  approveProduct(productId: number, versionId: number): Promise<ProductVersion> {
    return apiClient.post(`/products/${productId}/versions/${versionId}/approve`).then((res) => res.data);
  },

  rejectProduct(productId: number, versionId: number, comment: string): Promise<ProductVersion> {
    return apiClient.post(`/products/${productId}/versions/${versionId}/reject`, { comment }).then((res) => res.data);
  },

  activateProduct(productId: number, versionId: number): Promise<ProductVersion> {
    return apiClient.post(`/products/${productId}/versions/${versionId}/activate`).then((res) => res.data);
  },

  retireProduct(productId: number, versionId: number): Promise<ProductVersion> {
    return apiClient.post(`/products/${productId}/versions/${versionId}/retire`).then((res) => res.data);
  },

  getVersionHistory(productId: number): Promise<ProductVersion[]> {
    return apiClient.get(`/products/${productId}/versions`).then((res) => res.data);
  },

  getFeatures(productId: number, versionId: number): Promise<ProductFeature[]> {
    return apiClient.get(`/products/${productId}/versions/${versionId}/features`).then((res) => res.data);
  },

  addFeature(productId: number, versionId: number, data: ProductFeatureRequest): Promise<ProductFeature> {
    return apiClient.post(`/products/${productId}/versions/${versionId}/features`, data).then((res) => res.data);
  },

  updateFeature(productId: number, versionId: number, featureId: number, data: ProductFeatureRequest): Promise<ProductFeature> {
    return apiClient.put(`/products/${productId}/versions/${versionId}/features/${featureId}`, data).then((res) => res.data);
  },

  removeFeature(productId: number, versionId: number, featureId: number): Promise<void> {
    return apiClient.delete(`/products/${productId}/versions/${versionId}/features/${featureId}`);
  },

  getFeeEntries(productId: number, versionId: number): Promise<ProductFeeEntry[]> {
    return apiClient.get(`/products/${productId}/versions/${versionId}/fees`).then((res) => res.data);
  },

  addFeeEntry(productId: number, versionId: number, data: ProductFeeEntryRequest): Promise<ProductFeeEntry> {
    return apiClient.post(`/products/${productId}/versions/${versionId}/fees`, data).then((res) => res.data);
  },

  removeFeeEntry(productId: number, versionId: number, feeId: number): Promise<void> {
    return apiClient.delete(`/products/${productId}/versions/${versionId}/fees/${feeId}`);
  },

  getSegments(productId: number, versionId: number): Promise<string[]> {
    return apiClient.get(`/products/${productId}/versions/${versionId}/segments`).then((res) => res.data);
  },

  assignSegments(productId: number, versionId: number, segments: string[]): Promise<void> {
    return apiClient.post(`/products/${productId}/versions/${versionId}/segments`, { segments });
  },

  getActiveProductByCode(code: string): Promise<ProductDetail> {
    return apiClient.get(`/products/active/by-code/${code}`).then((res) => res.data);
  },

  getProductVersionById(versionId: number): Promise<ProductDetail> {
    return apiClient.get(`/products/versions/${versionId}`).then((res) => res.data);
  },

  getActiveProductsByFamily(family: string): Promise<Product[]> {
    return apiClient.get('/products/active', { params: { family } }).then((res) => res.data);
  },

  getActiveProductsByCustomerType(type: string): Promise<Product[]> {
    return apiClient.get('/products/active/by-customer-type', { params: { customerType: type } }).then((res) => res.data);
  },
};

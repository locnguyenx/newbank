import { Configuration } from '@/api/configuration';
import { ProductControllerApi, ProductVersionControllerApi, ProductFeatureControllerApi, ProductPricingControllerApi, ProductSegmentControllerApi } from '@/api/api';
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
import axios from 'axios';

const createAuthAxios = () => {
  const instance = axios.create();
  instance.interceptors.request.use((config: any) => {
    if (!config.headers['X-Username']) {
      config.headers = config.headers || {};
      config.headers['X-Username'] = localStorage.getItem('username') || 'system';
    }
    return config;
  });
  return instance;
};

const config = new Configuration();
config.baseOptions = { axios: createAuthAxios() };
config.basePath = '';

const productAPI = new ProductControllerApi(config);
const productVersionAPI = new ProductVersionControllerApi(config);
const productFeatureAPI = new ProductFeatureControllerApi(config);
const productPricingAPI = new ProductPricingControllerApi(config);
const productSegmentAPI = new ProductSegmentControllerApi(config);

export const productService = {
  getXUsername(): string {
    return localStorage.getItem('username') || 'system';
  },
  getProducts(params?: ProductSearchParams): Promise<PaginatedResponse<Product>> {
    const page = params?.page ?? 0;
    const size = params?.size ?? 10;
    return productAPI.searchProducts(params?.family || undefined, params?.status || undefined, params?.customerType || undefined, page, size)
      .then((response: any) => {
        const data = response.data as any;
        return {
          content: data.content || data || [],
          totalElements: data.totalElements || 0,
          totalPages: data.totalPages || 0
        } as PaginatedResponse<Product>;
      });
  },

  getProductById(id: number): Promise<ProductDetail> {
    return productAPI.getProductDetail(id).then((res: any) => res.data as ProductDetail);
  },

  createProduct(data: CreateProductRequest): Promise<Product> {
    return productAPI.createProduct(this.getXUsername(), data).then((res: any) => res.data as Product);
  },

  updateProduct(id: number, data: UpdateProductRequest): Promise<Product> {
    return productAPI.updateProduct(id, this.getXUsername(), data).then((res: any) => res.data as Product);
  },

  submitForApproval(productId: number, versionId: number): Promise<ProductVersion> {
    return productVersionAPI.submitForApproval(productId, versionId, this.getXUsername()).then((res: any) => res.data as ProductVersion);
  },

  approveProduct(productId: number, versionId: number): Promise<ProductVersion> {
    return productVersionAPI.approve(productId, versionId, this.getXUsername()).then((res: any) => res.data as ProductVersion);
  },

  rejectProduct(productId: number, versionId: number, comment: string): Promise<ProductVersion> {
    return productVersionAPI.reject(productId, versionId, this.getXUsername(), { comment }).then((res: any) => res.data as ProductVersion);
  },

  activateProduct(productId: number, versionId: number): Promise<ProductVersion> {
    return productVersionAPI.activate(productId, versionId, this.getXUsername()).then((res: any) => res.data as ProductVersion);
  },

  retireProduct(productId: number, versionId: number): Promise<ProductVersion> {
    return productVersionAPI.retire(productId, versionId, this.getXUsername()).then((res: any) => res.data as ProductVersion);
  },

  getVersionHistory(productId: number): Promise<ProductVersion[]> {
    return productVersionAPI.listVersions(productId).then((res: any) => res.data as ProductVersion[]);
  },

  getFeatures(productId: number, versionId: number): Promise<ProductFeature[]> {
    return productFeatureAPI.listFeatures(productId, versionId).then((res: any) => res.data as ProductFeature[]);
  },

  addFeature(productId: number, versionId: number, data: ProductFeatureRequest): Promise<ProductFeature> {
    return productFeatureAPI.addFeature(productId, versionId, this.getXUsername(), data).then((res: any) => res.data as ProductFeature);
  },

  updateFeature(productId: number, versionId: number, featureId: number, data: ProductFeatureRequest): Promise<ProductFeature> {
    return productFeatureAPI.updateFeature(productId, versionId, featureId, this.getXUsername(), data).then((res: any) => res.data as ProductFeature);
  },

  removeFeature(productId: number, versionId: number, featureId: number): Promise<void> {
    return productFeatureAPI.deleteFeature(productId, versionId, featureId).then(() => undefined);
  },

  getFeeEntries(productId: number, versionId: number): Promise<ProductFeeEntry[]> {
    return productPricingAPI.listFeeEntries(productId, versionId).then((res: any) => res.data as ProductFeeEntry[]);
  },

  addFeeEntry(productId: number, versionId: number, data: ProductFeeEntryRequest): Promise<ProductFeeEntry> {
    return productPricingAPI.addFeeEntry(productId, versionId, this.getXUsername(), data).then((res: any) => res.data as ProductFeeEntry);
  },

  removeFeeEntry(productId: number, versionId: number, feeId: number): Promise<void> {
    return productPricingAPI.deleteFeeEntry(productId, versionId, feeId).then(() => undefined);
  },

  getSegments(productId: number, versionId: number): Promise<string[]> {
    return productSegmentAPI.getSegments(productId, versionId).then((res: any) => res.data as string[]);
  },

  assignSegments(productId: number, versionId: number, segments: string[]): Promise<void> {
    return productSegmentAPI.assignSegments(productId, versionId, this.getXUsername(), { customerTypes: segments as any }).then(() => undefined);
  },

  getActiveProductByCode(code: string): Promise<ProductDetail> {
    return productAPI.getProductByCode(code).then((res: any) => res.data as ProductDetail);
  },

  getProductVersionById(versionId: number): Promise<ProductDetail> {
    return productAPI.getProductDetail(versionId).then((res: any) => res.data as ProductDetail);
  },

  getActiveProductsByFamily(family: string): Promise<Product[]> {
    return productAPI.searchProducts(family as any, undefined, undefined, 0, 100)
      .then((response: any) => {
        const all = (response.data.content || response.data) as Product[];
        return all.filter((p: Product) => p.family === family);
      });
  },

  getActiveProductsByCustomerType(type: string): Promise<Product[]> {
    return productAPI.searchProducts(undefined, undefined, type as any, 0, 100)
      .then((response: any) => {
        const all = (response.data.content || response.data) as Product[];
        return all;
      });
  },
};

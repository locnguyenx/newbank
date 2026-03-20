import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
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
import { productService } from '@/services/productService';

interface PaginationState {
  totalElements: number;
  totalPages: number;
}

interface ProductState {
  products: Product[];
  selectedProduct: ProductDetail | null;
  versionHistory: ProductVersion[];
  features: ProductFeature[];
  feeEntries: ProductFeeEntry[];
  segments: string[];
  loading: boolean;
  error: string | null;
  filters: ProductSearchParams;
  pagination: PaginationState;
}

const initialState: ProductState = {
  products: [],
  selectedProduct: null,
  versionHistory: [],
  features: [],
  feeEntries: [],
  segments: [],
  loading: false,
  error: null,
  filters: {},
  pagination: {
    totalElements: 0,
    totalPages: 0,
  },
};

export const fetchProducts = createAsyncThunk<PaginatedResponse<Product>, ProductSearchParams | undefined>(
  'product/fetchAll',
  async (params = {}) => {
    return await productService.getProducts(params);
  }
);

export const fetchProductById = createAsyncThunk<ProductDetail, number>(
  'product/fetchById',
  async (id) => {
    return await productService.getProductById(id);
  }
);

export const createProduct = createAsyncThunk<Product, CreateProductRequest>(
  'product/create',
  async (data) => {
    return await productService.createProduct(data);
  }
);

export const updateProduct = createAsyncThunk<Product, { id: number; data: UpdateProductRequest }>(
  'product/update',
  async ({ id, data }) => {
    return await productService.updateProduct(id, data);
  }
);

export const submitForApproval = createAsyncThunk<ProductVersion, { productId: number; versionId: number }>(
  'product/submitForApproval',
  async ({ productId, versionId }) => {
    return await productService.submitForApproval(productId, versionId);
  }
);

export const approveProduct = createAsyncThunk<ProductVersion, { productId: number; versionId: number }>(
  'product/approve',
  async ({ productId, versionId }) => {
    return await productService.approveProduct(productId, versionId);
  }
);

export const rejectProduct = createAsyncThunk<ProductVersion, { productId: number; versionId: number; comment: string }>(
  'product/reject',
  async ({ productId, versionId, comment }) => {
    return await productService.rejectProduct(productId, versionId, comment);
  }
);

export const activateProduct = createAsyncThunk<ProductVersion, { productId: number; versionId: number }>(
  'product/activate',
  async ({ productId, versionId }) => {
    return await productService.activateProduct(productId, versionId);
  }
);

export const retireProduct = createAsyncThunk<ProductVersion, { productId: number; versionId: number }>(
  'product/retire',
  async ({ productId, versionId }) => {
    return await productService.retireProduct(productId, versionId);
  }
);

export const fetchVersionHistory = createAsyncThunk<ProductVersion[], number>(
  'product/fetchVersionHistory',
  async (productId) => {
    return await productService.getVersionHistory(productId);
  }
);

export const addFeature = createAsyncThunk<ProductFeature, { productId: number; versionId: number; data: ProductFeatureRequest }>(
  'product/addFeature',
  async ({ productId, versionId, data }) => {
    return await productService.addFeature(productId, versionId, data);
  }
);

export const updateFeature = createAsyncThunk<ProductFeature, { productId: number; versionId: number; featureId: number; data: ProductFeatureRequest }>(
  'product/updateFeature',
  async ({ productId, versionId, featureId, data }) => {
    return await productService.updateFeature(productId, versionId, featureId, data);
  }
);

export const removeFeature = createAsyncThunk<void, { productId: number; versionId: number; featureId: number }>(
  'product/removeFeature',
  async ({ productId, versionId, featureId }) => {
    await productService.removeFeature(productId, versionId, featureId);
  }
);

export const addFeeEntry = createAsyncThunk<ProductFeeEntry, { productId: number; versionId: number; data: ProductFeeEntryRequest }>(
  'product/addFeeEntry',
  async ({ productId, versionId, data }) => {
    return await productService.addFeeEntry(productId, versionId, data);
  }
);

export const removeFeeEntry = createAsyncThunk<void, { productId: number; versionId: number; feeId: number }>(
  'product/removeFeeEntry',
  async ({ productId, versionId, feeId }) => {
    await productService.removeFeeEntry(productId, versionId, feeId);
  }
);

export const assignSegments = createAsyncThunk<void, { productId: number; versionId: number; segments: string[] }>(
  'product/assignSegments',
  async ({ productId, versionId, segments }) => {
    await productService.assignSegments(productId, versionId, segments);
  }
);

const productSlice = createSlice({
  name: 'product',
  initialState,
  reducers: {
    clearSelectedProduct: (state) => {
      state.selectedProduct = null;
    },
    clearError: (state) => {
      state.error = null;
    },
    setFilters: (state, action) => {
      state.filters = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder
      // fetchProducts
      .addCase(fetchProducts.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchProducts.fulfilled, (state, action) => {
        state.loading = false;
        state.products = action.payload.content;
        state.pagination.totalElements = action.payload.totalElements;
        state.pagination.totalPages = action.payload.totalPages;
      })
      .addCase(fetchProducts.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch products';
      })
      // fetchProductById
      .addCase(fetchProductById.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchProductById.fulfilled, (state, action) => {
        state.loading = false;
        state.selectedProduct = action.payload;
        state.features = action.payload.features || [];
        state.feeEntries = action.payload.feeEntries || [];
        state.segments = action.payload.segments?.map((s) => s.customerType) || [];
      })
      .addCase(fetchProductById.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch product';
      })
      // createProduct
      .addCase(createProduct.fulfilled, (state, action) => {
        state.products.push(action.payload);
      })
      // submitForApproval, approveProduct, rejectProduct, activateProduct, retireProduct
      .addCase(submitForApproval.fulfilled, (state, action) => {
        if (state.selectedProduct && state.selectedProduct.id === action.payload.productId) {
          state.selectedProduct.status = action.payload.status;
          state.selectedProduct.submittedBy = action.payload.submittedBy;
        }
      })
      .addCase(approveProduct.fulfilled, (state, action) => {
        if (state.selectedProduct && state.selectedProduct.id === action.payload.productId) {
          state.selectedProduct.status = action.payload.status;
          state.selectedProduct.approvedBy = action.payload.approvedBy;
        }
      })
      .addCase(rejectProduct.fulfilled, (state, action) => {
        if (state.selectedProduct && state.selectedProduct.id === action.payload.productId) {
          state.selectedProduct.status = action.payload.status;
          state.selectedProduct.rejectionComment = action.payload.rejectionComment;
        }
      })
      .addCase(activateProduct.fulfilled, (state, action) => {
        if (state.selectedProduct && state.selectedProduct.id === action.payload.productId) {
          state.selectedProduct.status = action.payload.status;
        }
      })
      .addCase(retireProduct.fulfilled, (state, action) => {
        if (state.selectedProduct && state.selectedProduct.id === action.payload.productId) {
          state.selectedProduct.status = action.payload.status;
        }
      })
      // fetchVersionHistory
      .addCase(fetchVersionHistory.fulfilled, (state, action) => {
        state.versionHistory = action.payload;
      })
      // addFeature
      .addCase(addFeature.fulfilled, (state, action) => {
        state.features.push(action.payload);
      })
      // updateFeature
      .addCase(updateFeature.fulfilled, (state, action) => {
        const idx = state.features.findIndex((f) => f.id === action.payload.id);
        if (idx !== -1) {
          state.features[idx] = action.payload;
        }
      })
      // removeFeature
      .addCase(removeFeature.fulfilled, (state, action) => {
        const featureId = action.meta.arg.featureId;
        state.features = state.features.filter((f) => f.id !== featureId);
      })
      // addFeeEntry
      .addCase(addFeeEntry.fulfilled, (state, action) => {
        state.feeEntries.push(action.payload);
      })
      // removeFeeEntry
      .addCase(removeFeeEntry.fulfilled, (state, action) => {
        const feeId = action.meta.arg.feeId;
        state.feeEntries = state.feeEntries.filter((f) => f.id !== feeId);
      })
      // assignSegments
      .addCase(assignSegments.fulfilled, (state, action) => {
        state.segments = action.meta.arg.segments;
      });
  },
});

export const { clearSelectedProduct, clearError, setFilters } = productSlice.actions;
export default productSlice.reducer;

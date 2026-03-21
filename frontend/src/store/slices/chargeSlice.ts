import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import type {
  ChargeDefinition,
  ChargeRule,
  ProductCharge,
  CustomerChargeOverride,
  FeeWaiver,
  InterestRate,
  ChargeCalculationRequest,
  ChargeCalculationResponse,
  CreateChargeDefinitionRequest,
  CreateChargeRuleRequest,
  CreateFeeWaiverRequest,
  CreateInterestRateRequest,
} from '@/types/charge.types';
import { chargeService } from '@/services/chargeService';

interface ChargeState {
  charges: ChargeDefinition[];
  chargeRules: Record<number, ChargeRule[]>;
  productCharges: ProductCharge[];
  customerOverrides: CustomerChargeOverride[];
  waivers: FeeWaiver[];
  interestRates: InterestRate[];
  calculationResult: ChargeCalculationResponse | null;
  loading: boolean;
  error: string | null;
}

const initialState: ChargeState = {
  charges: [],
  chargeRules: {},
  productCharges: [],
  customerOverrides: [],
  waivers: [],
  interestRates: [],
  calculationResult: null,
  loading: false,
  error: null,
};

export const fetchChargeDefinitions = createAsyncThunk<ChargeDefinition[], { chargeType?: string; status?: string }>(
  'charges/fetchAll',
  async (params = {}) => {
    return await chargeService.getChargeDefinitions(params);
  }
);

export const fetchChargeDefinitionById = createAsyncThunk<ChargeDefinition, number>(
  'charges/fetchById',
  async (id) => {
    return await chargeService.getChargeDefinitionById(id);
  }
);

export const createChargeDefinition = createAsyncThunk<ChargeDefinition, CreateChargeDefinitionRequest>(
  'charges/create',
  async (data) => {
    return await chargeService.createChargeDefinition(data);
  }
);

export const activateCharge = createAsyncThunk<ChargeDefinition, number>(
  'charges/activate',
  async (id) => {
    return await chargeService.activateCharge(id);
  }
);

export const deactivateCharge = createAsyncThunk<ChargeDefinition, number>(
  'charges/deactivate',
  async (id) => {
    return await chargeService.deactivateCharge(id);
  }
);

export const fetchChargeRules = createAsyncThunk<ChargeRule[], number>(
  'charges/fetchRules',
  async (chargeId) => {
    return await chargeService.getChargeRules(chargeId);
  }
);

export const addChargeRule = createAsyncThunk<ChargeRule, { chargeId: number; data: CreateChargeRuleRequest }>(
  'charges/addRule',
  async ({ chargeId, data }) => {
    return await chargeService.addChargeRule(chargeId, data);
  }
);

export const fetchProductCharges = createAsyncThunk<ProductCharge[], string>(
  'charges/fetchProductCharges',
  async (productCode) => {
    return await chargeService.getProductCharges(productCode);
  }
);

export const assignProductCharge = createAsyncThunk<ProductCharge, { chargeId: number; referenceId: string; overrideAmount?: number }>(
  'charges/assignProductCharge',
  async (data) => {
    return await chargeService.assignProductCharge(data);
  }
);

export const fetchCustomerOverrides = createAsyncThunk<CustomerChargeOverride[], number>(
  'charges/fetchCustomerOverrides',
  async (customerId) => {
    return await chargeService.getCustomerOverrides(customerId);
  }
);

export const assignCustomerCharge = createAsyncThunk<CustomerChargeOverride, { chargeId: number; referenceId: string; overrideAmount?: number }>(
  'charges/assignCustomerCharge',
  async (data) => {
    return await chargeService.assignCustomerCharge(data);
  }
);

export const fetchWaivers = createAsyncThunk<FeeWaiver[], { scope?: string; referenceId?: string }>(
  'charges/fetchWaivers',
  async (params = {}) => {
    return await chargeService.getWaivers(params);
  }
);

export const createWaiver = createAsyncThunk<FeeWaiver, CreateFeeWaiverRequest>(
  'charges/createWaiver',
  async (data) => {
    return await chargeService.createWaiver(data);
  }
);

export const removeWaiver = createAsyncThunk<number, number>(
  'charges/removeWaiver',
  async (id) => {
    await chargeService.removeWaiver(id);
    return id;
  }
);

export const fetchApplicableWaivers = createAsyncThunk<FeeWaiver[], { chargeId: number; customerId?: number; accountNumber?: string; productCode?: string; date: string }>(
  'charges/fetchApplicableWaivers',
  async (params) => {
    return await chargeService.getApplicableWaivers(params);
  }
);

export const fetchInterestRatesByProduct = createAsyncThunk<InterestRate[], string>(
  'charges/fetchInterestRates',
  async (productCode) => {
    return await chargeService.getInterestRatesByProduct(productCode);
  }
);

export const createInterestRate = createAsyncThunk<InterestRate, CreateInterestRateRequest>(
  'charges/createInterestRate',
  async (data) => {
    return await chargeService.createInterestRate(data);
  }
);

export const calculateCharge = createAsyncThunk<ChargeCalculationResponse, ChargeCalculationRequest>(
  'charges/calculate',
  async (data) => {
    return await chargeService.calculateCharge(data);
  }
);

const chargeSlice = createSlice({
  name: 'charges',
  initialState,
  reducers: {
    clearCalculationResult: (state) => {
      state.calculationResult = null;
    },
    clearError: (state) => {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchChargeDefinitions.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchChargeDefinitions.fulfilled, (state, action) => {
        state.loading = false;
        state.charges = action.payload;
      })
      .addCase(fetchChargeDefinitions.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch charges';
      })
      .addCase(createChargeDefinition.fulfilled, (state, action) => {
        state.charges.push(action.payload);
      })
      .addCase(activateCharge.fulfilled, (state, action) => {
        const idx = state.charges.findIndex((c) => c.id === action.payload.id);
        if (idx !== -1) {
          state.charges[idx] = action.payload;
        }
      })
      .addCase(deactivateCharge.fulfilled, (state, action) => {
        const idx = state.charges.findIndex((c) => c.id === action.payload.id);
        if (idx !== -1) {
          state.charges[idx] = action.payload;
        }
      })
      .addCase(fetchChargeRules.fulfilled, (state, action) => {
        const chargeId = action.meta.arg;
        state.chargeRules[chargeId] = action.payload;
      })
      .addCase(addChargeRule.fulfilled, (state, action) => {
        const chargeId = action.meta.arg.chargeId;
        if (!state.chargeRules[chargeId]) {
          state.chargeRules[chargeId] = [];
        }
        state.chargeRules[chargeId].push(action.payload);
      })
      .addCase(fetchProductCharges.fulfilled, (state, action) => {
        state.productCharges = action.payload;
      })
      .addCase(fetchCustomerOverrides.fulfilled, (state, action) => {
        state.customerOverrides = action.payload;
      })
      .addCase(fetchWaivers.fulfilled, (state, action) => {
        state.waivers = action.payload;
      })
      .addCase(createWaiver.fulfilled, (state, action) => {
        state.waivers.push(action.payload);
      })
      .addCase(removeWaiver.fulfilled, (state, action) => {
        state.waivers = state.waivers.filter((w) => w.id !== action.payload);
      })
      .addCase(fetchInterestRatesByProduct.fulfilled, (state, action) => {
        state.interestRates = action.payload;
      })
      .addCase(createInterestRate.fulfilled, (state, action) => {
        state.interestRates.push(action.payload);
      })
      .addCase(calculateCharge.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(calculateCharge.fulfilled, (state, action) => {
        state.loading = false;
        state.calculationResult = action.payload;
      })
      .addCase(calculateCharge.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to calculate charge';
      });
  },
});

export const { clearCalculationResult, clearError } = chargeSlice.actions;
export default chargeSlice.reducer;
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import type {
  Currency,
  Country,
  Branch,
  Channel,
  DocumentType,
} from '@/types/masterData.types';
import { masterDataService } from '@/services/masterDataService';

interface MasterDataState {
  currencies: Currency[];
  countries: Country[];
  branches: Branch[];
  channels: Channel[];
  documentTypes: DocumentType[];
  loading: boolean;
  error: string | null;
}

const initialState: MasterDataState = {
  currencies: [],
  countries: [],
  branches: [],
  channels: [],
  documentTypes: [],
  loading: false,
  error: null,
};

export const fetchCurrencies = createAsyncThunk<Currency[], boolean | undefined>(
  'masterData/fetchCurrencies',
  async (activeOnly) => {
    return await masterDataService.getCurrencies(activeOnly);
  }
);

export const fetchCountries = createAsyncThunk<Country[], boolean | undefined>(
  'masterData/fetchCountries',
  async (activeOnly) => {
    return await masterDataService.getCountries(activeOnly);
  }
);

export const fetchBranches = createAsyncThunk<Branch[], string | undefined>(
  'masterData/fetchBranches',
  async (countryCode) => {
    return await masterDataService.getBranches(countryCode);
  }
);

export const fetchChannels = createAsyncThunk<Channel[], void>(
  'masterData/fetchChannels',
  async () => {
    return await masterDataService.getChannels();
  }
);

export const fetchDocumentTypes = createAsyncThunk<DocumentType[], string | undefined>(
  'masterData/fetchDocumentTypes',
  async (category) => {
    return await masterDataService.getDocumentTypes(category);
  }
);

const masterDataSlice = createSlice({
  name: 'masterData',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      // fetchCurrencies
      .addCase(fetchCurrencies.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchCurrencies.fulfilled, (state, action) => {
        state.loading = false;
        state.currencies = action.payload;
      })
      .addCase(fetchCurrencies.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch currencies';
      })
      // fetchCountries
      .addCase(fetchCountries.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchCountries.fulfilled, (state, action) => {
        state.loading = false;
        state.countries = action.payload;
      })
      .addCase(fetchCountries.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch countries';
      })
      // fetchBranches
      .addCase(fetchBranches.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchBranches.fulfilled, (state, action) => {
        state.loading = false;
        state.branches = action.payload;
      })
      .addCase(fetchBranches.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch branches';
      })
      // fetchChannels
      .addCase(fetchChannels.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchChannels.fulfilled, (state, action) => {
        state.loading = false;
        state.channels = action.payload;
      })
      .addCase(fetchChannels.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch channels';
      })
      // fetchDocumentTypes
      .addCase(fetchDocumentTypes.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchDocumentTypes.fulfilled, (state, action) => {
        state.loading = false;
        state.documentTypes = action.payload;
      })
      .addCase(fetchDocumentTypes.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || 'Failed to fetch document types';
      });
  },
});

export const { clearError } = masterDataSlice.actions;
export default masterDataSlice.reducer;

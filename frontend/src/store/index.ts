import { configureStore } from '@reduxjs/toolkit';
import customerReducer from './slices/customerSlice';
import employmentReducer from './slices/employmentSlice';
import kycReducer from './slices/kycSlice';
import authorizationReducer from './slices/authorizationSlice';
import accountReducer from './slices/accountSlice';
import productReducer from './slices/productSlice';
import masterDataReducer from './slices/masterDataSlice';
import limitReducer from './slices/limitSlice';
import chargeReducer from './slices/chargeSlice';

export const store = configureStore({
  reducer: {
    customer: customerReducer,
    employment: employmentReducer,
    kyc: kycReducer,
    authorizations: authorizationReducer,
    account: accountReducer,
    products: productReducer,
    masterData: masterDataReducer,
    limits: limitReducer,
    charges: chargeReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

import { configureStore } from '@reduxjs/toolkit';
import customerReducer from './slices/customerSlice';
import employmentReducer from './slices/employmentSlice';
import kycReducer from './slices/kycSlice';
import authorizationReducer from './slices/authorizationSlice';
import accountReducer from './slices/accountSlice';
import productReducer from './slices/productSlice';

export const store = configureStore({
  reducer: {
    customer: customerReducer,
    employment: employmentReducer,
    kyc: kycReducer,
    authorizations: authorizationReducer,
    account: accountReducer,
    products: productReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

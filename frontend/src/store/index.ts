import { configureStore } from '@reduxjs/toolkit';
import customerReducer from './slices/customerSlice';
import employmentReducer from './slices/employmentSlice';
import kycReducer from './slices/kycSlice';

export const store = configureStore({
  reducer: {
    customer: customerReducer,
    employment: employmentReducer,
    kyc: kycReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

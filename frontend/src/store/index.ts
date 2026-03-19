import { configureStore } from '@reduxjs/toolkit';
import customerReducer from './slices/customerSlice';
import employmentReducer from './slices/employmentSlice';

export const store = configureStore({
  reducer: {
    customer: customerReducer,
    employment: employmentReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

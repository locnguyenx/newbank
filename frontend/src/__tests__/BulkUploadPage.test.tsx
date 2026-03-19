import { render, screen } from '@testing-library/react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { describe, it, expect, beforeEach } from 'vitest';
import { BulkUploadPage } from '../pages/employment/BulkUploadPage';
import employmentReducer from '../store/slices/employmentSlice';

const createTestStore = () => {
  return configureStore({
    reducer: {
      employment: employmentReducer,
    },
    preloadedState: {
      employment: {
        employments: [],
        loading: false,
        error: null,
        uploadProgress: 0,
        uploadResult: null,
        uploadLoading: false,
      },
    },
  });
};

const renderWithProviders = (component: React.ReactElement) => {
  const store = createTestStore();
  return {
    ...render(
      <Provider store={store}>
        <MemoryRouter initialEntries={['/customers/1/employees/bulk-upload']}>
          <Routes>
            <Route path="/customers/:customerId/employees/bulk-upload" element={component} />
          </Routes>
        </MemoryRouter>
      </Provider>
    ),
    store,
  };
};

describe('BulkUploadPage', () => {
  beforeEach(() => {
    localStorage.setItem('authToken', 'test-token');
  });

  it('renders bulk upload page', () => {
    renderWithProviders(<BulkUploadPage />);
    expect(screen.getByText('Bulk Upload Employees')).toBeInTheDocument();
  });

  it('renders file selector button', () => {
    renderWithProviders(<BulkUploadPage />);
    expect(screen.getByText('Select CSV File')).toBeInTheDocument();
  });

  it('renders upload button', () => {
    renderWithProviders(<BulkUploadPage />);
    expect(screen.getByText('Upload')).toBeInTheDocument();
  });

  it('renders reset button', () => {
    renderWithProviders(<BulkUploadPage />);
    expect(screen.getByText('Reset')).toBeInTheDocument();
  });

  it('renders CSV format instructions', () => {
    renderWithProviders(<BulkUploadPage />);
    expect(screen.getByText(/employeeName/i)).toBeInTheDocument();
  });
});

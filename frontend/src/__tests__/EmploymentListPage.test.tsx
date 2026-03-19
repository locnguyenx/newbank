import { render, screen } from '@testing-library/react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { describe, it, expect, beforeEach } from 'vitest';
import { EmploymentListPage } from '../pages/employment/EmploymentListPage';
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
        <MemoryRouter initialEntries={['/customers/1/employees']}>
          <Routes>
            <Route path="/customers/:customerId/employees" element={component} />
          </Routes>
        </MemoryRouter>
      </Provider>
    ),
    store,
  };
};

describe('EmploymentListPage', () => {
  beforeEach(() => {
    localStorage.setItem('authToken', 'test-token');
  });

  it('renders employment list page', () => {
    renderWithProviders(<EmploymentListPage />);
    expect(screen.getByText('Employees')).toBeInTheDocument();
  });

  it('renders Bulk Upload button', () => {
    renderWithProviders(<EmploymentListPage />);
    expect(screen.getByText('Bulk Upload')).toBeInTheDocument();
  });

  it('renders status filter', () => {
    renderWithProviders(<EmploymentListPage />);
    expect(screen.getByText('Filter by status')).toBeInTheDocument();
  });

  it('renders table columns', () => {
    renderWithProviders(<EmploymentListPage />);
    expect(screen.getByText('Employee ID')).toBeInTheDocument();
    expect(screen.getByText('Name')).toBeInTheDocument();
    expect(screen.getByText('Department')).toBeInTheDocument();
    expect(screen.getByText('Position')).toBeInTheDocument();
  });
});

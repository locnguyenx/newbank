import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen } from '@testing-library/react';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import ProtectedRoute from './ProtectedRoute';
import authReducer from '@/store/slices/authSlice';

const mockNavigate = vi.fn();
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  };
});

describe('ProtectedRoute', () => {
  const createStore = (isAuthenticated: boolean, user: { roles: string[] } | null) =>
    configureStore({
      reducer: {
        auth: authReducer,
      },
      preloadedState: {
        auth: {
          user,
          isAuthenticated,
          loading: false,
          error: null,
          mfaRequired: false,
        },
      },
    });

  const TestComponent = () => <div>Protected Content</div>;

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should render children when authenticated with no role restrictions', () => {
    const store = createStore(true, { roles: ['SYSTEM_ADMIN'] });

    render(
      <Provider store={store}>
        <MemoryRouter initialEntries={['/dashboard']}>
          <Routes>
            <Route
              path="/dashboard"
              element={
                <ProtectedRoute>
                  <TestComponent />
                </ProtectedRoute>
              }
            />
          </Routes>
        </MemoryRouter>
      </Provider>
    );

    expect(screen.getByText('Protected Content')).toBeInTheDocument();
  });

  it('should render children when user has allowed role', () => {
    const store = createStore(true, { roles: ['SYSTEM_ADMIN'] });

    render(
      <Provider store={store}>
        <MemoryRouter initialEntries={['/iam/users']}>
          <Routes>
            <Route
              path="/iam/users"
              element={
                <ProtectedRoute allowedRoles={['SYSTEM_ADMIN']}>
                  <TestComponent />
                </ProtectedRoute>
              }
            />
          </Routes>
        </MemoryRouter>
      </Provider>
    );

    expect(screen.getByText('Protected Content')).toBeInTheDocument();
  });

  it('should redirect to /unauthorized when user lacks required role', () => {
    const store = createStore(true, { roles: ['COMPANY_ADMIN'] });

    render(
      <Provider store={store}>
        <MemoryRouter initialEntries={['/iam/users']}>
          <Routes>
            <Route
              path="/iam/users"
              element={
                <ProtectedRoute allowedRoles={['SYSTEM_ADMIN']}>
                  <TestComponent />
                </ProtectedRoute>
              }
            />
            <Route path="/unauthorized" element={<div>Unauthorized</div>} />
          </Routes>
        </MemoryRouter>
      </Provider>
    );

    expect(screen.getByText('Unauthorized')).toBeInTheDocument();
  });

  it('should show loading spinner when loading is true', () => {
    const store = configureStore({
      reducer: {
        auth: (state = {}) => state,
      },
      preloadedState: {
        auth: {
          user: null,
          isAuthenticated: false,
          loading: true,
          error: null,
          mfaRequired: false,
        },
      },
    });

    render(
      <Provider store={store}>
        <MemoryRouter initialEntries={['/dashboard']}>
          <Routes>
            <Route
              path="/dashboard"
              element={
                <ProtectedRoute>
                  <TestComponent />
                </ProtectedRoute>
              }
            />
          </Routes>
        </MemoryRouter>
      </Provider>
    );

    expect(document.querySelector('.ant-spin')).toBeInTheDocument();
  });

  it('should redirect to /login when not authenticated', () => {
    const store = createStore(false, null);

    render(
      <Provider store={store}>
        <MemoryRouter initialEntries={['/dashboard']}>
          <Routes>
            <Route
              path="/dashboard"
              element={
                <ProtectedRoute>
                  <TestComponent />
                </ProtectedRoute>
              }
            />
            <Route path="/login" element={<div>Login Page</div>} />
          </Routes>
        </MemoryRouter>
      </Provider>
    );

    expect(screen.getByText('Login Page')).toBeInTheDocument();
  });
});

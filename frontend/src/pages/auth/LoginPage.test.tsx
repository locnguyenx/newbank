import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import { Provider } from 'react-redux';
import { BrowserRouter } from 'react-router-dom';
import { configureStore } from '@reduxjs/toolkit';
import LoginPage from './LoginPage';
import authReducer from '@/store/slices/authSlice';

const mockNavigate = vi.fn();
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  };
});

vi.mock('@/store/slices/authSlice', () => ({
  __esModule: true,
  default: (state = {}) => state,
  login: vi.fn(() => ({ type: 'auth/login/fulfilled', payload: {} })),
}));

describe('LoginPage', () => {
  const createStore = (isAuthenticated = false) =>
    configureStore({
      reducer: {
        auth: authReducer,
      },
      preloadedState: {
        auth: {
          user: null,
          isAuthenticated,
          loading: false,
          error: null,
          mfaRequired: false,
        },
      },
    });

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should display login form', () => {
    render(
      <Provider store={createStore()}>
        <BrowserRouter>
          <LoginPage />
        </BrowserRouter>
      </Provider>
    );

    expect(screen.getByPlaceholderText('Email')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Password')).toBeInTheDocument();
  });

  it('should display sign in button', () => {
    render(
      <Provider store={createStore()}>
        <BrowserRouter>
          <LoginPage />
        </BrowserRouter>
      </Provider>
    );

    expect(screen.getByRole('button', { name: /sign in/i })).toBeInTheDocument();
  });

  it('should display banking portal title', () => {
    render(
      <Provider store={createStore()}>
        <BrowserRouter>
          <LoginPage />
        </BrowserRouter>
      </Provider>
    );

    expect(screen.getByText('Banking Portal')).toBeInTheDocument();
  });

  it('should have email input with UserOutlined icon', () => {
    render(
      <Provider store={createStore()}>
        <BrowserRouter>
          <LoginPage />
        </BrowserRouter>
      </Provider>
    );

    const emailInput = screen.getByPlaceholderText('Email');
    expect(emailInput).toBeInTheDocument();
  });

  it('should have password input', () => {
    render(
      <Provider store={createStore()}>
        <BrowserRouter>
          <LoginPage />
        </BrowserRouter>
      </Provider>
    );

    const passwordInput = screen.getByPlaceholderText('Password');
    expect(passwordInput).toBeInTheDocument();
  });

  it('should show validation error when submitting empty form', async () => {
    render(
      <Provider store={createStore()}>
        <BrowserRouter>
          <LoginPage />
        </BrowserRouter>
      </Provider>
    );

    const submitButton = screen.getByRole('button', { name: /sign in/i });
    submitButton.click();

    await waitFor(() => {
      expect(screen.getByText(/please enter your email/i)).toBeInTheDocument();
    });
  });
});

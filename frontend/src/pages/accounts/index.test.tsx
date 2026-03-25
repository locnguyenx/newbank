import { describe, it, expect } from 'vitest';
import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { AccountListPage } from './AccountListPage';
import { AccountDetailPage } from './AccountDetailPage';
import { AccountOpeningForm } from './AccountOpeningForm';

describe('Account Pages', () => {
  it('AccountListPage should display accounts title', () => {
    render(
      <MemoryRouter>
        <AccountListPage />
      </MemoryRouter>
    );
    expect(screen.getByText('Accounts')).toBeInTheDocument();
  });

  it('AccountDetailPage should render', () => {
    render(
      <MemoryRouter>
        <AccountDetailPage />
      </MemoryRouter>
    );
  });

  it('AccountOpeningForm should render', () => {
    render(
      <MemoryRouter>
        <AccountOpeningForm />
      </MemoryRouter>
    );
  });
});

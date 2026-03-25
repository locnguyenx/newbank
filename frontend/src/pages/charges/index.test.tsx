import { describe, it, expect } from 'vitest';
import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { ChargeListPage } from './ChargeListPage';

describe('ChargeListPage', () => {
  it('should render', () => {
    render(
      <MemoryRouter>
        <ChargeListPage />
      </MemoryRouter>
    );
  });
});

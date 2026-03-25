import { describe, it, expect } from 'vitest';
import { render } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { EmploymentListPage } from './EmploymentListPage';
import { BulkUploadPage } from './BulkUploadPage';

describe('Employment Pages', () => {
  it('EmploymentListPage should render', () => {
    render(
      <MemoryRouter>
        <EmploymentListPage />
      </MemoryRouter>
    );
  });

  it('BulkUploadPage should render', () => {
    render(
      <MemoryRouter>
        <BulkUploadPage />
      </MemoryRouter>
    );
  });
});

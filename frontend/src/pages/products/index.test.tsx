import { describe, it, expect } from 'vitest';
import { render } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { ProductListPage } from './ProductListPage';
import { ProductDetailPage } from './ProductDetailPage';
import { ProductFormPage } from './ProductFormPage';
import { ProductVersionComparePage } from './ProductVersionComparePage';

describe('Product Pages', () => {
  it('ProductListPage should render', () => {
    render(
      <MemoryRouter>
        <ProductListPage />
      </MemoryRouter>
    );
  });

  it('ProductDetailPage should render', () => {
    render(
      <MemoryRouter>
        <ProductDetailPage />
      </MemoryRouter>
    );
  });

  it('ProductFormPage should render', () => {
    render(
      <MemoryRouter>
        <ProductFormPage />
      </MemoryRouter>
    );
  });

  it('ProductVersionComparePage should render', () => {
    render(
      <MemoryRouter>
        <ProductVersionComparePage />
      </MemoryRouter>
    );
  });
});

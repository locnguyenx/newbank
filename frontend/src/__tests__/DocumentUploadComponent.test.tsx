import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { DocumentUploadComponent } from '../components/DocumentUploadComponent';

describe('DocumentUploadComponent', () => {
  it('renders the component', () => {
    const onDocumentsChange = () => {};
    render(<DocumentUploadComponent onDocumentsChange={onDocumentsChange} />);
    expect(screen.getByText('Document Type')).toBeInTheDocument();
  });

  it('renders empty state', () => {
    const onDocumentsChange = () => {};
    render(<DocumentUploadComponent onDocumentsChange={onDocumentsChange} />);
    expect(screen.getByText('No documents added')).toBeInTheDocument();
  });

  it('renders the add file button', () => {
    const onDocumentsChange = () => {};
    render(<DocumentUploadComponent onDocumentsChange={onDocumentsChange} />);
    expect(screen.getByText('Add File')).toBeInTheDocument();
  });
});

import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { BulkUploadResults } from '../pages/employment/BulkUploadResults';
import type { BulkUploadResult } from '../types';

describe('BulkUploadResults', () => {
  it('renders success results', () => {
    const result: BulkUploadResult = {
      totalRows: 10,
      successCount: 10,
      failureCount: 0,
      errors: [],
    };
    render(<BulkUploadResults result={result} />);
    expect(screen.getByText('Total Rows')).toBeInTheDocument();
    expect(screen.getByText('Successful')).toBeInTheDocument();
    expect(screen.getByText('Failed')).toBeInTheDocument();
    expect(screen.getAllByText('10')).toHaveLength(2);
    expect(screen.getByText('0')).toBeInTheDocument();
    expect(screen.getByText(/all 10 rows uploaded successfully/i)).toBeInTheDocument();
  });

  it('renders failure results with errors', () => {
    const result: BulkUploadResult = {
      totalRows: 5,
      successCount: 3,
      failureCount: 2,
      errors: [
        { row: 2, field: 'salary', message: 'Invalid number', value: 'abc' },
        { row: 4, field: 'startDate', message: 'Invalid date', value: 'not-a-date' },
      ],
    };
    render(<BulkUploadResults result={result} />);
    expect(screen.getByText('3')).toBeInTheDocument();
    expect(screen.getByText(/2 row\(s\) failed/i)).toBeInTheDocument();
    expect(screen.getByText('salary')).toBeInTheDocument();
    expect(screen.getByText('startDate')).toBeInTheDocument();
    expect(screen.getByText('Invalid number')).toBeInTheDocument();
    expect(screen.getByText('Invalid date')).toBeInTheDocument();
  });

  it('renders error table with correct columns', () => {
    const result: BulkUploadResult = {
      totalRows: 1,
      successCount: 0,
      failureCount: 1,
      errors: [{ row: 1, field: 'email', message: 'Required', value: '' }],
    };
    render(<BulkUploadResults result={result} />);
    expect(screen.getByText('Row')).toBeInTheDocument();
    expect(screen.getByText('Field')).toBeInTheDocument();
    expect(screen.getByText('Value')).toBeInTheDocument();
    expect(screen.getByText('Error')).toBeInTheDocument();
  });
});

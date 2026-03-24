import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import BatchPaymentPage from './BatchPaymentPage';

global.fetch = vi.fn();

describe('BatchPaymentPage', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should display batch payments list', async () => {
    const mockBatches = [
      {
        id: 1,
        batchReference: 'BATCH-001',
        customerId: 1,
        status: 'PENDING',
        fileFormat: 'CSV',
        totalAmount: '25000.00',
        currency: 'USD',
        instructionCount: 10,
      },
      {
        id: 2,
        batchReference: 'BATCH-002',
        customerId: 1,
        status: 'COMPLETED',
        fileFormat: 'CSV',
        totalAmount: '50000.00',
        currency: 'USD',
        instructionCount: 25,
      },
    ];

    vi.mocked(global.fetch).mockResolvedValue({
      ok: true,
      json: vi.fn().mockResolvedValue({ success: true, data: mockBatches }),
    } as any);

    render(<BatchPaymentPage />);

    await waitFor(() => {
      expect(screen.getByText('BATCH-001')).toBeInTheDocument();
      expect(screen.getByText('BATCH-002')).toBeInTheDocument();
    });
  });

  it('should display page title', () => {
    render(<BatchPaymentPage />);
    expect(screen.getByText('Batch Payments')).toBeInTheDocument();
  });

  it('should show create batch payment button', () => {
    render(<BatchPaymentPage />);
    expect(screen.getByText('Create Batch Payment')).toBeInTheDocument();
  });
});

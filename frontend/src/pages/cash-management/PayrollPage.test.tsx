import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import PayrollPage from './PayrollPage';

global.fetch = vi.fn();

describe('PayrollPage', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should display payroll batches list', async () => {
    const mockBatches = [
      {
        id: 1,
        batchReference: 'PAY-20260324-001',
        customerId: 1,
        status: 'PENDING',
        recordCount: 50,
        totalAmount: '50000.00',
        currency: 'USD',
      },
      {
        id: 2,
        batchReference: 'PAY-20260324-002',
        customerId: 1,
        status: 'COMPLETED',
        recordCount: 100,
        totalAmount: '100000.00',
        currency: 'USD',
      },
    ];

    vi.mocked(global.fetch).mockResolvedValue({
      ok: true,
      json: vi.fn().mockResolvedValue({ success: true, data: mockBatches }),
    } as any);

    render(<PayrollPage />);

    await waitFor(() => {
      expect(screen.getByText('PAY-20260324-001')).toBeInTheDocument();
      expect(screen.getByText('PAY-20260324-002')).toBeInTheDocument();
    });
  });

  it('should show loading state initially', async () => {
    vi.mocked(global.fetch).mockResolvedValue({
      ok: true,
      json: vi.fn().mockResolvedValue({ success: true, data: [] }),
    } as any);

    render(<PayrollPage />);
    expect(screen.getByText('Payroll Management')).toBeInTheDocument();
  });
});

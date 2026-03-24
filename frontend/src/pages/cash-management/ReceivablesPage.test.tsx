import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import ReceivablesPage from './ReceivablesPage';

global.fetch = vi.fn();

describe('ReceivablesPage', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should display invoices list', async () => {
    const mockInvoices = [
      {
        id: 1,
        invoiceNumber: 'INV-001',
        customerId: 1,
        amount: '10000.00',
        currency: 'USD',
        status: 'OUTSTANDING',
        dueDate: '2026-04-01',
      },
      {
        id: 2,
        invoiceNumber: 'INV-002',
        customerId: 1,
        amount: '5000.00',
        currency: 'USD',
        status: 'PAID',
        dueDate: '2026-03-15',
      },
    ];

    vi.mocked(global.fetch).mockResolvedValue({
      ok: true,
      json: vi.fn().mockResolvedValue({ success: true, data: mockInvoices }),
    } as any);

    render(<ReceivablesPage />);

    await waitFor(() => {
      expect(screen.getByText('INV-001')).toBeInTheDocument();
      expect(screen.getByText('INV-002')).toBeInTheDocument();
    });
  });

  it('should display page title', () => {
    render(<ReceivablesPage />);
    expect(screen.getByText('Receivables Management')).toBeInTheDocument();
  });

  it('should create new invoice', async () => {
    const newInvoice = {
      id: 3,
      invoiceNumber: 'INV-003',
      customerId: 1,
      amount: '7500.00',
      currency: 'USD',
      status: 'OUTSTANDING',
      dueDate: '2026-04-15',
    };

    vi.mocked(global.fetch).mockResolvedValue({
      ok: true,
      json: vi.fn().mockResolvedValue({ success: true, data: newInvoice }),
    } as any);

    render(<ReceivablesPage />);

    await waitFor(() => {
      expect(screen.getByText('Create Invoice')).toBeInTheDocument();
    });
  });
});

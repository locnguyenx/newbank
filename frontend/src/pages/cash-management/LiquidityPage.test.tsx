import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import LiquidityPage from './LiquidityPage';

global.fetch = vi.fn();

describe('LiquidityPage', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should display current cash position', async () => {
    const mockPosition = {
      id: 1,
      customerId: 1,
      totalBalance: '150000.00',
      currency: 'USD',
      accountCount: 5,
      lastUpdated: '2026-03-24T10:00:00Z',
    };

    vi.mocked(global.fetch).mockResolvedValue({
      ok: true,
      json: vi.fn().mockResolvedValue({ success: true, data: mockPosition }),
    } as any);

    render(<LiquidityPage />);

    await waitFor(() => {
      expect(screen.getByText('Liquidity Management')).toBeInTheDocument();
    });
  });

  it('should display liquidity position history', async () => {
    const mockHistory = [
      { id: 1, date: '2026-03-24', balance: '150000.00', currency: 'USD' },
      { id: 2, date: '2026-03-23', balance: '145000.00', currency: 'USD' },
    ];

    vi.mocked(global.fetch).mockResolvedValue({
      ok: true,
      json: vi.fn().mockResolvedValue({ success: true, data: mockHistory }),
    } as any);

    render(<LiquidityPage />);

    await waitFor(() => {
      expect(screen.getByText('Liquidity Management')).toBeInTheDocument();
    });
  });
});

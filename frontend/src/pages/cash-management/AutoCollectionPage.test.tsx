import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import AutoCollectionPage from './AutoCollectionPage';

global.fetch = vi.fn();

describe('AutoCollectionPage', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should display auto-collection rules list', async () => {
    const mockRules = [
      {
        id: 1,
        ruleName: 'Daily Invoice Collection',
        customerId: 1,
        isActive: true,
        triggerType: 'ON_INVOICE_DUE',
        amountType: 'INVOICE_AMOUNT',
      },
      {
        id: 2,
        ruleName: 'Monthly Subscription Collection',
        customerId: 1,
        isActive: false,
        triggerType: 'SCHEDULED',
        amountType: 'FIXED_AMOUNT',
      },
    ];

    vi.mocked(global.fetch).mockResolvedValue({
      ok: true,
      json: vi.fn().mockResolvedValue({ success: true, data: mockRules }),
    } as any);

    render(<AutoCollectionPage />);

    await waitFor(() => {
      expect(screen.getByText('Daily Invoice Collection')).toBeInTheDocument();
      expect(screen.getByText('Monthly Subscription Collection')).toBeInTheDocument();
    });
  });

  it('should display page title', () => {
    render(<AutoCollectionPage />);
    expect(screen.getByText('Auto-Collection')).toBeInTheDocument();
  });

  it('should show create rule button', () => {
    render(<AutoCollectionPage />);
    expect(screen.getByText('Create Rule')).toBeInTheDocument();
  });
});

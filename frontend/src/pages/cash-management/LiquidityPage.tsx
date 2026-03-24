import { useState, useEffect } from 'react';
import { Card, Row, Col, Statistic, Table, message } from 'antd';

interface LiquidityPosition {
  id: number;
  customerId: number;
  totalBalance: string;
  currency: string;
  accountCount: number;
  lastUpdated: string;
}

export default function LiquidityPage() {
  const [position, setPosition] = useState<LiquidityPosition | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadPosition();
  }, []);

  const loadPosition = async () => {
    try {
      setLoading(true);
      const response = await fetch('/api/cash-management/liquidity/position?customerId=1');
      const result = await response.json();
      if (result.success) {
        setPosition(result.data);
      }
    } catch (error) {
      message.error('Failed to load liquidity position');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card title="Liquidity Management">
      <Row gutter={16}>
        <Col span={8}>
          <Statistic
            title="Total Balance"
            value={position?.totalBalance || '0'}
            prefix={position?.currency || 'USD'}
            loading={loading}
          />
        </Col>
        <Col span={8}>
          <Statistic
            title="Accounts"
            value={position?.accountCount || 0}
            loading={loading}
          />
        </Col>
        <Col span={8}>
          <Statistic
            title="Last Updated"
            value={position?.lastUpdated || '-'}
            loading={loading}
          />
        </Col>
      </Row>
    </Card>
  );
}

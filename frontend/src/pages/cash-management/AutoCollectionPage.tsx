import { useState, useEffect } from 'react';
import { Table, Button, Card, Tag, Switch, message } from 'antd';
import type { ColumnsType } from 'antd/es/table';

interface AutoCollectionRule {
  id: number;
  ruleName: string;
  customerId: number;
  isActive: boolean;
  triggerType: string;
  amountType: string;
}

export default function AutoCollectionPage() {
  const [rules, setRules] = useState<AutoCollectionRule[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadRules();
  }, []);

  const loadRules = async () => {
    try {
      setLoading(true);
      const response = await fetch('/api/cash-management/auto-collection/rules');
      const result = await response.json();
      if (result.success) {
        setRules(result.data);
      }
    } catch (error) {
      message.error('Failed to load auto-collection rules');
    } finally {
      setLoading(false);
    }
  };

  const getTriggerLabel = (trigger: string) => {
    switch (trigger) {
      case 'ON_INVOICE_DUE':
        return 'On Invoice Due';
      case 'SCHEDULED':
        return 'Scheduled';
      case 'BALANCE_THRESHOLD':
        return 'Balance Threshold';
      default:
        return trigger;
    }
  };

  const columns: ColumnsType<AutoCollectionRule> = [
    {
      title: 'Rule Name',
      dataIndex: 'ruleName',
      key: 'ruleName',
    },
    {
      title: 'Trigger',
      dataIndex: 'triggerType',
      key: 'triggerType',
      render: (trigger: string) => (
        <Tag>{getTriggerLabel(trigger)}</Tag>
      ),
    },
    {
      title: 'Amount Type',
      dataIndex: 'amountType',
      key: 'amountType',
    },
    {
      title: 'Status',
      dataIndex: 'isActive',
      key: 'isActive',
      render: (isActive: boolean) => (
        <Switch checked={isActive} disabled />
      ),
    },
  ];

  return (
    <Card title="Auto-Collection">
      <Button type="primary" style={{ marginBottom: 16 }}>
        Create Rule
      </Button>
      <Table
        columns={columns}
        dataSource={rules}
        loading={loading}
        rowKey="id"
      />
    </Card>
  );
}

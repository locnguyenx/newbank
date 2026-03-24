import { useState, useEffect } from 'react';
import { Table, Button, Card, Tag, message } from 'antd';
import type { ColumnsType } from 'antd/es/table';

interface BatchPayment {
  id: number;
  batchReference: string;
  customerId: number;
  status: string;
  fileFormat: string;
  totalAmount: string;
  currency: string;
  instructionCount: number;
}

export default function BatchPaymentPage() {
  const [batches, setBatches] = useState<BatchPayment[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadBatches();
  }, []);

  const loadBatches = async () => {
    try {
      setLoading(true);
      const response = await fetch('/api/cash-management/batch-payments');
      const result = await response.json();
      if (result.success) {
        setBatches(result.data);
      }
    } catch (error) {
      message.error('Failed to load batch payments');
    } finally {
      setLoading(false);
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'PENDING':
        return 'orange';
      case 'PROCESSING':
        return 'blue';
      case 'COMPLETED':
        return 'green';
      case 'FAILED':
        return 'red';
      default:
        return 'default';
    }
  };

  const columns: ColumnsType<BatchPayment> = [
    {
      title: 'Batch Reference',
      dataIndex: 'batchReference',
      key: 'batchReference',
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      render: (status: string) => (
        <Tag color={getStatusColor(status)}>{status}</Tag>
      ),
    },
    {
      title: 'File Format',
      dataIndex: 'fileFormat',
      key: 'fileFormat',
    },
    {
      title: 'Instructions',
      dataIndex: 'instructionCount',
      key: 'instructionCount',
    },
    {
      title: 'Amount',
      dataIndex: 'totalAmount',
      key: 'totalAmount',
      render: (amount: string, record: BatchPayment) =>
        `${record.currency} ${amount}`,
    },
  ];

  return (
    <Card title="Batch Payments">
      <Button type="primary" style={{ marginBottom: 16 }}>
        Create Batch Payment
      </Button>
      <Table
        columns={columns}
        dataSource={batches}
        loading={loading}
        rowKey="id"
      />
    </Card>
  );
}

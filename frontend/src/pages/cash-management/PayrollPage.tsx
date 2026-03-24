import { useState, useEffect } from 'react';
import { Table, Button, Card, message } from 'antd';
import type { ColumnsType } from 'antd/es/table';

interface PayrollBatch {
  id: number;
  batchReference: string;
  customerId: number;
  status: string;
  recordCount: number;
  totalAmount: string;
  currency: string;
}

export default function PayrollPage() {
  const [batches, setBatches] = useState<PayrollBatch[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadBatches();
  }, []);

  const loadBatches = async () => {
    try {
      setLoading(true);
      const response = await fetch('/api/cash-management/payroll/batches');
      const result = await response.json();
      if (result.success) {
        setBatches(result.data);
      }
    } catch (error) {
      message.error('Failed to load payroll batches');
    } finally {
      setLoading(false);
    }
  };

  const columns: ColumnsType<PayrollBatch> = [
    {
      title: 'Batch Reference',
      dataIndex: 'batchReference',
      key: 'batchReference',
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
    },
    {
      title: 'Records',
      dataIndex: 'recordCount',
      key: 'recordCount',
    },
    {
      title: 'Amount',
      dataIndex: 'totalAmount',
      key: 'totalAmount',
      render: (amount: string, record: PayrollBatch) => 
        `${record.currency} ${amount}`,
    },
  ];

  return (
    <Card title="Payroll Management">
      <Button type="primary" style={{ marginBottom: 16 }}>
        Create Payroll Batch
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

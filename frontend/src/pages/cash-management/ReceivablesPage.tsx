import { useState, useEffect } from 'react';
import { Table, Button, Card, Tag, message } from 'antd';
import type { ColumnsType } from 'antd/es/table';

interface ReceivableInvoice {
  id: number;
  invoiceNumber: string;
  customerId: number;
  amount: string;
  currency: string;
  status: string;
  dueDate: string;
}

export default function ReceivablesPage() {
  const [invoices, setInvoices] = useState<ReceivableInvoice[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadInvoices();
  }, []);

  const loadInvoices = async () => {
    try {
      setLoading(true);
      const response = await fetch('/api/cash-management/receivables/invoices');
      const result = await response.json();
      if (result.success) {
        setInvoices(result.data);
      }
    } catch (error) {
      message.error('Failed to load invoices');
    } finally {
      setLoading(false);
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'PAID':
        return 'green';
      case 'OUTSTANDING':
        return 'orange';
      case 'OVERDUE':
        return 'red';
      default:
        return 'default';
    }
  };

  const columns: ColumnsType<ReceivableInvoice> = [
    {
      title: 'Invoice Number',
      dataIndex: 'invoiceNumber',
      key: 'invoiceNumber',
    },
    {
      title: 'Amount',
      dataIndex: 'amount',
      key: 'amount',
      render: (amount: string, record: ReceivableInvoice) => 
        `${record.currency} ${amount}`,
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
      title: 'Due Date',
      dataIndex: 'dueDate',
      key: 'dueDate',
    },
  ];

  return (
    <Card title="Receivables Management">
      <Button type="primary" style={{ marginBottom: 16 }}>
        Create Invoice
      </Button>
      <Table
        columns={columns}
        dataSource={invoices}
        loading={loading}
        rowKey="id"
      />
    </Card>
  );
}

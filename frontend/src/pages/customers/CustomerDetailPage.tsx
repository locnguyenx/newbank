import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Tabs, Card, Descriptions, Button, Space, Spin, message, Table, Tag } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import { ArrowLeftOutlined, EditOutlined } from '@ant-design/icons';
import { Eye } from 'lucide-react';
import { useAppDispatch, useAppSelector } from '@/hooks/useRedux';
import { fetchCustomerById } from '@/store/slices/customerSlice';
import { fetchAccounts } from '@/store/slices/accountSlice';
import type { CustomerVariant } from '@/types';

function IndividualCustomerDetails({ customer }: { customer: any }) {
  return (
    <Descriptions column={2} bordered>
      <Descriptions.Item label="Name">{customer.name}</Descriptions.Item>
      <Descriptions.Item label="Tax ID">{customer.taxId || '-'}</Descriptions.Item>
      <Descriptions.Item label="Date of Birth">{customer.dateOfBirth || '-'}</Descriptions.Item>
      <Descriptions.Item label="Nationality">{customer.nationality || '-'}</Descriptions.Item>
      <Descriptions.Item label="Email" span={2}>{customer.emails?.join(', ') || '-'}</Descriptions.Item>
      <Descriptions.Item label="Phone" span={2}>{customer.phones?.map((p: any) => `${p.countryCode || '+1'} ${p.number}`).join(', ') || '-'}</Descriptions.Item>
    </Descriptions>
  );
}

function SMECustomerDetails({ customer }: { customer: any }) {
  return (
    <Descriptions column={2} bordered>
      <Descriptions.Item label="Company Name" span={2}>{customer.name}</Descriptions.Item>
      <Descriptions.Item label="Registration Number">{customer.registrationNumber || customer.taxId || '-'}</Descriptions.Item>
      <Descriptions.Item label="Tax ID">{customer.taxId || '-'}</Descriptions.Item>
      <Descriptions.Item label="Industry">{customer.industry || '-'}</Descriptions.Item>
      <Descriptions.Item label="Employee Count">{customer.employeeCount || '-'}</Descriptions.Item>
      <Descriptions.Item label="Annual Turnover">
        {customer.annualTurnoverCurrency} {customer.annualTurnoverAmount?.toLocaleString() || '-'}
      </Descriptions.Item>
      <Descriptions.Item label="Years in Operation">{customer.yearsInOperation || '-'}</Descriptions.Item>
      <Descriptions.Item label="Email" span={2}>{customer.emails?.join(', ') || '-'}</Descriptions.Item>
      <Descriptions.Item label="Phone" span={2}>{customer.phones?.map((p: any) => `${p.countryCode || '+1'} ${p.number}`).join(', ') || '-'}</Descriptions.Item>
    </Descriptions>
  );
}

function CorporateCustomerDetails({ customer }: { customer: any }) {
  return (
    <Descriptions column={2} bordered>
      <Descriptions.Item label="Company Name" span={2}>{customer.name}</Descriptions.Item>
      <Descriptions.Item label="Registration Number">{customer.registrationNumber || customer.taxId || '-'}</Descriptions.Item>
      <Descriptions.Item label="Tax ID">{customer.taxId || '-'}</Descriptions.Item>
      <Descriptions.Item label="Industry">{customer.industry || '-'}</Descriptions.Item>
      <Descriptions.Item label="Employee Count">{customer.employeeCount || '-'}</Descriptions.Item>
      <Descriptions.Item label="Annual Revenue">
        {customer.annualRevenueCurrency} {customer.annualRevenueAmount?.toLocaleString() || '-'}
      </Descriptions.Item>
      <Descriptions.Item label="Website">{customer.website || '-'}</Descriptions.Item>
      <Descriptions.Item label="Email" span={2}>{customer.emails?.join(', ') || '-'}</Descriptions.Item>
      <Descriptions.Item label="Phone" span={2}>{customer.phones?.map((p: any) => `${p.countryCode || '+1'} ${p.number}`).join(', ') || '-'}</Descriptions.Item>
    </Descriptions>
  );
}

function CustomerDetails({ customer }: { customer: any }) {
  if (customer.type === 'INDIVIDUAL') {
    return <IndividualCustomerDetails customer={customer} />;
  }
  if (customer.type === 'SME') {
    return <SMECustomerDetails customer={customer} />;
  }
  return <CorporateCustomerDetails customer={customer} />;
}

export function CustomerDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { selectedCustomer: customer, loading, error } = useAppSelector((state) => state.customer);
  const { accounts } = useAppSelector((state) => state.account);
  const [accountsLoading, setAccountsLoading] = useState(false);

  useEffect(() => {
    if (id) {
      dispatch(fetchCustomerById(parseInt(id, 10)));
      setAccountsLoading(true);
      dispatch(fetchAccounts({ customerId: parseInt(id, 10) })).finally(() => setAccountsLoading(false));
    }
  }, [dispatch, id]);

  const getStatusColor = (status: string) => {
    const colorMap: Record<string, string> = {
      PENDING: 'orange',
      ACTIVE: 'green',
      SUSPENDED: 'red',
      CLOSED: 'gray',
      FROZEN: 'orange',
      DORMANT: 'default',
    };
    return colorMap[status] || 'default';
  };

  const accountColumns: ColumnsType<any> = [
    { title: 'Account Number', dataIndex: 'accountNumber', key: 'accountNumber' },
    { title: 'Type', dataIndex: 'type', key: 'type', render: (type: string) => <Tag>{type}</Tag> },
    { title: 'Status', dataIndex: 'status', key: 'status', render: (status: string) => <Tag color={getStatusColor(status)}>{status}</Tag> },
    { title: 'Balance', dataIndex: 'balance', key: 'balance', render: (balance: number, record: any) => `${balance?.toLocaleString() || 0} ${record.currency || ''}` },
    {
      title: '',
      key: 'action',
      render: (_, record) => (
        <Button type="link" size="small" icon={<Eye size={14} />} onClick={() => navigate(`/accounts/${record.accountNumber}`)}>
          View
        </Button>
      ),
    },
  ];

  if (loading) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', padding: 100 }}>
        <Spin size="large" />
      </div>
    );
  }

  if (error) {
    message.error(error);
    return null;
  }

  if (!customer) {
    return null;
  }

  const items = [
    { key: 'details', label: 'Details', children: <CustomerDetails customer={customer} /> },
    {
      key: 'accounts',
      label: 'Accounts',
      children: accountsLoading ? (
        <div style={{ textAlign: 'center', padding: 40 }}><Spin /></div>
      ) : accounts.length > 0 ? (
        <Table columns={accountColumns} dataSource={accounts} rowKey="id" pagination={false} style={{ marginTop: 8 }} />
      ) : (
        <div style={{ padding: 20, textAlign: 'center', color: '#999' }}>No accounts found</div>
      ),
    },
    { key: 'documents', label: 'Documents', children: <div style={{ padding: 20, textAlign: 'center', color: '#999' }}>No documents found</div> },
    {
      key: 'audit',
      label: 'Audit Trail',
      children: (
        <Descriptions column={2} bordered>
          <Descriptions.Item label="Created By">{customer.createdBy}</Descriptions.Item>
          <Descriptions.Item label="Created At">{customer.createdAt ? new Date(customer.createdAt).toLocaleString() : '-'}</Descriptions.Item>
          <Descriptions.Item label="Updated By">{customer.updatedBy}</Descriptions.Item>
          <Descriptions.Item label="Updated At">{customer.updatedAt ? new Date(customer.updatedAt).toLocaleString() : '-'}</Descriptions.Item>
        </Descriptions>
      ),
    },
  ];

  return (
    <div style={{ padding: 24 }}>
      <Space style={{ marginBottom: 16 }}>
        <Button icon={<ArrowLeftOutlined />} onClick={() => navigate('/customers')}>Back</Button>
      </Space>

      <Card
        title={`Customer: ${customer.customerNumber}`}
        extra={<Button type="primary" icon={<EditOutlined />} onClick={() => navigate(`/customers/${customer.id}/edit`)}>Edit</Button>}
      >
        <Descriptions column={3} style={{ marginBottom: 24 }}>
          <Descriptions.Item label="Customer Number">{customer.customerNumber}</Descriptions.Item>
          <Descriptions.Item label="Type">{customer.type}</Descriptions.Item>
          <Descriptions.Item label="Status"><span style={{ color: getStatusColor(customer.status) }}>{customer.status}</span></Descriptions.Item>
        </Descriptions>
        <Tabs items={items} />
      </Card>
    </div>
  );
}

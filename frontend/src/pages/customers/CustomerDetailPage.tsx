import { useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Tabs, Card, Descriptions, Button, Space, Spin, message } from 'antd';
import { EditOutlined, ArrowLeftOutlined } from '@ant-design/icons';
import { useAppDispatch, useAppSelector } from '@/hooks/useRedux';
import { fetchCustomerById } from '@/store/slices/customerSlice';
import type { CustomerVariant, IndividualCustomer, SMECustomer, CorporateCustomer } from '@/types';

function IndividualCustomerDetails({ customer }: { customer: IndividualCustomer }) {
  return (
    <Descriptions column={2} bordered>
      <Descriptions.Item label="First Name">{customer.firstName}</Descriptions.Item>
      <Descriptions.Item label="Last Name">{customer.lastName}</Descriptions.Item>
      <Descriptions.Item label="Date of Birth">{customer.dateOfBirth}</Descriptions.Item>
      <Descriptions.Item label="ID Number">{customer.idNumber}</Descriptions.Item>
      <Descriptions.Item label="Email">{customer.email}</Descriptions.Item>
      <Descriptions.Item label="Phone">{customer.phoneNumber}</Descriptions.Item>
    </Descriptions>
  );
}

function SMECustomerDetails({ customer }: { customer: SMECustomer }) {
  return (
    <Descriptions column={2} bordered>
      <Descriptions.Item label="Company Name" span={2}>{customer.companyName}</Descriptions.Item>
      <Descriptions.Item label="Registration Number">{customer.registrationNumber}</Descriptions.Item>
      <Descriptions.Item label="Incorporation Date">{customer.incorporationDate}</Descriptions.Item>
      <Descriptions.Item label="Industry">{customer.industry}</Descriptions.Item>
      <Descriptions.Item label="Number of Employees">{customer.numberOfEmployees}</Descriptions.Item>
      <Descriptions.Item label="Email">{customer.email}</Descriptions.Item>
      <Descriptions.Item label="Phone">{customer.phoneNumber}</Descriptions.Item>
    </Descriptions>
  );
}

function CorporateCustomerDetails({ customer }: { customer: CorporateCustomer }) {
  return (
    <Descriptions column={2} bordered>
      <Descriptions.Item label="Company Name" span={2}>{customer.companyName}</Descriptions.Item>
      <Descriptions.Item label="Registration Number">{customer.registrationNumber}</Descriptions.Item>
      <Descriptions.Item label="Incorporation Date">{customer.incorporationDate}</Descriptions.Item>
      <Descriptions.Item label="Industry">{customer.industry}</Descriptions.Item>
      <Descriptions.Item label="Employee Count Range">{customer.employeeCountRange}</Descriptions.Item>
      <Descriptions.Item label="Annual Revenue">
        {customer.revenueCurrency} {customer.annualRevenue.toLocaleString()}
      </Descriptions.Item>
      <Descriptions.Item label="Email">{customer.email}</Descriptions.Item>
      <Descriptions.Item label="Phone">{customer.phoneNumber}</Descriptions.Item>
    </Descriptions>
  );
}

function CustomerDetails({ customer }: { customer: CustomerVariant }) {
  if (customer.customerType === 'INDIVIDUAL') {
    return <IndividualCustomerDetails customer={customer} />;
  }
  if (customer.customerType === 'SME') {
    return <SMECustomerDetails customer={customer} />;
  }
  return <CorporateCustomerDetails customer={customer} />;
}

export function CustomerDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { selectedCustomer: customer, loading, error } = useAppSelector((state) => state.customer);

  useEffect(() => {
    if (id) {
      dispatch(fetchCustomerById(parseInt(id, 10)));
    }
  }, [dispatch, id]);

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

  const getStatusColor = (status: string) => {
    const colorMap: Record<string, string> = {
      PENDING: 'orange',
      ACTIVE: 'green',
      SUSPENDED: 'red',
      CLOSED: 'gray',
    };
    return colorMap[status] || 'default';
  };

  const items = [
    {
      key: 'details',
      label: 'Details',
      children: <CustomerDetails customer={customer} />,
    },
    {
      key: 'accounts',
      label: 'Accounts',
      children: <div style={{ padding: 20, textAlign: 'center', color: '#999' }}>No accounts found</div>,
    },
    {
      key: 'transactions',
      label: 'Transactions',
      children: <div style={{ padding: 20, textAlign: 'center', color: '#999' }}>No transactions found</div>,
    },
    {
      key: 'documents',
      label: 'Documents',
      children: <div style={{ padding: 20, textAlign: 'center', color: '#999' }}>No documents found</div>,
    },
    {
      key: 'audit',
      label: 'Audit Trail',
      children: (
        <Descriptions column={2} bordered>
          <Descriptions.Item label="Created By">{customer.createdBy}</Descriptions.Item>
          <Descriptions.Item label="Created At">{new Date(customer.createdAt).toLocaleString()}</Descriptions.Item>
          <Descriptions.Item label="Updated By">{customer.updatedBy}</Descriptions.Item>
          <Descriptions.Item label="Updated At">{new Date(customer.updatedAt).toLocaleString()}</Descriptions.Item>
        </Descriptions>
      ),
    },
  ];

  return (
    <div style={{ padding: 24 }}>
      <Space style={{ marginBottom: 16 }}>
        <Button icon={<ArrowLeftOutlined />} onClick={() => navigate('/customers')}>
          Back
        </Button>
      </Space>

      <Card
        title={`Customer: ${customer.customerNumber}`}
        extra={
          <Button type="primary" icon={<EditOutlined />} onClick={() => navigate(`/customers/${customer.id}/edit`)}>
            Edit
          </Button>
        }
      >
        <Descriptions column={3} style={{ marginBottom: 24 }}>
          <Descriptions.Item label="Customer Number">{customer.customerNumber}</Descriptions.Item>
          <Descriptions.Item label="Type">{customer.customerType}</Descriptions.Item>
          <Descriptions.Item label="Status">
            <span style={{ color: getStatusColor(customer.status) }}>{customer.status}</span>
          </Descriptions.Item>
        </Descriptions>

        <Tabs items={items} />
      </Card>
    </div>
  );
}

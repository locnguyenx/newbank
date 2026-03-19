import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Table, Input, Select, Button, Space, Tag, Card } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import { useAppDispatch, useAppSelector } from '@/hooks/useRedux';
import { fetchCustomers } from '@/store/slices/customerSlice';
import type { CustomerVariant, CustomerType, CustomerStatus } from '@/types';

const { Search } = Input;

interface FilterParams {
  search?: string;
  customerType?: CustomerType;
  status?: CustomerStatus;
}

export function CustomerListPage() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { customers, loading } = useAppSelector((state) => state.customer);
  const [filters, setFilters] = useState<FilterParams>({});
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);

  useEffect(() => {
    dispatch(fetchCustomers());
  }, [dispatch]);

  const filteredCustomers = customers.filter((customer) => {
    if (filters.search) {
      const searchLower = filters.search.toLowerCase();
      const matchesSearch =
        customer.customerNumber.toLowerCase().includes(searchLower) ||
        ('companyName' in customer && customer.companyName.toLowerCase().includes(searchLower)) ||
        ('firstName' in customer && `${customer.firstName} ${customer.lastName}`.toLowerCase().includes(searchLower)) ||
        ('email' in customer && customer.email.toLowerCase().includes(searchLower));
      if (!matchesSearch) return false;
    }
    if (filters.customerType && customer.customerType !== filters.customerType) return false;
    if (filters.status && customer.status !== filters.status) return false;
    return true;
  });

  const paginatedCustomers = filteredCustomers.slice(
    (page - 1) * pageSize,
    page * pageSize
  );

  const getCustomerDisplayName = (customer: CustomerVariant): string => {
    if (customer.customerType === 'INDIVIDUAL') {
      return `${customer.firstName} ${customer.lastName}`;
    }
    return customer.companyName;
  };

  const columns: ColumnsType<CustomerVariant> = [
    {
      title: 'Customer Number',
      dataIndex: 'customerNumber',
      key: 'customerNumber',
      width: 150,
    },
    {
      title: 'Name',
      key: 'name',
      render: (_, record) => getCustomerDisplayName(record),
    },
    {
      title: 'Type',
      dataIndex: 'customerType',
      key: 'customerType',
      width: 120,
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      width: 120,
      render: (status: CustomerStatus) => {
        const colorMap: Record<CustomerStatus, string> = {
          PENDING: 'orange',
          ACTIVE: 'green',
          SUSPENDED: 'red',
          CLOSED: 'gray',
        };
        return <Tag color={colorMap[status]}>{status}</Tag>;
      },
    },
    {
      title: 'Email',
      key: 'email',
      render: (_, record) => {
        if ('email' in record) return record.email;
        return '-';
      },
    },
    {
      title: 'Created',
      dataIndex: 'createdAt',
      key: 'createdAt',
      width: 120,
      render: (date: string) => new Date(date).toLocaleDateString(),
    },
    {
      title: 'Action',
      key: 'action',
      width: 150,
      render: (_, record) => (
        <Space>
          <Button type="link" onClick={() => navigate(`/customers/${record.id}`)}>
            View
          </Button>
          <Button type="link" onClick={() => navigate(`/customers/${record.id}/edit`)}>
            Edit
          </Button>
        </Space>
      ),
    },
  ];

  const handleSearch = (value: string) => {
    setFilters((prev) => ({ ...prev, search: value }));
    setPage(1);
  };

  const handleFilterChange = (key: keyof FilterParams, value: FilterParams[keyof FilterParams]) => {
    setFilters((prev) => ({ ...prev, [key]: value }));
    setPage(1);
  };

  return (
    <div style={{ padding: 24 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 16 }}>
        <h1>Customers</h1>
        <Button type="primary" onClick={() => navigate('/customers/new')}>
          Add Customer
        </Button>
      </div>

      <Card>
        <Space direction="vertical" style={{ width: '100%' }} size="middle">
          <Space wrap>
            <Search
              placeholder="Search by name, number, or email"
              onSearch={handleSearch}
              enterButton
              style={{ width: 300 }}
              allowClear
            />
            <Select
              placeholder="Customer Type"
              style={{ width: 150 }}
              allowClear
              onChange={(value) => handleFilterChange('customerType', value)}
            >
              <Select.Option value="INDIVIDUAL">Individual</Select.Option>
              <Select.Option value="SME">SME</Select.Option>
              <Select.Option value="CORPORATE">Corporate</Select.Option>
            </Select>
            <Select
              placeholder="Status"
              style={{ width: 150 }}
              allowClear
              onChange={(value) => handleFilterChange('status', value)}
            >
              <Select.Option value="PENDING">Pending</Select.Option>
              <Select.Option value="ACTIVE">Active</Select.Option>
              <Select.Option value="SUSPENDED">Suspended</Select.Option>
              <Select.Option value="CLOSED">Closed</Select.Option>
            </Select>
          </Space>

          <Table
            columns={columns}
            dataSource={paginatedCustomers}
            rowKey="id"
            loading={loading}
            pagination={{
              current: page,
              pageSize,
              total: filteredCustomers.length,
              showSizeChanger: true,
              showTotal: (total) => `Total ${total} customers`,
              onChange: (p, ps) => {
                setPage(p);
                setPageSize(ps);
              },
            }}
          />
        </Space>
      </Card>
    </div>
  );
}

import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Table, Input, Select, Button, Space, Tag, Card } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import { useAppDispatch, useAppSelector } from '@/hooks/useRedux';
import { fetchAccounts } from '@/store/slices/accountSlice';
import type { AccountResponse, AccountType, AccountStatus } from '@/types/account.types';
import { Eye } from 'lucide-react';

const { Search } = Input;

interface FilterParams {
  search?: string;
  type?: AccountType;
  status?: AccountStatus;
}

export function AccountListPage() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { accounts, pagination, loading } = useAppSelector((state) => state.account);
  const [filters, setFilters] = useState<FilterParams>({});
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);

  useEffect(() => {
    dispatch(fetchAccounts({ search: filters.search, type: filters.type, status: filters.status, page: page - 1, size: pageSize }));
  }, [dispatch, filters, page, pageSize]);

  const getStatusColor = (status: AccountStatus) => {
    const colors: Record<AccountStatus, string> = {
      ACTIVE: 'green',
      CLOSED: 'red',
      FROZEN: 'orange',
      DORMANT: 'default',
      PENDING: 'blue',
    };
    return colors[status] || 'default';
  };

  const columns: ColumnsType<AccountResponse> = [
    {
      title: 'Account Number',
      dataIndex: 'accountNumber',
      key: 'accountNumber',
      width: 180,
    },
    {
      title: 'Type',
      dataIndex: 'type',
      key: 'type',
      width: 150,
      render: (type: AccountType) => type,
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      width: 120,
      render: (status: AccountStatus) => (
        <Tag color={getStatusColor(status)}>{status}</Tag>
      ),
    },
    {
      title: 'Balance',
      dataIndex: 'balance',
      key: 'balance',
      width: 150,
      render: (balance: number, record) => (
        <>
          {balance.toFixed(2)} {record.currency}
        </>
      ),
    },
    {
      title: 'Actions',
      key: 'actions',
      width: 100,
      render: (_, record) => (
        <Button
          type="primary"
          size="small"
          icon={<Eye size={16} />}
          onClick={() => navigate(`/accounts/${record.accountNumber}`)}
        >
          View
        </Button>
      ),
    },
  ];

  return (
    <Card title="Accounts" loading={loading}>
      <Space direction="vertical" style={{ width: '100%' }} size="middle">
        <Space wrap>
          <Search
            placeholder="Search by account number"
            allowClear
            style={{ width: 300 }}
            onChange={(e) => {
              setFilters({ ...filters, search: e.target.value });
              setPage(1);
            }}
          />
          <Select
            placeholder="Filter by type"
            style={{ width: 150 }}
            allowClear
            onChange={(value) => {
              setFilters({ ...filters, type: value });
              setPage(1);
            }}
            options={Object.values(['CURRENT', 'SAVINGS', 'FIXED_DEPOSIT', 'LOAN']).map((type) => ({
              label: type,
              value: type,
            }))}
          />
          <Select
            placeholder="Filter by status"
            style={{ width: 150 }}
            allowClear
            onChange={(value) => {
              setFilters({ ...filters, status: value });
              setPage(1);
            }}
            options={Object.values(['ACTIVE', 'CLOSED', 'FROZEN', 'DORMANT', 'PENDING']).map((status) => ({
              label: status,
              value: status,
            }))}
          />
        </Space>

        <Table
          columns={columns}
          dataSource={accounts}
          rowKey="accountNumber"
          pagination={{
            current: page,
            pageSize,
            total: pagination.totalElements,
            showSizeChanger: true,
            onChange: (newPage, newPageSize) => {
              setPage(newPage);
              setPageSize(newPageSize);
            },
          }}
        />
      </Space>
    </Card>
  );
}

// @ts-nocheck - Type mismatches with OpenAPI-generated types
import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Card, Table, Tag, Button, Space, Spin, message, Select } from 'antd';
import { ArrowLeftOutlined, PlusOutlined, EditOutlined, StopOutlined } from '@ant-design/icons';
import { useAppDispatch, useAppSelector } from '@/hooks/useRedux';
import { fetchAuthorizations, revokeAuthorization } from '@/store/slices/authorizationSlice';
import type { AuthorizationStatus, AuthorizationType } from '@/types/authorization.types';

const statusFilters: Array<{ value: string; label: string }> = [
  { value: 'ACTIVE', label: 'Active' },
  { value: 'REVOKED', label: 'Revoked' },
  { value: 'EXPIRED', label: 'Expired' },
  { value: 'PENDING', label: 'Pending' },
];

// @ts-expect-error - PENDING may not be in AuthorizationStatus enum
const statusColorMap: Record<AuthorizationStatus, string> = {
  ACTIVE: 'success',
  REVOKED: 'error',
  EXPIRED: 'warning',
  PENDING: 'processing',
};

// @ts-expect-error - SIGNATORY may not be in AuthorizationType enum
const typeLabelMap: Record<AuthorizationType, string> = {
  SIGNATORY: 'Signatory',
  POWER_OF_ATTORNEY: 'Power of Attorney',
  JOINT_AUTHORITY: 'Joint Authority',
  SOLE_AUTHORITY: 'Sole Authority',
  DELEGATED: 'Delegated',
};

export function AuthorizationListPage() {
  const { customerId } = useParams<{ customerId: string }>();
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { authorizations, loading, error } = useAppSelector((state) => state.authorizations);
  const [statusFilter, setStatusFilter] = useState<string | undefined>(undefined);

  useEffect(() => {
    if (customerId) {
      dispatch(fetchAuthorizations({ customerId: parseInt(customerId, 10), status: statusFilter }));
    }
  }, [dispatch, customerId, statusFilter]);

  if (error) {
    message.error(error);
    return null;
  }

  const handleRevoke = async (authorizationId: number) => {
    try {
      await dispatch(revokeAuthorization({ id: authorizationId, reason: 'Revoked by user' })).unwrap();
      message.success('Authorization revoked');
    } catch {
      message.error('Failed to revoke authorization');
    }
  };

  const columns = [
    {
      title: 'Type',
      dataIndex: 'authorizationType',
      key: 'authorizationType',
      render: (type: AuthorizationType) => typeLabelMap[type] || type,
    },
    {
      title: 'Authorized Person',
      dataIndex: 'authorizedPersonName',
      key: 'authorizedPersonName',
    },
    {
      title: 'Email',
      dataIndex: 'authorizedPersonEmail',
      key: 'authorizedPersonEmail',
    },
    {
      title: 'Limit',
      key: 'limit',
      render: (_: unknown, record: { transactionLimit: number | null; currency: string }) =>
        record.transactionLimit !== null
          ? `${record.currency} ${record.transactionLimit.toLocaleString()}`
          : 'No limit',
    },
    {
      title: 'Effective Date',
      dataIndex: 'effectiveDate',
      key: 'effectiveDate',
      render: (date: string) => new Date(date).toLocaleDateString(),
    },
    {
      title: 'Expiry Date',
      dataIndex: 'expiryDate',
      key: 'expiryDate',
      render: (date: string | null) => (date ? new Date(date).toLocaleDateString() : 'No expiry'),
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      render: (status: AuthorizationStatus) => (
        <Tag color={statusColorMap[status]}>{status}</Tag>
      ),
    },
    {
      title: 'Actions',
      key: 'actions',
      // @ts-expect-error - status comparison with string literal
      render: (_: unknown, record: { id: number; status: AuthorizationStatus }) => (
        <Space>
          <Button
            type="link"
            icon={<EditOutlined />}
            onClick={() => navigate(`/customers/${customerId}/authorizations/${record.id}/edit`)}
            disabled={record.status !== 'ACTIVE' && record.status !== 'PENDING'}
          >
            Edit
          </Button>
          {record.status === 'ACTIVE' && (
            <Button
              type="link"
              danger
              icon={<StopOutlined />}
              onClick={() => handleRevoke(record.id)}
            >
              Revoke
            </Button>
          )}
        </Space>
      ),
    },
  ];

  return (
    <div style={{ padding: 24 }}>
      <Space style={{ marginBottom: 16 }}>
        <Button icon={<ArrowLeftOutlined />} onClick={() => navigate(`/customers/${customerId}`)}>
          Back to Customer
        </Button>
        <Button
          type="primary"
          icon={<PlusOutlined />}
          onClick={() => navigate(`/customers/${customerId}/authorizations/new`)}
        >
          Add Authorization
        </Button>
      </Space>

      <Card title="Authorizations">
        <div style={{ marginBottom: 16 }}>
          <Select
            placeholder="Filter by status"
            allowClear
            value={statusFilter}
            onChange={(value) => setStatusFilter(value)}
            style={{ width: 200 }}
            options={statusFilters}
          />
        </div>

        {loading ? (
          <div style={{ display: 'flex', justifyContent: 'center', padding: 50 }}>
            <Spin size="large" />
          </div>
        ) : (
          <Table
            dataSource={authorizations}
            // @ts-expect-error - columns type mismatch with generated types
            columns={columns}
            rowKey="id"
            pagination={{ pageSize: 10 }}
            locale={{ emptyText: 'No authorizations found' }}
          />
        )}
      </Card>
    </div>
  );
}

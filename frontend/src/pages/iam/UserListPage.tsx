import React, { useEffect, useState } from 'react';
import { Table, Button, Space, Tag, Card, Input, Select, message, DatePicker } from 'antd';
import { PlusOutlined, EditOutlined, StopOutlined, CheckOutlined, UserOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { fetchUsers, activateUser, deactivateUser } from '@/store/slices/usersSlice';
import type { RootState } from '@/store';
import type { User } from '@/services/userService';

const UserListPage: React.FC = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { users, loading } = useSelector((state: RootState) => state.users);
  const [searchText, setSearchText] = useState('');
  const [roleFilter, setRoleFilter] = useState<string | null>(null);

  useEffect(() => {
    dispatch(fetchUsers() as any);
  }, [dispatch]);

  const handleActivate = async (id: number) => {
    try {
      await dispatch(activateUser(id) as any);
      message.success('User activated');
      dispatch(fetchUsers() as any);
    } catch {
      message.error('Failed to activate user');
    }
  };

  const handleDeactivate = async (id: number) => {
    try {
      await dispatch(deactivateUser(id) as any);
      message.success('User deactivated');
      dispatch(fetchUsers() as any);
    } catch {
      message.error('Failed to deactivate user');
    }
  };

  const filteredUsers = users.filter((user) => {
    const matchesSearch = user.email.toLowerCase().includes(searchText.toLowerCase());
    const matchesRole = roleFilter ? user.role === roleFilter : true;
    return matchesSearch && matchesRole;
  });

  const columns = [
    {
      title: 'Email',
      dataIndex: 'email',
      key: 'email',
    },
    {
      title: 'Role',
      dataIndex: 'role',
      key: 'role',
      render: (role: string) => <Tag color="blue">{role || 'N/A'}</Tag>,
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      render: (status: string) => (
        <Tag color={status === 'ACTIVE' ? 'green' : 'red'}>{status}</Tag>
      ),
    },
    {
      title: 'MFA Enabled',
      dataIndex: 'mfaEnabled',
      key: 'mfaEnabled',
      render: (enabled: boolean) => (
        <Tag color={enabled ? 'blue' : 'default'}>{enabled ? 'Yes' : 'No'}</Tag>
      ),
    },
    {
      title: 'Created',
      dataIndex: 'createdAt',
      key: 'createdAt',
      render: (date: string) => new Date(date).toLocaleDateString(),
    },
    {
      title: 'Actions',
      key: 'actions',
      render: (_: unknown, record: User) => (
        <Space>
          <Button
            type="link"
            icon={<EditOutlined />}
            onClick={() => navigate(`/iam/users/${record.id}`)}
          >
            Edit
          </Button>
          {record.status === 'ACTIVE' ? (
            <Button
              type="link"
              danger
              icon={<StopOutlined />}
              onClick={() => handleDeactivate(record.id)}
            >
              Deactivate
            </Button>
          ) : (
            <Button
              type="link"
              icon={<CheckOutlined />}
              onClick={() => handleActivate(record.id)}
            >
              Activate
            </Button>
          )}
        </Space>
      ),
    },
  ];

  return (
    <Card
      title={
        <Space>
          <UserOutlined />
          User Management
        </Space>
      }
      extra={
        <Button type="primary" icon={<PlusOutlined />} onClick={() => navigate('/iam/users/new')}>
          Add User
        </Button>
      }
    >
      <Space style={{ marginBottom: 16 }} wrap>
        <Input
          placeholder="Search users..."
          style={{ width: 200 }}
          value={searchText}
          onChange={(e) => setSearchText(e.target.value)}
          allowClear
        />
        <Select
          placeholder="Filter by role"
          style={{ width: 180 }}
          allowClear
          value={roleFilter}
          onChange={(value) => setRoleFilter(value)}
        >
          <Select.Option value="SYSTEM_ADMIN">SYSTEM_ADMIN</Select.Option>
          <Select.Option value="HO_ADMIN">HO_ADMIN</Select.Option>
          <Select.Option value="BRANCH_ADMIN">BRANCH_ADMIN</Select.Option>
          <Select.Option value="COMPANY_ADMIN">COMPANY_ADMIN</Select.Option>
          <Select.Option value="COMPANY_MAKER">COMPANY_MAKER</Select.Option>
          <Select.Option value="COMPANY_CHECKER">COMPANY_CHECKER</Select.Option>
          <Select.Option value="DEPARTMENT_VIEWER">DEPARTMENT_VIEWER</Select.Option>
          <Select.Option value="DEPARTMENT_MAKER">DEPARTMENT_MAKER</Select.Option>
          <Select.Option value="DEPARTMENT_CHECKER">DEPARTMENT_CHECKER</Select.Option>
        </Select>
      </Space>
      <Table
        columns={columns}
        dataSource={filteredUsers}
        rowKey="id"
        loading={loading}
      />
    </Card>
  );
};

export default UserListPage;

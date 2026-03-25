import React, { useEffect, useState } from 'react';
import { Table, Button, Space, Tag, Card, Input, message } from 'antd';
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

  const filteredUsers = users.filter((user) =>
    user.email.toLowerCase().includes(searchText.toLowerCase())
  );

  const columns = [
    {
      title: 'Email',
      dataIndex: 'email',
      key: 'email',
    },
    {
      title: 'User Type',
      dataIndex: 'userType',
      key: 'userType',
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
      <Input
        placeholder="Search users..."
        style={{ marginBottom: 16 }}
        value={searchText}
        onChange={(e) => setSearchText(e.target.value)}
        allowClear
      />
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

import React, { useEffect, useState } from 'react';
import { Table, Button, Space, Tag, Card, message } from 'antd';
import { PlusOutlined, StopOutlined, CheckOutlined, EditOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import { userService, User } from '@/services/userService';

const CompanyUserListPage: React.FC = () => {
  const navigate = useNavigate();
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    setLoading(true);
    try {
      const data = await userService.getUsers();
      setUsers(data);
    } catch {
      message.error('Failed to fetch users');
    } finally {
      setLoading(false);
    }
  };

  const handleDeactivate = async (id: number) => {
    try {
      await userService.deactivateUser(id);
      message.success('User deactivated');
      fetchUsers();
    } catch {
      message.error('Failed to deactivate user');
    }
  };

  const handleActivate = async (id: number) => {
    try {
      await userService.activateUser(id);
      message.success('User activated');
      fetchUsers();
    } catch {
      message.error('Failed to activate user');
    }
  };

  const columns = [
    { title: 'Email', dataIndex: 'email', key: 'email' },
    { title: 'User Type', dataIndex: 'userType', key: 'userType' },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      render: (status: string) => <Tag color={status === 'ACTIVE' ? 'green' : 'red'}>{status}</Tag>,
    },
    {
      title: 'Actions',
      key: 'actions',
      render: (_: unknown, record: User) => (
        <Space>
          <Button type="link" icon={<EditOutlined />} onClick={() => navigate(`/company/users/${record.id}`)}>
            Edit
          </Button>
          {record.status === 'ACTIVE' ? (
            <Button type="link" danger icon={<StopOutlined />} onClick={() => handleDeactivate(record.id)}>
              Deactivate
            </Button>
          ) : (
            <Button type="link" icon={<CheckOutlined />} onClick={() => handleActivate(record.id)}>
              Activate
            </Button>
          )}
        </Space>
      ),
    },
  ];

  return (
    <Card
      title="Company Users"
      extra={
        <Button type="primary" icon={<PlusOutlined />} onClick={() => navigate('/company/users/invite')}>
          Invite User
        </Button>
      }
    >
      <Table columns={columns} dataSource={users} rowKey="id" loading={loading} />
    </Card>
  );
};

export default CompanyUserListPage;

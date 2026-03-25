import React, { useEffect, useState } from 'react';
import { Table, Button, Space, Tag, Card, message } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined, EyeOutlined, TeamOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import { roleService, Role } from '@/services/roleService';

const RoleListPage: React.FC = () => {
  const navigate = useNavigate();
  const [roles, setRoles] = useState<Role[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchRoles();
  }, []);

  const fetchRoles = async () => {
    setLoading(true);
    try {
      const data = await roleService.getRoles();
      setRoles(data);
    } catch {
      message.error('Failed to fetch roles');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: number) => {
    try {
      await roleService.deleteRole(id);
      message.success('Role deleted');
      fetchRoles();
    } catch {
      message.error('Failed to delete role');
    }
  };

  const columns = [
    { title: 'Name', dataIndex: 'name', key: 'name' },
    { title: 'Description', dataIndex: 'description', key: 'description' },
    {
      title: 'Type',
      dataIndex: 'type',
      key: 'type',
      render: (type: string) => (
        <Tag color={type === 'SYSTEM' ? 'blue' : 'green'}>{type}</Tag>
      ),
    },
    {
      title: 'Permissions',
      dataIndex: 'permissions',
      key: 'permissions',
      render: (perms: string[]) => (
        <>
          {perms?.slice(0, 3).map((p) => (
            <Tag key={p} color="purple">{p}</Tag>
          ))}
          {perms?.length > 3 && <Tag>+{perms.length - 3} more</Tag>}
        </>
      ),
    },
    {
      title: 'Actions',
      key: 'actions',
      render: (_: unknown, record: Role) => (
        <Space>
          <Button type="link" icon={<EyeOutlined />} onClick={() => navigate(`/iam/roles/${record.id}`)}>
            View
          </Button>
          <Button type="link" icon={<EditOutlined />} onClick={() => navigate(`/iam/roles/${record.id}/edit`)}>
            Edit
          </Button>
          {record.type === 'CUSTOM' && (
            <Button type="link" danger icon={<DeleteOutlined />} onClick={() => handleDelete(record.id)}>
              Delete
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
          <TeamOutlined />
          Role Management
        </Space>
      }
      extra={
        <Button type="primary" icon={<PlusOutlined />} onClick={() => navigate('/iam/roles/new')}>
          Create Role
        </Button>
      }
    >
      <Table columns={columns} dataSource={roles} rowKey="id" loading={loading} />
    </Card>
  );
};

export default RoleListPage;

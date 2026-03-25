import React, { useEffect, useState } from 'react';
import { Card, Table, Tabs, Tag } from 'antd';
import { HistoryOutlined } from '@ant-design/icons';
import apiClient from '@/services/apiClient';

interface LoginHistory {
  id: number;
  userId: number;
  email: string;
  action: string;
  ipAddress: string;
  timestamp: string;
  success: boolean;
}

const ActivityPage: React.FC = () => {
  const [loginHistory, setLoginHistory] = useState<LoginHistory[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchLoginHistory();
  }, []);

  const fetchLoginHistory = async () => {
    setLoading(true);
    try {
      const response = await apiClient.get('/iam/activity/login-history', { params: { page: 0, size: 50 } });
      setLoginHistory(response.data.content || []);
    } catch {
      // Handle error
    } finally {
      setLoading(false);
    }
  };

  const loginColumns = [
    { title: 'User', dataIndex: 'email', key: 'email' },
    { title: 'IP Address', dataIndex: 'ipAddress', key: 'ipAddress' },
    { title: 'Timestamp', dataIndex: 'timestamp', key: 'timestamp', 
      render: (ts: string) => new Date(ts).toLocaleString() },
    {
      title: 'Status',
      dataIndex: 'success',
      key: 'success',
      render: (success: boolean) => (
        <Tag color={success ? 'green' : 'red'}>{success ? 'Success' : 'Failed'}</Tag>
      ),
    },
  ];

  const items = [
    {
      key: 'login-history',
      label: 'Login History',
      children: (
        <Table 
          columns={loginColumns} 
          dataSource={loginHistory} 
          rowKey="id" 
          loading={loading}
          pagination={{ pageSize: 10 }}
        />
      ),
    },
  ];

  return (
    <Card 
      title={
        <span>
          <HistoryOutlined style={{ marginRight: 8 }} />
          Activity Monitoring
        </span>
      }
    >
      <Tabs items={items} />
    </Card>
  );
};

export default ActivityPage;

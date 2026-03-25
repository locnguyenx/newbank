import React, { useEffect, useState } from 'react';
import { Card, Table, Tabs, Tag, Input, DatePicker, Space } from 'antd';
import { HistoryOutlined } from '@ant-design/icons';
import apiClient from '@/services/apiClient';
import dayjs, { Dayjs } from 'dayjs';

const { RangePicker } = DatePicker;

interface LoginHistory {
  id: number;
  userId: number;
  email: string;
  action: string;
  ipAddress: string;
  timestamp: string;
  success: boolean;
  failureReason?: string;
}

interface FailedLogin {
  id: number;
  email: string;
  ipAddress: string;
  attemptTime: string;
  reason: string;
}

interface PermissionChange {
  id: number;
  userId: number;
  email: string;
  changedBy: string;
  permission: string;
  action: string;
  timestamp: string;
}

const ActivityPage: React.FC = () => {
  const [loginHistory, setLoginHistory] = useState<LoginHistory[]>([]);
  const [failedLogins, setFailedLogins] = useState<FailedLogin[]>([]);
  const [permissionChanges, setPermissionChanges] = useState<PermissionChange[]>([]);
  const [loading, setLoading] = useState(false);
  const [userFilter, setUserFilter] = useState('');
  const [dateRange, setDateRange] = useState<[Dayjs | null, Dayjs | null]>([null, null]);

  useEffect(() => {
    fetchLoginHistory();
    fetchFailedLogins();
    fetchPermissionChanges();
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

  const fetchFailedLogins = async () => {
    try {
      const response = await apiClient.get('/iam/activity/failed-logins', { params: { page: 0, size: 50 } });
      setFailedLogins(response.data.content || []);
    } catch {
      // Handle error
    }
  };

  const fetchPermissionChanges = async () => {
    try {
      const response = await apiClient.get('/iam/activity/permission-changes', { params: { page: 0, size: 50 } });
      setPermissionChanges(response.data.content || []);
    } catch {
      // Handle error
    }
  };

  const filteredLogins = loginHistory.filter((login) => {
    const matchesUser = userFilter ? login.email.toLowerCase().includes(userFilter.toLowerCase()) : true;
    const matchesDate = dateRange[0] && dateRange[1]
      ? new Date(login.timestamp) >= dateRange[0].toDate() && new Date(login.timestamp) <= dateRange[1].toDate()
      : true;
    return matchesUser && matchesDate;
  });

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

  const failedLoginColumns = [
    { title: 'Email', dataIndex: 'email', key: 'email' },
    { title: 'IP Address', dataIndex: 'ipAddress', key: 'ipAddress' },
    { title: 'Timestamp', dataIndex: 'attemptTime', key: 'attemptTime', 
      render: (ts: string) => new Date(ts).toLocaleString() },
    { title: 'Failure Reason', dataIndex: 'reason', key: 'reason' },
  ];

  const permissionColumns = [
    { title: 'User', dataIndex: 'email', key: 'email' },
    { title: 'Changed By', dataIndex: 'changedBy', key: 'changedBy' },
    { title: 'Permission', dataIndex: 'permission', key: 'permission' },
    { title: 'Action', dataIndex: 'action', key: 'action',
      render: (action: string) => (
        <Tag color={action === 'ADDED' ? 'green' : action === 'REMOVED' ? 'red' : 'blue'}>{action}</Tag>
      ),
    },
    { title: 'Timestamp', dataIndex: 'timestamp', key: 'timestamp', 
      render: (ts: string) => new Date(ts).toLocaleString() },
  ];

  const items = [
    {
      key: 'login-history',
      label: 'Login History',
      children: (
        <>
          <Space style={{ marginBottom: 16 }} wrap>
            <Input
              placeholder="Filter by user email"
              style={{ width: 200 }}
              value={userFilter}
              onChange={(e) => setUserFilter(e.target.value)}
              allowClear
            />
            <RangePicker
              value={dateRange}
              onChange={(dates) => setDateRange(dates as [Dayjs | null, Dayjs | null])}
            />
          </Space>
          <Table 
            columns={loginColumns} 
            dataSource={filteredLogins} 
            rowKey="id" 
            loading={loading}
            pagination={{ pageSize: 10 }}
          />
        </>
      ),
    },
    {
      key: 'failed-logins',
      label: 'Failed Logins',
      children: (
        <Table 
          columns={failedLoginColumns} 
          dataSource={failedLogins} 
          rowKey="id" 
          loading={loading}
          pagination={{ pageSize: 10 }}
        />
      ),
    },
    {
      key: 'permission-changes',
      label: 'Permission Changes',
      children: (
        <Table 
          columns={permissionColumns} 
          dataSource={permissionChanges} 
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

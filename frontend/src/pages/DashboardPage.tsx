import React from 'react';
import { Card, Row, Col, Table, Statistic, Typography } from 'antd';
import { TeamOutlined, BankOutlined, CheckCircleOutlined, ClockCircleOutlined } from '@ant-design/icons';
import type { ColumnsType } from 'antd/es/table';

const { Title } = Typography;

interface RecentActivity {
  key: string;
  customer: string;
  action: string;
  time: string;
  status: string;
}

const DashboardPage: React.FC = () => {
  const activityColumns: ColumnsType<RecentActivity> = [
    { title: 'Customer', dataIndex: 'customer', key: 'customer' },
    { title: 'Action', dataIndex: 'action', key: 'action' },
    { title: 'Time', dataIndex: 'time', key: 'time' },
    { title: 'Status', dataIndex: 'status', key: 'status' },
  ];

  const activityData: RecentActivity[] = [
    { key: '1', customer: 'Acme Corporation', action: 'Corporate customer created', time: '2 min ago', status: 'Completed' },
    { key: '2', customer: 'Tech Solutions Ltd', action: 'SME customer created', time: '15 min ago', status: 'Completed' },
    { key: '3', customer: 'John Doe', action: 'Individual customer created', time: '1 hour ago', status: 'Completed' },
    { key: '4', customer: 'Global Industries', action: 'KYC review submitted', time: '2 hours ago', status: 'Pending' },
  ];

  return (
    <div>
      <Title level={3}>Dashboard</Title>

      <Row gutter={[16, 16]}>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="Total Customers"
              value={4}
              prefix={<TeamOutlined />}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="Corporate"
              value={1}
              prefix={<BankOutlined />}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="SME"
              value={1}
              prefix={<BankOutlined />}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="Individual"
              value={2}
              prefix={<TeamOutlined />}
            />
          </Card>
        </Col>
      </Row>

      <Row gutter={[16, 16]} style={{ marginTop: 16 }}>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="Active KYC"
              value={3}
              prefix={<CheckCircleOutlined style={{ color: '#52c41a' }} />}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="Pending KYC"
              value={1}
              prefix={<ClockCircleOutlined style={{ color: '#faad14' }} />}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="Employment Records"
              value={5}
              prefix={<TeamOutlined />}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card>
            <Statistic
              title="Authorizations"
              value={2}
              prefix={<CheckCircleOutlined style={{ color: '#1890ff' }} />}
            />
          </Card>
        </Col>
      </Row>

      <Card title="Recent Activity" style={{ marginTop: 16 }}>
        <Table
          columns={activityColumns}
          dataSource={activityData}
          pagination={false}
          size="middle"
        />
      </Card>
    </div>
  );
};

export default DashboardPage;

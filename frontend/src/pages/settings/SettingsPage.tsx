import React, { useState } from 'react';
import { Card, Form, Switch, Select, Button, message, Divider, Typography } from 'antd';
import { SettingOutlined, BellOutlined, GlobalOutlined, LockOutlined } from '@ant-design/icons';
import { useSelector } from 'react-redux';
import type { RootState } from '@/store';

const { Title } = Typography;

interface SettingsFormValues {
  emailNotifications: boolean;
  smsNotifications: boolean;
  pushNotifications: boolean;
  transactionAlerts: boolean;
  securityAlerts: boolean;
  weeklyReport: boolean;
  language: string;
  timezone: string;
  dateFormat: string;
}

const SettingsPage: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const { user } = useSelector((state: RootState) => state.auth);
  const [form] = Form.useForm();

  const handleSave = async (values: SettingsFormValues) => {
    setLoading(true);
    try {
      await new Promise((resolve) => setTimeout(resolve, 500));
      message.success('Settings saved successfully');
    } catch {
      message.error('Failed to save settings');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card title={<><SettingOutlined style={{ marginRight: 8 }} />Settings</>}>
      <Form
        form={form}
        layout="vertical"
        initialValues={{
          emailNotifications: true,
          smsNotifications: true,
          pushNotifications: false,
          transactionAlerts: true,
          securityAlerts: true,
          weeklyReport: false,
          language: 'en',
          timezone: 'UTC',
          dateFormat: 'YYYY-MM-DD',
        }}
        onFinish={handleSave}
      >
        <Title level={5} style={{ marginTop: 0 }}>
          <BellOutlined style={{ marginRight: 8 }} />
          Notification Preferences
        </Title>
        
        <Form.Item name="emailNotifications" valuePropName="checked">
          <Switch />
          <span style={{ marginLeft: 8 }}>Email Notifications</span>
        </Form.Item>
        
        <Form.Item name="smsNotifications" valuePropName="checked">
          <Switch />
          <span style={{ marginLeft: 8 }}>SMS Notifications</span>
        </Form.Item>
        
        <Form.Item name="pushNotifications" valuePropName="checked">
          <Switch />
          <span style={{ marginLeft: 8 }}>Push Notifications</span>
        </Form.Item>
        
        <Divider />
        
        <Title level={5}>
          <BellOutlined style={{ marginRight: 8 }} />
          Alert Types
        </Title>
        
        <Form.Item name="transactionAlerts" valuePropName="checked">
          <Switch />
          <span style={{ marginLeft: 8 }}>Transaction Alerts</span>
        </Form.Item>
        
        <Form.Item name="securityAlerts" valuePropName="checked">
          <Switch />
          <span style={{ marginLeft: 8 }}>Security Alerts</span>
        </Form.Item>
        
        <Form.Item name="weeklyReport" valuePropName="checked">
          <Switch />
          <span style={{ marginLeft: 8 }}>Weekly Summary Report</span>
        </Form.Item>
        
        <Divider />
        
        <Title level={5}>
          <GlobalOutlined style={{ marginRight: 8 }} />
          Display Settings
        </Title>
        
        <Form.Item name="language" label="Language">
          <Select style={{ width: 200 }}>
            <Select.Option value="en">English</Select.Option>
            <Select.Option value="es">Spanish</Select.Option>
            <Select.Option value="fr">French</Select.Option>
            <Select.Option value="de">German</Select.Option>
          </Select>
        </Form.Item>
        
        <Form.Item name="timezone" label="Timezone">
          <Select style={{ width: 200 }}>
            <Select.Option value="UTC">UTC</Select.Option>
            <Select.Option value="America/New_York">Eastern Time (ET)</Select.Option>
            <Select.Option value="America/Chicago">Central Time (CT)</Select.Option>
            <Select.Option value="America/Denver">Mountain Time (MT)</Select.Option>
            <Select.Option value="America/Los_Angeles">Pacific Time (PT)</Select.Option>
            <Select.Option value="Europe/London">London (GMT)</Select.Option>
            <Select.Option value="Asia/Tokyo">Tokyo (JST)</Select.Option>
          </Select>
        </Form.Item>
        
        <Form.Item name="dateFormat" label="Date Format">
          <Select style={{ width: 200 }}>
            <Select.Option value="YYYY-MM-DD">YYYY-MM-DD</Select.Option>
            <Select.Option value="DD/MM/YYYY">DD/MM/YYYY</Select.Option>
            <Select.Option value="MM/DD/YYYY">MM/DD/YYYY</Select.Option>
          </Select>
        </Form.Item>
        
        <Divider />
        
        <Form.Item>
          <Button type="primary" htmlType="submit" loading={loading}>
            Save Settings
          </Button>
        </Form.Item>
      </Form>
    </Card>
  );
};

export default SettingsPage;

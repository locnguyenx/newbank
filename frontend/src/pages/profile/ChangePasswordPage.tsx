import React, { useState } from 'react';
import { Card, Form, Input, Button, message, Space } from 'antd';
import { useNavigate } from 'react-router-dom';
import { authService } from '@/services/authService';

const ChangePasswordPage: React.FC = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  const onFinish = async (values: { currentPassword: string; newPassword: string; confirmPassword: string }) => {
    if (values.newPassword !== values.confirmPassword) {
      message.error('Passwords do not match');
      return;
    }

    setLoading(true);
    try {
      await authService.changePassword({
        currentPassword: values.currentPassword,
        newPassword: values.newPassword,
      });
      message.success('Password changed successfully');
      navigate('/profile');
    } catch {
      message.error('Failed to change password');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card title="Change Password">
      <Form layout="vertical" onFinish={onFinish}>
        <Form.Item
          name="currentPassword"
          label="Current Password"
          rules={[{ required: true, message: 'Please enter current password' }]}
        >
          <Input.Password />
        </Form.Item>
        <Form.Item
          name="newPassword"
          label="New Password"
          rules={[
            { required: true, message: 'Please enter new password' },
            { min: 8, message: 'Password must be at least 8 characters' },
          ]}
        >
          <Input.Password />
        </Form.Item>
        <Form.Item
          name="confirmPassword"
          label="Confirm New Password"
          rules={[{ required: true, message: 'Please confirm new password' }]}
        >
          <Input.Password />
        </Form.Item>
        <Form.Item>
          <Space>
            <Button type="primary" htmlType="submit" loading={loading}>
              Change Password
            </Button>
            <Button onClick={() => navigate('/profile')}>Cancel</Button>
          </Space>
        </Form.Item>
      </Form>
    </Card>
  );
};

export default ChangePasswordPage;

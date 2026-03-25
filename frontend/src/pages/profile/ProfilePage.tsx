import React, { useState } from 'react';
import { Card, Descriptions, Button, Tag, Space, message, Modal, Form, Input } from 'antd';
import { UserOutlined, LockOutlined, SafetyOutlined, EditOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import type { RootState } from '@/store';
import { authService } from '@/services/authService';
import { fetchCurrentUser } from '@/store/slices/authSlice';

const ProfilePage: React.FC = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { user } = useSelector((state: RootState) => state.auth);
  const [editModalVisible, setEditModalVisible] = useState(false);
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();

  const handleDisableMfa = async () => {
    try {
      await authService.disableMfa();
      message.success('MFA disabled');
    } catch {
      message.error('Failed to disable MFA');
    }
  };

  const handleEditProfile = async () => {
    try {
      const values = await form.validateFields();
      setLoading(true);
      await authService.updateProfile({ fullName: values.fullName });
      dispatch(fetchCurrentUser() as any);
      message.success('Profile updated successfully');
      setEditModalVisible(false);
    } catch {
      message.error('Failed to update profile');
    } finally {
      setLoading(false);
    }
  };

  const openEditModal = () => {
    form.setFieldsValue({ fullName: user?.fullName || '' });
    setEditModalVisible(true);
  };

  return (
    <>
      <Card title={<><UserOutlined style={{ marginRight: 8 }} />Profile</>}>
        <Descriptions bordered column={1}>
          <Descriptions.Item label="Email">{user?.email || 'N/A'}</Descriptions.Item>
          <Descriptions.Item label="Full Name">
            {user?.fullName || 'N/A'}
            <Button type="link" icon={<EditOutlined />} onClick={openEditModal} style={{ marginLeft: 8 }}>
              Edit
            </Button>
          </Descriptions.Item>
          <Descriptions.Item label="Roles">
            {user?.roles?.map((role) => (
              <Tag key={role} color="blue">{role}</Tag>
            )) || 'N/A'}
          </Descriptions.Item>
          <Descriptions.Item label="MFA Status">
            <Tag color={user?.mfaEnabled ? 'green' : 'default'}>
              {user?.mfaEnabled ? 'Enabled' : 'Disabled'}
            </Tag>
          </Descriptions.Item>
        </Descriptions>
        <div style={{ marginTop: 24 }}>
          <Space>
            <Button 
              type="primary" 
              icon={<LockOutlined />} 
              onClick={() => navigate('/profile/change-password')}
            >
              Change Password
            </Button>
            {!user?.mfaEnabled && (
              <Button icon={<SafetyOutlined />}>Enable MFA</Button>
            )}
            {user?.mfaEnabled && (
              <Button danger onClick={handleDisableMfa}>Disable MFA</Button>
            )}
          </Space>
        </div>
      </Card>

      <Modal
        title="Edit Profile"
        open={editModalVisible}
        onOk={handleEditProfile}
        onCancel={() => setEditModalVisible(false)}
        confirmLoading={loading}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="fullName" label="Full Name" rules={[{ required: true, message: 'Please enter your full name' }]}>
            <Input placeholder="Enter your full name" />
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
};

export default ProfilePage;

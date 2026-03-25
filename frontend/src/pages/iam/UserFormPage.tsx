import React, { useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Card, Form, Input, Select, Button, Space, message } from 'antd';
import { useDispatch, useSelector } from 'react-redux';
import { createUser, fetchUser, updateUser, clearSelectedUser } from '@/store/slices/usersSlice';
import type { RootState } from '@/store';
import type { CreateUserRequest } from '@/services/userService';

const UserFormPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [form] = Form.useForm();
  const { selectedUser, loading } = useSelector((state: RootState) => state.users);
  const isEdit = !!id;

  useEffect(() => {
    if (id) {
      dispatch(fetchUser(Number(id)) as any);
    }
    return () => {
      dispatch(clearSelectedUser());
    };
  }, [id, dispatch]);

  useEffect(() => {
    if (selectedUser && isEdit) {
      form.setFieldsValue({
        email: selectedUser.email,
        userType: selectedUser.userType,
      });
    }
  }, [selectedUser, isEdit, form]);

  const onFinish = async (values: CreateUserRequest) => {
    try {
      if (isEdit) {
        await dispatch(updateUser({ id: Number(id), data: values }) as any);
        message.success('User updated');
      } else {
        await dispatch(createUser(values) as any);
        message.success('User created');
      }
      navigate('/iam/users');
    } catch {
      message.error('Operation failed');
    }
  };

  return (
    <Card title={isEdit ? 'Edit User' : 'Create User'}>
      <Form form={form} layout="vertical" onFinish={onFinish}>
        <Form.Item
          name="email"
          label="Email"
          rules={[{ required: true, type: 'email' }]}
        >
          <Input />
        </Form.Item>

        {!isEdit && (
          <Form.Item
            name="password"
            label="Password"
            rules={[{ required: true, min: 8 }]}
          >
            <Input.Password />
          </Form.Item>
        )}

        <Form.Item name="userType" label="User Type" rules={[{ required: true }]}>
          <Select>
            <Select.Option value="INTERNAL">Internal</Select.Option>
            <Select.Option value="EXTERNAL">External</Select.Option>
          </Select>
        </Form.Item>

        <Form.Item name="role" label="Role">
          <Select>
            <Select.Option value="SYSTEM_ADMIN">System Admin</Select.Option>
            <Select.Option value="HO_ADMIN">HO Admin</Select.Option>
            <Select.Option value="BRANCH_ADMIN">Branch Admin</Select.Option>
            <Select.Option value="COMPANY_ADMIN">Company Admin</Select.Option>
            <Select.Option value="COMPANY_MAKER">Company Maker</Select.Option>
            <Select.Option value="COMPANY_CHECKER">Company Checker</Select.Option>
            <Select.Option value="COMPANY_VIEWER">Company Viewer</Select.Option>
            <Select.Option value="DEPARTMENT_MAKER">Department Maker</Select.Option>
            <Select.Option value="DEPARTMENT_CHECKER">Department Checker</Select.Option>
            <Select.Option value="DEPARTMENT_VIEWER">Department Viewer</Select.Option>
          </Select>
        </Form.Item>

        <Form.Item>
          <Space>
            <Button type="primary" htmlType="submit" loading={loading}>
              {isEdit ? 'Update' : 'Create'}
            </Button>
            <Button onClick={() => navigate('/iam/users')}>Cancel</Button>
          </Space>
        </Form.Item>
      </Form>
    </Card>
  );
};

export default UserFormPage;

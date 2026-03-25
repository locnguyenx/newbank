import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Card, Form, Input, Button, message, Checkbox, Space } from 'antd';
import { roleService, CreateRoleRequest } from '@/services/roleService';

const RoleFormPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [permissions, setPermissions] = useState<string[]>([]);
  const isEdit = !!id;

  useEffect(() => {
    roleService.getPermissions().then((data) => {
      setPermissions(data.map((p: { name: string }) => p.name));
    });

    if (id) {
      roleService.getRole(Number(id)).then((role) => {
        form.setFieldsValue(role);
      });
    }
  }, [id, form]);

  const onFinish = async (values: Record<string, unknown>) => {
    setLoading(true);
    try {
      const roleData = values as unknown as CreateRoleRequest;
      if (isEdit) {
        await roleService.updateRole(Number(id), roleData);
        message.success('Role updated');
      } else {
        await roleService.createRole(roleData);
        message.success('Role created');
      }
      navigate('/iam/roles');
    } catch {
      message.error('Operation failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card title={isEdit ? 'Edit Role' : 'Create Role'}>
      <Form form={form} layout="vertical" onFinish={onFinish}>
        <Form.Item name="name" label="Role Name" rules={[{ required: true }]}>
          <Input />
        </Form.Item>
        <Form.Item name="description" label="Description">
          <Input.TextArea />
        </Form.Item>
        <Form.Item name="permissions" label="Permissions">
          <Checkbox.Group options={permissions} />
        </Form.Item>
        <Form.Item>
          <Space>
            <Button type="primary" htmlType="submit" loading={loading}>
              {isEdit ? 'Update' : 'Create'}
            </Button>
            <Button onClick={() => navigate('/iam/roles')}>Cancel</Button>
          </Space>
        </Form.Item>
      </Form>
    </Card>
  );
};

export default RoleFormPage;

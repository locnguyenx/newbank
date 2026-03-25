import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Card, Form, Input, Button, message, Checkbox, Space, Spin } from 'antd';
import { roleService, CreateRoleRequest, Permission } from '@/services/roleService';

const RoleFormPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [fetching, setFetching] = useState(false);
  const [permissions, setPermissions] = useState<Permission[]>([]);
  const isEdit = !!id;

  useEffect(() => {
    setFetching(true);
    roleService.getPermissions()
      .then((data) => {
        setPermissions(data);
      })
      .finally(() => {
        setFetching(false);
      });

    if (id) {
      setFetching(true);
      roleService.getRole(Number(id))
        .then((role) => {
          form.setFieldsValue({
            name: role.name,
            description: role.description,
            permissions: role.permissions || [],
          });
        })
        .finally(() => {
          setFetching(false);
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

  const permissionOptions = permissions.map((p) => ({
    label: p.name,
    value: p.name,
  }));

  return (
    <Spin spinning={fetching}>
      <Card title={isEdit ? 'Edit Role' : 'Create Role'}>
        <Form form={form} layout="vertical" onFinish={onFinish}>
          <Form.Item name="name" label="Role Name" rules={[{ required: true, message: 'Please enter role name' }]}>
            <Input placeholder="e.g., COMPANY_ADMIN" />
          </Form.Item>
          <Form.Item name="description" label="Description">
            <Input.TextArea rows={3} placeholder="Role description" />
          </Form.Item>
          <Form.Item name="permissions" label="Permissions">
            <Checkbox.Group options={permissionOptions} />
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
    </Spin>
  );
};

export default RoleFormPage;

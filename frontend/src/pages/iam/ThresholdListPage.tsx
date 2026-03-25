import React, { useEffect, useState } from 'react';
import { Card, Table, Button, Space, Modal, InputNumber, message, Select, Form } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import { thresholdService, Threshold, CreateThresholdRequest } from '@/services/thresholdService';

const ThresholdListPage: React.FC = () => {
  const navigate = useNavigate();
  const [thresholds, setThresholds] = useState<Threshold[]>([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [editingThreshold, setEditingThreshold] = useState<Threshold | null>(null);
  const [form] = Form.useForm();

  useEffect(() => {
    fetchThresholds();
  }, []);

  const fetchThresholds = async () => {
    setLoading(true);
    try {
      const data = await thresholdService.getThresholds();
      setThresholds(data);
    } catch {
      message.error('Failed to fetch thresholds');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: number) => {
    Modal.confirm({
      title: 'Delete Threshold',
      content: 'Are you sure you want to delete this threshold? This action cannot be undone.',
      okText: 'Delete',
      okType: 'danger',
      cancelText: 'Cancel',
      onOk: async () => {
        try {
          await thresholdService.deleteThreshold(id);
          message.success('Threshold deleted');
          fetchThresholds();
        } catch {
          message.error('Failed to delete threshold');
        }
      },
    });
  };

  const handleEdit = (record: Threshold) => {
    setEditingThreshold(record);
    form.setFieldsValue(record);
    setModalVisible(true);
  };

  const handleCreate = () => {
    setEditingThreshold(null);
    form.resetFields();
    setModalVisible(true);
  };

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      if (editingThreshold) {
        await thresholdService.updateThreshold(editingThreshold.id, values.amount);
        message.success('Threshold updated');
      } else {
        await thresholdService.createThreshold(values as CreateThresholdRequest);
        message.success('Threshold created');
      }
      setModalVisible(false);
      fetchThresholds();
    } catch {
      message.error('Operation failed');
    }
  };

  const columns = [
    { title: 'Role', dataIndex: 'role', key: 'role' },
    { 
      title: 'Amount', 
      dataIndex: 'amount', 
      key: 'amount', 
      render: (amt: number, rec: Threshold) => `${rec.currency} ${amt.toLocaleString()}` 
    },
    { title: 'Type', dataIndex: 'type', key: 'type' },
    {
      title: 'Actions',
      key: 'actions',
      render: (_: unknown, record: Threshold) => (
        <Space>
          <Button type="link" icon={<EditOutlined />} onClick={() => handleEdit(record)}>
            Edit
          </Button>
          <Button type="link" danger icon={<DeleteOutlined />} onClick={() => handleDelete(record.id)}>
            Delete
          </Button>
        </Space>
      ),
    },
  ];

  return (
    <Card
      title="Approval Thresholds"
      extra={
        <Button type="primary" icon={<PlusOutlined />} onClick={handleCreate}>
          Add Threshold
        </Button>
      }
    >
      <Table columns={columns} dataSource={thresholds} rowKey="id" loading={loading} />
      
      <Modal
        title={editingThreshold ? 'Edit Threshold' : 'Create Threshold'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
      >
        <Form form={form} layout="vertical">
          {!editingThreshold && (
            <>
              <Form.Item name="role" label="Role" rules={[{ required: true }]}>
                <Select>
                  <Select.Option value="COMPANY_ADMIN">Company Admin</Select.Option>
                  <Select.Option value="COMPANY_MAKER">Company Maker</Select.Option>
                  <Select.Option value="COMPANY_CHECKER">Company Checker</Select.Option>
                </Select>
              </Form.Item>
              <Form.Item name="currency" label="Currency" rules={[{ required: true }]}>
                <Select>
                  <Select.Option value="USD">USD</Select.Option>
                  <Select.Option value="EUR">EUR</Select.Option>
                  <Select.Option value="GBP">GBP</Select.Option>
                </Select>
              </Form.Item>
              <Form.Item name="type" label="Type" rules={[{ required: true }]}>
                <Select>
                  <Select.Option value="SINGLE">Single Transaction</Select.Option>
                  <Select.Option value="DAILY">Daily Limit</Select.Option>
                </Select>
              </Form.Item>
            </>
          )}
          <Form.Item name="amount" label="Amount" rules={[{ required: true }]}>
            <InputNumber style={{ width: '100%' }} min={0} />
          </Form.Item>
        </Form>
      </Modal>
    </Card>
  );
};

export default ThresholdListPage;

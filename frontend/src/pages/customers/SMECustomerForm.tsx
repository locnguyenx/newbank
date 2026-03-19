import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Form, Input, InputNumber, Button, Card, Row, Col, message } from 'antd';
import { ArrowLeftOutlined } from '@ant-design/icons';
import { useAppDispatch, useAppSelector } from '@/hooks/useRedux';
import { fetchCustomerById, createCustomer, updateCustomer } from '@/store/slices/customerSlice';
import type { SMECustomer } from '@/types';

interface SMECustomerFormData {
  companyName: string;
  registrationNumber: string;
  incorporationDate: string;
  email: string;
  phoneNumber: string;
  industry: string;
  numberOfEmployees: number;
}

export function SMECustomerForm() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const [form] = Form.useForm<SMECustomerFormData>();
  const { selectedCustomer: customer, loading } = useAppSelector((state) => state.customer);
  const isEditing = Boolean(id);

  useEffect(() => {
    if (isEditing && id) {
      dispatch(fetchCustomerById(parseInt(id, 10)));
    }
  }, [dispatch, id, isEditing]);

  useEffect(() => {
    if (isEditing && customer && customer.customerType === 'SME') {
      const smeCustomer = customer as SMECustomer;
      form.setFieldsValue({
        companyName: smeCustomer.companyName,
        registrationNumber: smeCustomer.registrationNumber,
        incorporationDate: smeCustomer.incorporationDate,
        email: smeCustomer.email,
        phoneNumber: smeCustomer.phoneNumber,
        industry: smeCustomer.industry,
        numberOfEmployees: smeCustomer.numberOfEmployees,
      });
    }
  }, [customer, form, isEditing]);

  const handleSubmit = async (values: SMECustomerFormData) => {
    try {
      if (isEditing && id) {
        await dispatch(updateCustomer({ id: parseInt(id, 10), ...values })).unwrap();
        message.success('Customer updated successfully');
      } else {
        await dispatch(createCustomer({ ...values, customerType: 'SME' })).unwrap();
        message.success('Customer created successfully');
      }
      navigate('/customers');
    } catch {
      message.error('Failed to save customer');
    }
  };

  return (
    <div style={{ padding: 24 }}>
      <Button icon={<ArrowLeftOutlined />} onClick={() => navigate('/customers')} style={{ marginBottom: 16 }}>
        Back
      </Button>

      <Card title={isEditing ? 'Edit SME Customer' : 'New SME Customer'}>
        <Form form={form} layout="vertical" onFinish={handleSubmit}>
          <Row gutter={24}>
            <Col span={12}>
              <Form.Item
                label="Company Name"
                name="companyName"
                rules={[{ required: true, message: 'Company name is required' }]}
              >
                <Input />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                label="Registration Number"
                name="registrationNumber"
                rules={[{ required: true, message: 'Registration number is required' }]}
              >
                <Input />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={24}>
            <Col span={12}>
              <Form.Item
                label="Incorporation Date"
                name="incorporationDate"
                rules={[{ required: true, message: 'Incorporation date is required' }]}
              >
                <Input type="date" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                label="Industry"
                name="industry"
                rules={[{ required: true, message: 'Industry is required' }]}
              >
                <Input />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={24}>
            <Col span={12}>
              <Form.Item
                label="Number of Employees"
                name="numberOfEmployees"
                rules={[{ required: true, message: 'Number of employees is required' }]}
              >
                <InputNumber min={1} style={{ width: '100%' }} />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                label="Email"
                name="email"
                rules={[
                  { required: true, message: 'Email is required' },
                  { type: 'email', message: 'Invalid email format' },
                ]}
              >
                <Input />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={24}>
            <Col span={12}>
              <Form.Item
                label="Phone Number"
                name="phoneNumber"
                rules={[{ required: true, message: 'Phone number is required' }]}
              >
                <Input />
              </Form.Item>
            </Col>
          </Row>

          <Form.Item>
            <Button type="primary" htmlType="submit" loading={loading}>
              {isEditing ? 'Update' : 'Create'}
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  );
}

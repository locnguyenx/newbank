import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Form, Input, Button, Card, Row, Col, message } from 'antd';
import { ArrowLeftOutlined } from '@ant-design/icons';
import { useAppDispatch, useAppSelector } from '@/hooks/useRedux';
import { createCustomer, updateCustomer } from '@/store/slices/customerSlice';
import type { IndividualCustomer } from '@/types';

interface IndividualCustomerFormData {
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  nationality: string;
  email: string;
  phoneNumber: string;
  taxId: string;
}

export function IndividualCustomerForm() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const [form] = Form.useForm<IndividualCustomerFormData>();
  const { selectedCustomer: customer, loading } = useAppSelector((state) => state.customer);
  const isEditing = Boolean(id);

  useEffect(() => {
    if (isEditing && customer && customer.type === 'INDIVIDUAL') {
      const ind = customer as IndividualCustomer;
      const nameParts = (ind.name || '').split(' ');
      form.setFieldsValue({
        firstName: nameParts[0] || '',
        lastName: nameParts.slice(1).join(' ') || '',
        dateOfBirth: ind.dateOfBirth || '',
        nationality: ind.nationality || '',
        email: ind.emails?.[0] || '',
        phoneNumber: ind.phones?.[0]?.number || '',
        taxId: ind.taxId || '',
      });
    }
  }, [customer, form, isEditing]);

  const handleSubmit = async (values: IndividualCustomerFormData) => {
    try {
      if (isEditing && id) {
        // @ts-expect-error - Missing idNumber field in UpdateIndividualPayload
        await dispatch(updateCustomer({ id: parseInt(id, 10), ...values })).unwrap();
        message.success('Customer updated successfully');
      } else {
        // @ts-expect-error - Missing idNumber field in CreateIndividualPayload
        await dispatch(createCustomer({ ...values, customerType: 'INDIVIDUAL' })).unwrap();
        message.success('Customer created successfully');
      }
      navigate('/customers');
    } catch (err: any) {
      message.error(err?.response?.data?.message || err.message || 'Failed to save customer');
    }
  };

  return (
    <div style={{ padding: 24 }}>
      <Button icon={<ArrowLeftOutlined />} onClick={() => navigate('/customers')} style={{ marginBottom: 16 }}>
        Back
      </Button>

      <Card title={isEditing ? 'Edit Individual Customer' : 'New Individual Customer'}>
        <Form form={form} layout="vertical" onFinish={handleSubmit}>
          <Row gutter={24}>
            <Col span={12}>
              <Form.Item label="First Name" name="firstName" rules={[{ required: true, message: 'First name is required' }]}>
                <Input />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="Last Name" name="lastName" rules={[{ required: true, message: 'Last name is required' }]}>
                <Input />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={24}>
            <Col span={12}>
              <Form.Item label="Date of Birth" name="dateOfBirth">
                <Input type="date" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="Nationality" name="nationality">
                <Input />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={24}>
            <Col span={12}>
              <Form.Item label="Tax ID / ID Number" name="taxId" rules={[{ required: true, message: 'Tax ID is required' }]}>
                <Input />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="Email" name="email" rules={[{ type: 'email', message: 'Invalid email format' }]}>
                <Input />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={24}>
            <Col span={12}>
              <Form.Item label="Phone Number" name="phoneNumber">
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

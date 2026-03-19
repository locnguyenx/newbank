import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Form, Input, Button, Card, Row, Col, message } from 'antd';
import { ArrowLeftOutlined } from '@ant-design/icons';
import { useAppDispatch, useAppSelector } from '@/hooks/useRedux';
import { fetchCustomerById, createCustomer, updateCustomer } from '@/store/slices/customerSlice';
import type { IndividualCustomer } from '@/types';

interface IndividualCustomerFormData {
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  email: string;
  phoneNumber: string;
  idNumber: string;
}

export function IndividualCustomerForm() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const [form] = Form.useForm<IndividualCustomerFormData>();
  const { selectedCustomer: customer, loading } = useAppSelector((state) => state.customer);
  const isEditing = Boolean(id);

  useEffect(() => {
    if (isEditing && id) {
      dispatch(fetchCustomerById(parseInt(id, 10)));
    }
  }, [dispatch, id, isEditing]);

  useEffect(() => {
    if (isEditing && customer && customer.customerType === 'INDIVIDUAL') {
      const indCustomer = customer as IndividualCustomer;
      form.setFieldsValue({
        firstName: indCustomer.firstName,
        lastName: indCustomer.lastName,
        dateOfBirth: indCustomer.dateOfBirth,
        email: indCustomer.email,
        phoneNumber: indCustomer.phoneNumber,
        idNumber: indCustomer.idNumber,
      });
    }
  }, [customer, form, isEditing]);

  const handleSubmit = async (values: IndividualCustomerFormData) => {
    try {
      if (isEditing && id) {
        await dispatch(updateCustomer({ id: parseInt(id, 10), ...values })).unwrap();
        message.success('Customer updated successfully');
      } else {
        await dispatch(createCustomer({ ...values, customerType: 'INDIVIDUAL' })).unwrap();
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

      <Card title={isEditing ? 'Edit Individual Customer' : 'New Individual Customer'}>
        <Form form={form} layout="vertical" onFinish={handleSubmit}>
          <Row gutter={24}>
            <Col span={12}>
              <Form.Item
                label="First Name"
                name="firstName"
                rules={[{ required: true, message: 'First name is required' }]}
              >
                <Input />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                label="Last Name"
                name="lastName"
                rules={[{ required: true, message: 'Last name is required' }]}
              >
                <Input />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={24}>
            <Col span={12}>
              <Form.Item
                label="Date of Birth"
                name="dateOfBirth"
                rules={[{ required: true, message: 'Date of birth is required' }]}
              >
                <Input type="date" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                label="ID Number"
                name="idNumber"
                rules={[{ required: true, message: 'ID number is required' }]}
              >
                <Input />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={24}>
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

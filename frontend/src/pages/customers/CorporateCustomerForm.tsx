import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Form, Input, Button, Card, Row, Col, message } from 'antd';
import { ArrowLeftOutlined } from '@ant-design/icons';
import { useAppDispatch, useAppSelector } from '@/hooks/useRedux';
import { fetchCustomerById, createCustomer, updateCustomer } from '@/store/slices/customerSlice';
import type { CorporateCustomer } from '@/types';

interface CorporateCustomerFormData {
  companyName: string;
  registrationNumber: string;
  incorporationDate: string;
  email: string;
  phoneNumber: string;
  industry: string;
  employeeCountRange: string;
  annualRevenue: number;
  revenueCurrency: string;
}

export function CorporateCustomerForm() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const [form] = Form.useForm<CorporateCustomerFormData>();
  const { selectedCustomer: customer, loading } = useAppSelector((state) => state.customer);
  const isEditing = Boolean(id);

  useEffect(() => {
    if (isEditing && id) {
      dispatch(fetchCustomerById(parseInt(id, 10)));
    }
  }, [dispatch, id, isEditing]);

  useEffect(() => {
    if (isEditing && customer && customer.customerType === 'CORPORATE') {
      const corpCustomer = customer as CorporateCustomer;
      form.setFieldsValue({
        companyName: corpCustomer.companyName,
        registrationNumber: corpCustomer.registrationNumber,
        incorporationDate: corpCustomer.incorporationDate,
        email: corpCustomer.email,
        phoneNumber: corpCustomer.phoneNumber,
        industry: corpCustomer.industry,
        employeeCountRange: corpCustomer.employeeCountRange,
        annualRevenue: corpCustomer.annualRevenue,
        revenueCurrency: corpCustomer.revenueCurrency,
      });
    }
  }, [customer, form, isEditing]);

  const handleSubmit = async (values: CorporateCustomerFormData) => {
    try {
      if (isEditing && id) {
        await dispatch(updateCustomer({ id: parseInt(id, 10), ...values })).unwrap();
        message.success('Customer updated successfully');
      } else {
        await dispatch(createCustomer({ ...values, customerType: 'CORPORATE' })).unwrap();
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

      <Card title={isEditing ? 'Edit Corporate Customer' : 'New Corporate Customer'}>
        <Form
          form={form}
          layout="vertical"
          onFinish={handleSubmit}
          initialValues={{
            revenueCurrency: 'USD',
          }}
        >
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
                label="Employee Count Range"
                name="employeeCountRange"
                rules={[{ required: true, message: 'Employee count range is required' }]}
              >
                <Input />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                label="Annual Revenue"
                name="annualRevenue"
                rules={[{ required: true, message: 'Annual revenue is required' }]}
              >
                <Input type="number" />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={24}>
            <Col span={12}>
              <Form.Item label="Revenue Currency" name="revenueCurrency">
                <Input />
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

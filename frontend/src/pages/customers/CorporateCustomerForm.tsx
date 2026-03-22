import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Form, Input, Button, Card, Row, Col, InputNumber, message } from 'antd';
import { ArrowLeftOutlined } from '@ant-design/icons';
import { useAppDispatch, useAppSelector } from '@/hooks/useRedux';
import { createCustomer, updateCustomer } from '@/store/slices/customerSlice';
import type { CorporateCustomer } from '@/types';

interface CorporateCustomerFormData {
  companyName: string;
  registrationNumber: string;
  taxId: string;
  industry: string;
  employeeCount: number;
  annualRevenueAmount: number;
  annualRevenueCurrency: string;
  website: string;
  email: string;
  phoneNumber: string;
}

export function CorporateCustomerForm() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const [form] = Form.useForm<CorporateCustomerFormData>();
  const { selectedCustomer: customer, loading } = useAppSelector((state) => state.customer);
  const isEditing = Boolean(id);

  useEffect(() => {
    if (isEditing && customer && customer.type === 'CORPORATE') {
      const corp = customer as CorporateCustomer;
      form.setFieldsValue({
        companyName: corp.name || '',
        registrationNumber: corp.registrationNumber || corp.taxId || '',
        taxId: corp.taxId || '',
        industry: corp.industry || '',
        employeeCount: corp.employeeCount || 0,
        annualRevenueAmount: corp.annualRevenueAmount || 0,
        annualRevenueCurrency: corp.annualRevenueCurrency || 'USD',
        website: corp.website || '',
        email: corp.emails?.[0] || '',
        phoneNumber: corp.phones?.[0]?.number || '',
      });
    }
  }, [customer, form, isEditing]);

  const handleSubmit = async (values: CorporateCustomerFormData) => {
    try {
      if (isEditing && id) {
        // @ts-expect-error - Missing fields in UpdateCorporatePayload from OpenAPI types
        await dispatch(updateCustomer({ id: parseInt(id, 10), ...values })).unwrap();
        message.success('Customer updated successfully');
      } else {
        // @ts-expect-error - Missing fields in CreateCorporatePayload from OpenAPI types
        await dispatch(createCustomer({ ...values, customerType: 'CORPORATE' })).unwrap();
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

      <Card title={isEditing ? 'Edit Corporate Customer' : 'New Corporate Customer'}>
        <Form form={form} layout="vertical" onFinish={handleSubmit} initialValues={{ annualRevenueCurrency: 'USD' }}>
          <Row gutter={24}>
            <Col span={12}>
              <Form.Item label="Company Name" name="companyName" rules={[{ required: true, message: 'Company name is required' }]}>
                <Input />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="Registration Number" name="registrationNumber" rules={[{ required: true, message: 'Registration number is required' }]}>
                <Input />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={24}>
            <Col span={12}>
              <Form.Item label="Tax ID" name="taxId">
                <Input />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="Industry" name="industry">
                <Input />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={24}>
            <Col span={12}>
              <Form.Item label="Number of Employees" name="employeeCount">
                <InputNumber min={0} style={{ width: '100%' }} />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="Website" name="website">
                <Input />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={24}>
            <Col span={12}>
              <Form.Item label="Annual Revenue" name="annualRevenueAmount">
                <InputNumber min={0} style={{ width: '100%' }} />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="Currency" name="annualRevenueCurrency">
                <Input />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={24}>
            <Col span={12}>
              <Form.Item label="Email" name="email" rules={[{ type: 'email', message: 'Invalid email format' }]}>
                <Input />
              </Form.Item>
            </Col>
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

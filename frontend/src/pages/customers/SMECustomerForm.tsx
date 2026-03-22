import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Form, Input, Button, Card, Row, Col, InputNumber, message } from 'antd';
import { ArrowLeftOutlined } from '@ant-design/icons';
import { useAppDispatch, useAppSelector } from '@/hooks/useRedux';
import { createCustomer, updateCustomer } from '@/store/slices/customerSlice';
import type { SMECustomer } from '@/types';

interface SMECustomerFormData {
  companyName: string;
  registrationNumber: string;
  taxId: string;
  industry: string;
  employeeCount: number;
  annualTurnoverAmount: number;
  annualTurnoverCurrency: string;
  yearsInOperation: number;
  email: string;
  phoneNumber: string;
}

export function SMECustomerForm() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const [form] = Form.useForm<SMECustomerFormData>();
  const { selectedCustomer: customer, loading } = useAppSelector((state) => state.customer);
  const isEditing = Boolean(id);

  useEffect(() => {
    if (isEditing && customer && customer.type === 'SME') {
      const sme = customer as SMECustomer;
      form.setFieldsValue({
        companyName: sme.name || '',
        registrationNumber: sme.registrationNumber || sme.taxId || '',
        taxId: sme.taxId || '',
        industry: sme.industry || '',
        employeeCount: sme.employeeCount || 0,
        annualTurnoverAmount: sme.annualTurnoverAmount || 0,
        annualTurnoverCurrency: sme.annualTurnoverCurrency || 'USD',
        yearsInOperation: sme.yearsInOperation || 0,
        email: sme.emails?.[0] || '',
        phoneNumber: sme.phones?.[0]?.number || '',
      });
    }
  }, [customer, form, isEditing]);

  const handleSubmit = async (values: SMECustomerFormData) => {
    try {
      if (isEditing && id) {
        // @ts-expect-error - Missing fields in UpdateSMEPayload from OpenAPI types
        await dispatch(updateCustomer({ id: parseInt(id, 10), ...values })).unwrap();
        message.success('Customer updated successfully');
      } else {
        // @ts-expect-error - Missing fields in CreateSMEPayload from OpenAPI types
        await dispatch(createCustomer({ ...values, customerType: 'SME' })).unwrap();
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

      <Card title={isEditing ? 'Edit SME Customer' : 'New SME Customer'}>
        <Form form={form} layout="vertical" onFinish={handleSubmit} initialValues={{ annualTurnoverCurrency: 'USD' }}>
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
              <Form.Item label="Years in Operation" name="yearsInOperation">
                <InputNumber min={0} style={{ width: '100%' }} />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={24}>
            <Col span={12}>
              <Form.Item label="Annual Turnover" name="annualTurnoverAmount">
                <InputNumber min={0} style={{ width: '100%' }} />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="Currency" name="annualTurnoverCurrency">
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

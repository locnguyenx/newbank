import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Card, Form, Input, Select, Button, Space, message } from 'antd';
import { useAppDispatch } from '@/hooks/useRedux';
import { openAccount } from '@/store/slices/accountSlice';
import type { AccountType, Currency, AccountHolderRole } from '@/types/account.types';
import { Plus } from 'lucide-react';

export function AccountOpeningForm() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const [holders, setHolders] = useState<{ customerId: number; role: AccountHolderRole }[]>([]);
  const [newHolderId, setNewHolderId] = useState<number>();
  const [newHolderRole, setNewHolderRole] = useState<AccountHolderRole>();

  const onFinish = async (values: {
    customerId: number;
    productCode: string;
    type: AccountType;
    currency: Currency;
    initialDeposit: number;
  }) => {
    try {
      await dispatch(openAccount({
        ...values,
        holders,
      })).unwrap();
      message.success('Account opened successfully');
      navigate('/accounts');
    } catch (error) {
      message.error('Failed to open account');
    }
  };

  const addHolder = () => {
    if (newHolderId && newHolderRole) {
      setHolders([...holders, { customerId: newHolderId, role: newHolderRole }]);
      setNewHolderId(undefined);
      setNewHolderRole(undefined);
    }
  };

  const removeHolder = (index: number) => {
    setHolders(holders.filter((_, i) => i !== index));
  };

  return (
    <Card title="Open New Account" style={{ maxWidth: 800, margin: '0 auto' }}>
      <Form
        form={form}
        layout="vertical"
        onFinish={onFinish}
        initialValues={{
          type: 'CURRENT',
          currency: 'USD',
          initialDeposit: 0,
        }}
      >
        <Form.Item
          name="customerId"
          label="Customer ID"
          rules={[{ required: true, message: 'Please enter customer ID' }]}
        >
          <Input type="number" placeholder="Enter customer ID" />
        </Form.Item>

        <Form.Item
          name="productCode"
          label="Product Code"
          rules={[{ required: true, message: 'Please enter product code' }]}
        >
          <Input placeholder="e.g., PROD-CHECKING" />
        </Form.Item>

        <Form.Item
          name="type"
          label="Account Type"
          rules={[{ required: true, message: 'Please select account type' }]}
        >
          <Select
            options={[
              { label: 'Current', value: 'CURRENT' },
              { label: 'Savings', value: 'SAVINGS' },
              { label: 'Fixed Deposit', value: 'FIXED_DEPOSIT' },
              { label: 'Loan', value: 'LOAN' },
              { label: 'Escrow', value: 'ESCROW' },
            ]}
          />
        </Form.Item>

        <Form.Item
          name="currency"
          label="Currency"
          rules={[{ required: true, message: 'Please select currency' }]}
        >
          <Select
            options={[
              { label: 'USD', value: 'USD' },
              { label: 'EUR', value: 'EUR' },
              { label: 'GBP', value: 'GBP' },
              { label: 'SGD', value: 'SGD' },
              { label: 'JPY', value: 'JPY' },
              { label: 'CAD', value: 'CAD' },
              { label: 'AUD', value: 'AUD' },
              { label: 'CHF', value: 'CHF' },
            ]}
          />
        </Form.Item>

        <Form.Item
          name="initialDeposit"
          label="Initial Deposit"
          rules={[{ required: true, message: 'Please enter initial deposit' }]}
        >
          <Input type="number" step="0.01" placeholder="0.00" />
        </Form.Item>

        <Form.Item label="Account Holders">
          <Space direction="vertical" style={{ width: '100%' }}>
            <Space wrap>
              <Input
                type="number"
                placeholder="Customer ID"
                value={newHolderId}
                onChange={(e) => setNewHolderId(Number(e.target.value))}
                style={{ width: 150 }}
              />
              <Select
                placeholder="Role"
                value={newHolderRole}
                onChange={setNewHolderRole}
                style={{ width: 200 }}
                options={[
                  { label: 'PRIMARY', value: 'PRIMARY' },
                  { label: 'JOINT', value: 'JOINT' },
                  { label: 'AUTHORIZED_SIGNATORY', value: 'AUTHORIZED_SIGNATORY' },
                  { label: 'NOMINEE', value: 'NOMINEE' },
                ]}
              />
              <Button type="dashed" onClick={addHolder} icon={<Plus size={16} />}>
                Add Holder
              </Button>
            </Space>
            <ul>
              {holders.map((holder, index) => (
                <li key={index}>
                  Customer {holder.customerId} - {holder.role}
                  <Button
                    type="link"
                    size="small"
                    danger
                    onClick={() => removeHolder(index)}
                  >
                    Remove
                  </Button>
                </li>
              ))}
            </ul>
          </Space>
        </Form.Item>

        <Form.Item>
          <Space>
            <Button type="primary" htmlType="submit">
              Open Account
            </Button>
            <Button onClick={() => navigate('/accounts')}>Cancel</Button>
          </Space>
        </Form.Item>
      </Form>
    </Card>
  );
}

import { useEffect, useState } from 'react';
import { Table, Button, Tabs, Card, message, Tag, Modal, Form, Input, Select, InputNumber } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import { PlusOutlined } from '@ant-design/icons';
import { useAppDispatch, useAppSelector } from '@/hooks/useRedux';
import {
  fetchChargeDefinitions,
  createChargeDefinition,
  activateCharge,
  deactivateCharge,
  fetchWaivers,
  createWaiver,
  removeWaiver,
  calculateCharge,
  clearError,
} from '@/store/slices/chargeSlice';
import type { ChargeDefinition, FeeWaiver, CreateChargeDefinitionRequest, CreateFeeWaiverRequest, ChargeCalculationRequest } from '@/types/charge.types';
import { ChargeType, WaiverScope } from '@/types/charge.types';

const { TabPane } = Tabs;

const chargeColumns: ColumnsType<ChargeDefinition> = [
  { title: 'ID', dataIndex: 'id', key: 'id', width: 60 },
  { title: 'Name', dataIndex: 'name', key: 'name' },
  { title: 'Type', dataIndex: 'chargeType', key: 'chargeType', width: 150 },
  { title: 'Currency', dataIndex: 'currency', key: 'currency', width: 80 },
  {
    title: 'Status',
    dataIndex: 'status',
    key: 'status',
    width: 100,
    render: (status: string) => (
      <Tag color={status === 'ACTIVE' ? 'green' : 'red'}>{status}</Tag>
    ),
  },
  { title: 'Created', dataIndex: 'createdAt', key: 'createdAt', width: 150, render: (v: string) => v ? new Date(v).toLocaleDateString() : '-' },
];

const waiverColumns: ColumnsType<FeeWaiver> = [
  { title: 'ID', dataIndex: 'id', key: 'id', width: 60 },
  { title: 'Charge', dataIndex: 'chargeName', key: 'chargeName' },
  { title: 'Scope', dataIndex: 'scope', key: 'scope', width: 100 },
  { title: 'Reference', dataIndex: 'referenceId', key: 'referenceId', width: 100 },
  { title: 'Waiver %', dataIndex: 'waiverPercentage', key: 'waiverPercentage', width: 80 },
  { title: 'Valid From', dataIndex: 'validFrom', key: 'validFrom', width: 100 },
  { title: 'Valid To', dataIndex: 'validTo', key: 'validTo', width: 100 },
];

export function ChargeListPage() {
  const dispatch = useAppDispatch();
  const { charges, waivers, calculationResult, loading, error } = useAppSelector((state) => state.charges);
  const [activeTab, setActiveTab] = useState('charges');
  const [activeSubTab, setActiveSubTab] = useState('rules');
  const [createChargeModalVisible, setCreateChargeModalVisible] = useState(false);
  const [createWaiverModalVisible, setCreateWaiverModalVisible] = useState(false);
  const [form] = Form.useForm();
  const [waiverForm] = Form.useForm();
  const [calculatorForm] = Form.useForm();

  useEffect(() => {
    dispatch(fetchChargeDefinitions({}));
    dispatch(fetchWaivers({}));
  }, [dispatch]);

  useEffect(() => {
    if (error) {
      message.error(error);
      dispatch(clearError());
    }
  }, [error, dispatch]);

  const handleCreateCharge = async () => {
    try {
      const values = await form.validateFields();
      await dispatch(createChargeDefinition(values as CreateChargeDefinitionRequest)).unwrap();
      message.success('Charge created successfully');
      setCreateChargeModalVisible(false);
      form.resetFields();
      dispatch(fetchChargeDefinitions({}));
    } catch {
      message.error('Failed to create charge');
    }
  };

  const handleActivate = async (id: number) => {
    try {
      await dispatch(activateCharge(id)).unwrap();
      message.success('Charge activated');
      dispatch(fetchChargeDefinitions({}));
    } catch {
      message.error('Failed to activate charge');
    }
  };

  const handleDeactivate = async (id: number) => {
    try {
      await dispatch(deactivateCharge(id)).unwrap();
      message.success('Charge deactivated');
      dispatch(fetchChargeDefinitions({}));
    } catch {
      message.error('Failed to deactivate charge');
    }
  };

  const handleCreateWaiver = async () => {
    try {
      const values = await waiverForm.validateFields();
      await dispatch(createWaiver(values as CreateFeeWaiverRequest)).unwrap();
      message.success('Waiver created successfully');
      setCreateWaiverModalVisible(false);
      waiverForm.resetFields();
      dispatch(fetchWaivers({}));
    } catch {
      message.error('Failed to create waiver');
    }
  };

  const handleDeleteWaiver = async (id: number) => {
    try {
      await dispatch(removeWaiver(id)).unwrap();
      message.success('Waiver deleted');
      dispatch(fetchWaivers({}));
    } catch {
      message.error('Failed to delete waiver');
    }
  };

  const handleCalculate = async () => {
    try {
      const values = await calculatorForm.validateFields();
      const request: ChargeCalculationRequest = {
        productCode: values.productCode,
        chargeType: values.chargeType,
        transactionAmount: values.transactionAmount,
        customerId: values.customerId,
        currency: values.currency,
      };
      await dispatch(calculateCharge(request)).unwrap();
    } catch {
      message.error('Failed to calculate charge');
    }
  };

  const chargeTab = (
    <Card
      extra={
        <Button type="primary" icon={<PlusOutlined />} onClick={() => setCreateChargeModalVisible(true)}>
          Create Charge
        </Button>
      }
    >
      <Table
        columns={[
          ...chargeColumns,
          {
            title: 'Actions',
            key: 'actions',
            width: 150,
            render: (_, record) => (
              <>
                {record.status === 'ACTIVE' ? (
                  <Button size="small" onClick={() => handleDeactivate(record.id)}>Deactivate</Button>
                ) : (
                  <Button size="small" type="primary" onClick={() => handleActivate(record.id)}>Activate</Button>
                )}
              </>
            ),
          },
        ]}
        dataSource={charges}
        rowKey="id"
        loading={loading}
        scroll={{ x: 'max-content' }}
      />
    </Card>
  );

  const rulesTab = (
    <Tabs activeKey={activeSubTab} onChange={setActiveSubTab}>
      <TabPane tab="Rules" key="rules">
        <Card>
          <p>Charge rules will be displayed here.</p>
        </Card>
      </TabPane>
      <TabPane tab="Product Charges" key="product-charges">
        <Card>
          <Button type="primary" icon={<PlusOutlined />} style={{ marginBottom: 16 }}>
            Assign Product Charge
          </Button>
          <p>Product charges will be displayed here.</p>
        </Card>
      </TabPane>
      <TabPane tab="Customer Overrides" key="customer-overrides">
        <Card>
          <Button type="primary" icon={<PlusOutlined />} style={{ marginBottom: 16 }}>
            Assign Customer Override
          </Button>
          <p>Customer overrides will be displayed here.</p>
        </Card>
      </TabPane>
    </Tabs>
  );

  const waiversTab = (
    <Card
      extra={
        <Button type="primary" icon={<PlusOutlined />} onClick={() => setCreateWaiverModalVisible(true)}>
          Create Waiver
        </Button>
      }
    >
      <Table
        columns={[
          ...waiverColumns,
          {
            title: 'Actions',
            key: 'actions',
            width: 100,
            render: (_, record) => (
              <Button size="small" danger onClick={() => handleDeleteWaiver(record.id)}>Delete</Button>
            ),
          },
        ]}
        dataSource={waivers}
        rowKey="id"
        loading={loading}
        scroll={{ x: 'max-content' }}
      />
    </Card>
  );

  const calculatorTab = (
    <Card>
      <Form form={calculatorForm} layout="vertical" style={{ maxWidth: 500 }}>
        <Form.Item name="productCode" label="Product Code" rules={[{ required: true }]}>
          <Input />
        </Form.Item>
        <Form.Item name="chargeType" label="Charge Type" rules={[{ required: true }]}>
          <Select>
            {Object.values(ChargeType).map((type) => (
              <Select.Option key={type} value={type}>{type}</Select.Option>
            ))}
          </Select>
        </Form.Item>
        <Form.Item name="transactionAmount" label="Transaction Amount" rules={[{ required: true }]}>
          <InputNumber style={{ width: '100%' }} />
        </Form.Item>
        <Form.Item name="customerId" label="Customer ID (optional)">
          <InputNumber style={{ width: '100%' }} />
        </Form.Item>
        <Form.Item name="currency" label="Currency" rules={[{ required: true }]}>
          <Select>
            <Select.Option value="USD">USD</Select.Option>
            <Select.Option value="EUR">EUR</Select.Option>
            <Select.Option value="GBP">GBP</Select.Option>
          </Select>
        </Form.Item>
        <Button type="primary" onClick={handleCalculate} loading={loading}>
          Calculate
        </Button>
      </Form>
      
      {calculationResult && (
        <div style={{ marginTop: 24 }}>
          <h3>Result</h3>
          <p><strong>Base Amount:</strong> {calculationResult.baseAmount}</p>
          <p><strong>Waiver Amount:</strong> {calculationResult.waiverAmount}</p>
          <p><strong>Final Amount:</strong> {calculationResult.finalAmount}</p>
          <p>
            <strong>Waiver Applied:</strong>{' '}
            <Tag color={calculationResult.waiverApplied ? 'green' : 'red'}>
              {calculationResult.waiverApplied ? 'YES' : 'NO'}
            </Tag>
          </p>
        </div>
      )}
    </Card>
  );

  const tabItems = [
    { key: 'charges', label: 'Charges', children: chargeTab },
    { key: 'rules', label: 'Rules & Assignments', children: rulesTab },
    { key: 'waivers', label: 'Fee Waivers', children: waiversTab },
    { key: 'calculator', label: 'Calculator', children: calculatorTab },
  ];

  return (
    <div style={{ padding: 24 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 16 }}>
        <h1>Charges Management</h1>
      </div>

      <Tabs activeKey={activeTab} onChange={setActiveTab} items={tabItems} />

      <Modal
        title="Create Charge"
        open={createChargeModalVisible}
        onOk={handleCreateCharge}
        onCancel={() => setCreateChargeModalVisible(false)}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="name" label="Name" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="chargeType" label="Charge Type" rules={[{ required: true }]}>
            <Select>
              {Object.values(ChargeType).map((type) => (
                <Select.Option key={type} value={type}>{type}</Select.Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item name="currency" label="Currency" rules={[{ required: true }]}>
            <Select>
              <Select.Option value="USD">USD</Select.Option>
              <Select.Option value="EUR">EUR</Select.Option>
              <Select.Option value="GBP">GBP</Select.Option>
            </Select>
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="Create Waiver"
        open={createWaiverModalVisible}
        onOk={handleCreateWaiver}
        onCancel={() => setCreateWaiverModalVisible(false)}
      >
        <Form form={waiverForm} layout="vertical">
          <Form.Item name="chargeId" label="Charge ID" rules={[{ required: true }]}>
            <InputNumber style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="scope" label="Scope" rules={[{ required: true }]}>
            <Select>
              {Object.values(WaiverScope).map((scope) => (
                <Select.Option key={scope} value={scope}>{scope}</Select.Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item name="referenceId" label="Reference ID" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="waiverPercentage" label="Waiver Percentage" rules={[{ required: true }]}>
            <InputNumber min={0} max={100} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="validFrom" label="Valid From" rules={[{ required: true }]}>
            <Input type="date" />
          </Form.Item>
          <Form.Item name="validTo" label="Valid To">
            <Input type="date" />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}
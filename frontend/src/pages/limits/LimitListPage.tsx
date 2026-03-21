import { useEffect, useState } from 'react';
import { Table, Button, Tabs, Card, Tag, Modal, Form, Input, InputNumber, Select, message } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import { PlusOutlined } from '@ant-design/icons';
import { useAppDispatch, useAppSelector } from '@/hooks/useRedux';
import {
  fetchLimitDefinitions,
  createLimitDefinition,
  activateLimit,
  deactivateLimit,
  assignToProduct,
  assignToCustomer,
  assignToAccount,
  checkLimit,
  fetchPendingApprovals,
  approveApproval,
  rejectApproval,
  clearError,
} from '@/store/slices/limitSlice';
import type {
  LimitDefinition,
  ProductLimit,
  CustomerLimit,
  AccountLimit,
  ApprovalRequest,
  CreateLimitDefinitionRequest,
  AssignLimitRequest,
} from '@/types/limit.types';
import {
  LimitType,
  LimitStatus,
  LimitCheckResult,
  ApprovalStatus,
} from '@/types/limit.types';

const limitTypeOptions = Object.values(LimitType).map((v) => ({ label: v, value: v }));

const definitionColumns: ColumnsType<LimitDefinition> = [
  { title: 'ID', dataIndex: 'id', key: 'id', width: 80 },
  { title: 'Name', dataIndex: 'name', key: 'name' },
  { title: 'Type', dataIndex: 'limitType', key: 'limitType', width: 150 },
  { title: 'Amount', dataIndex: 'amount', key: 'amount', width: 150 },
  { title: 'Currency', dataIndex: 'currency', key: 'currency', width: 100 },
  {
    title: 'Status',
    dataIndex: 'status',
    key: 'status',
    width: 100,
    render: (status: LimitStatus) => (
      <Tag color={status === LimitStatus.ACTIVE ? 'green' : 'red'}>{status}</Tag>
    ),
  },
  { title: 'Created At', dataIndex: 'createdAt', key: 'createdAt', width: 180 },
];

const productLimitColumns: ColumnsType<ProductLimit> = [
  { title: 'ID', dataIndex: 'id', key: 'id', width: 80 },
  { title: 'Limit Name', dataIndex: 'limitName', key: 'limitName' },
  { title: 'Product Code', dataIndex: 'productCode', key: 'productCode', width: 150 },
  { title: 'Override Amount', dataIndex: 'overrideAmount', key: 'overrideAmount', width: 150 },
  { title: 'Created At', dataIndex: 'createdAt', key: 'createdAt', width: 180 },
];

const customerLimitColumns: ColumnsType<CustomerLimit> = [
  { title: 'ID', dataIndex: 'id', key: 'id', width: 80 },
  { title: 'Limit Name', dataIndex: 'limitName', key: 'limitName' },
  { title: 'Customer ID', dataIndex: 'customerId', key: 'customerId', width: 120 },
  { title: 'Override Amount', dataIndex: 'overrideAmount', key: 'overrideAmount', width: 150 },
  { title: 'Created At', dataIndex: 'createdAt', key: 'createdAt', width: 180 },
];

const accountLimitColumns: ColumnsType<AccountLimit> = [
  { title: 'ID', dataIndex: 'id', key: 'id', width: 80 },
  { title: 'Limit Name', dataIndex: 'limitName', key: 'limitName' },
  { title: 'Account Number', dataIndex: 'accountNumber', key: 'accountNumber', width: 180 },
  { title: 'Override Amount', dataIndex: 'overrideAmount', key: 'overrideAmount', width: 150 },
  { title: 'Created At', dataIndex: 'createdAt', key: 'createdAt', width: 180 },
];

const approvalColumns: ColumnsType<ApprovalRequest> = [
  { title: 'ID', dataIndex: 'id', key: 'id', width: 80 },
  { title: 'Transaction Ref', dataIndex: 'transactionReference', key: 'transactionReference', width: 180 },
  { title: 'Amount', dataIndex: 'amount', key: 'amount', width: 150 },
  { title: 'Currency', dataIndex: 'currency', key: 'currency', width: 100 },
  { title: 'Account', dataIndex: 'accountNumber', key: 'accountNumber', width: 180 },
  {
    title: 'Status',
    dataIndex: 'status',
    key: 'status',
    width: 120,
    render: (status: ApprovalStatus) => {
      const color = status === ApprovalStatus.PENDING ? 'orange' : status === ApprovalStatus.APPROVED ? 'green' : 'red';
      return <Tag color={color}>{status}</Tag>;
    },
  },
  { title: 'Created At', dataIndex: 'createdAt', key: 'createdAt', width: 180 },
];

export function LimitListPage() {
  const dispatch = useAppDispatch();
  const { limits, productLimits, customerLimits, accountLimits, approvals, loading, error, lastCheckResult } = useAppSelector(
    (state) => state.limits
  );

  const [activeTab, setActiveTab] = useState('definitions');
  const [assignmentSubTab, setAssignmentSubTab] = useState('product');
  const [createModalOpen, setCreateModalOpen] = useState(false);
  const [assignModalOpen, setAssignModalOpen] = useState(false);
  const [rejectModalOpen, setRejectModalOpen] = useState(false);
  const [selectedApprovalId, setSelectedApprovalId] = useState<number | null>(null);
  const [checkForm] = Form.useForm();
  const [createForm] = Form.useForm();
  const [assignForm] = Form.useForm();
  const [rejectForm] = Form.useForm();

  useEffect(() => {
    dispatch(fetchLimitDefinitions(undefined));
    dispatch(fetchPendingApprovals({}));
  }, [dispatch]);

  useEffect(() => {
    if (error) {
      message.error(error);
      dispatch(clearError());
    }
  }, [error, dispatch]);

  const handleCreateLimit = async () => {
    try {
      const values = await createForm.validateFields();
      await dispatch(createLimitDefinition(values as CreateLimitDefinitionRequest)).unwrap();
      message.success('Limit definition created');
      setCreateModalOpen(false);
      createForm.resetFields();
    } catch {
      // validation failed
    }
  };

  const handleActivate = async (id: number) => {
    try {
      await dispatch(activateLimit(id)).unwrap();
      message.success('Limit activated');
    } catch {
      // handled by error effect
    }
  };

  const handleDeactivate = async (id: number) => {
    try {
      await dispatch(deactivateLimit(id)).unwrap();
      message.success('Limit deactivated');
    } catch {
      // handled by error effect
    }
  };

  const handleAssign = async () => {
    try {
      const values = await assignForm.validateFields();
      const data: AssignLimitRequest = {
        limitId: values.limitId,
        referenceId: values.referenceId,
        overrideAmount: values.overrideAmount,
      };
      if (assignmentSubTab === 'product') {
        await dispatch(assignToProduct(data)).unwrap();
      } else if (assignmentSubTab === 'customer') {
        await dispatch(assignToCustomer(data)).unwrap();
      } else {
        await dispatch(assignToAccount(data)).unwrap();
      }
      message.success('Limit assigned');
      setAssignModalOpen(false);
      assignForm.resetFields();
    } catch {
      // validation failed
    }
  };

  const handleCheckLimit = async () => {
    try {
      const values = await checkForm.validateFields();
      await dispatch(checkLimit(values)).unwrap();
    } catch {
      // validation failed
    }
  };

  const handleApprove = async (id: number) => {
    try {
      await dispatch(approveApproval(id)).unwrap();
      message.success('Approval granted');
    } catch {
      // handled by error effect
    }
  };

  const handleRejectClick = (id: number) => {
    setSelectedApprovalId(id);
    setRejectModalOpen(true);
  };

  const handleReject = async () => {
    if (selectedApprovalId === null) return;
    try {
      const values = await rejectForm.validateFields();
      await dispatch(rejectApproval({ id: selectedApprovalId, reason: values.reason })).unwrap();
      message.success('Approval rejected');
      setRejectModalOpen(false);
      setSelectedApprovalId(null);
      rejectForm.resetFields();
    } catch {
      // validation failed
    }
  };

  const renderCheckResult = () => {
    if (!lastCheckResult) return null;
    const { result, effectiveLimit, currentUsage, remainingAmount, rejectionReason } = lastCheckResult;
    const color = result === LimitCheckResult.ALLOWED ? 'green' : result === LimitCheckResult.REJECTED ? 'red' : 'orange';
    return (
      <Card style={{ marginTop: 16 }}>
        <Tag color={color} style={{ marginBottom: 8 }}>{result}</Tag>
        <p><strong>Effective Limit:</strong> {effectiveLimit}</p>
        <p><strong>Current Usage:</strong> {currentUsage}</p>
        <p><strong>Remaining Amount:</strong> {remainingAmount}</p>
        {rejectionReason && <p style={{ color: 'red' }}><strong>Reason:</strong> {rejectionReason}</p>}
      </Card>
    );
  };

  const definitionColumnsWithActions: ColumnsType<LimitDefinition> = [
    ...definitionColumns,
    {
      title: 'Actions',
      key: 'actions',
      width: 180,
      render: (_: unknown, record: LimitDefinition) => (
        <>
          {record.status === LimitStatus.INACTIVE ? (
            <Button size="small" type="primary" onClick={() => handleActivate(record.id)}>Activate</Button>
          ) : (
            <Button size="small" danger onClick={() => handleDeactivate(record.id)}>Deactivate</Button>
          )}
        </>
      ),
    },
  ];

  const approvalColumnsWithActions: ColumnsType<ApprovalRequest> = [
    ...approvalColumns,
    {
      title: 'Actions',
      key: 'actions',
      width: 200,
      render: (_: unknown, record: ApprovalRequest) => (
        <>
          <Button size="small" type="primary" onClick={() => handleApprove(record.id)} style={{ marginRight: 8 }}>Approve</Button>
          <Button size="small" danger onClick={() => handleRejectClick(record.id)}>Reject</Button>
        </>
      ),
    },
  ];

  const tabItems = [
    {
      key: 'definitions',
      label: 'Limit Definitions',
      children: (
        <Card
          extra={
            <Button type="primary" icon={<PlusOutlined />} onClick={() => setCreateModalOpen(true)}>
              Create Limit
            </Button>
          }
        >
          <Table
            columns={definitionColumnsWithActions}
            dataSource={limits}
            rowKey="id"
            loading={loading}
            scroll={{ x: 'max-content' }}
          />
        </Card>
      ),
    },
    {
      key: 'assignments',
      label: 'Assignments',
      children: (
        <Tabs
          activeKey={assignmentSubTab}
          onChange={setAssignmentSubTab}
          items={[
            {
              key: 'product',
              label: 'Product',
              children: (
                <Card
                  extra={
                    <Button type="primary" icon={<PlusOutlined />} onClick={() => setAssignModalOpen(true)}>
                      Assign
                    </Button>
                  }
                >
                  <Table
                    columns={productLimitColumns}
                    dataSource={productLimits}
                    rowKey="id"
                    loading={loading}
                    scroll={{ x: 'max-content' }}
                  />
                </Card>
              ),
            },
            {
              key: 'customer',
              label: 'Customer',
              children: (
                <Card
                  extra={
                    <Button type="primary" icon={<PlusOutlined />} onClick={() => setAssignModalOpen(true)}>
                      Assign
                    </Button>
                  }
                >
                  <Table
                    columns={customerLimitColumns}
                    dataSource={customerLimits}
                    rowKey="id"
                    loading={loading}
                    scroll={{ x: 'max-content' }}
                  />
                </Card>
              ),
            },
            {
              key: 'account',
              label: 'Account',
              children: (
                <Card
                  extra={
                    <Button type="primary" icon={<PlusOutlined />} onClick={() => setAssignModalOpen(true)}>
                      Assign
                    </Button>
                  }
                >
                  <Table
                    columns={accountLimitColumns}
                    dataSource={accountLimits}
                    rowKey="id"
                    loading={loading}
                    scroll={{ x: 'max-content' }}
                  />
                </Card>
              ),
            },
          ]}
        />
      ),
    },
    {
      key: 'check',
      label: 'Limit Check',
      children: (
        <Card>
          <Form form={checkForm} layout="vertical" style={{ maxWidth: 600 }}>
            <Form.Item name="accountNumber" label="Account Number" rules={[{ required: true }]}>
              <Input />
            </Form.Item>
            <Form.Item name="customerId" label="Customer ID">
              <InputNumber style={{ width: '100%' }} />
            </Form.Item>
            <Form.Item name="productCode" label="Product Code">
              <Input />
            </Form.Item>
            <Form.Item name="transactionAmount" label="Transaction Amount" rules={[{ required: true }]}>
              <InputNumber style={{ width: '100%' }} />
            </Form.Item>
            <Form.Item name="currency" label="Currency" rules={[{ required: true }]}>
              <Input />
            </Form.Item>
            <Form.Item name="transactionReference" label="Transaction Reference" rules={[{ required: true }]}>
              <Input />
            </Form.Item>
            <Form.Item name="limitType" label="Limit Type" rules={[{ required: true }]}>
              <Select options={limitTypeOptions} />
            </Form.Item>
            <Button type="primary" onClick={handleCheckLimit}>Check</Button>
          </Form>
          {renderCheckResult()}
        </Card>
      ),
    },
    {
      key: 'approvals',
      label: 'Approvals',
      children: (
        <Card>
          <Table
            columns={approvalColumnsWithActions}
            dataSource={approvals}
            rowKey="id"
            loading={loading}
            scroll={{ x: 'max-content' }}
          />
        </Card>
      ),
    },
  ];

  return (
    <div style={{ padding: 24 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 16 }}>
        <h1>Limits Management</h1>
      </div>

      <Tabs activeKey={activeTab} onChange={setActiveTab} items={tabItems} />

      <Modal
        title="Create Limit Definition"
        open={createModalOpen}
        onOk={handleCreateLimit}
        onCancel={() => { setCreateModalOpen(false); createForm.resetFields(); }}
        okText="Create"
      >
        <Form form={createForm} layout="vertical">
          <Form.Item name="name" label="Name" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="limitType" label="Limit Type" rules={[{ required: true }]}>
            <Select options={limitTypeOptions} />
          </Form.Item>
          <Form.Item name="amount" label="Amount" rules={[{ required: true }]}>
            <InputNumber style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="currency" label="Currency" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title={`Assign Limit - ${assignmentSubTab.charAt(0).toUpperCase() + assignmentSubTab.slice(1)}`}
        open={assignModalOpen}
        onOk={handleAssign}
        onCancel={() => { setAssignModalOpen(false); assignForm.resetFields(); }}
        okText="Assign"
      >
        <Form form={assignForm} layout="vertical">
          <Form.Item name="limitId" label="Limit ID" rules={[{ required: true }]}>
            <InputNumber style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item
            name="referenceId"
            label={
              assignmentSubTab === 'product'
                ? 'Product Code'
                : assignmentSubTab === 'customer'
                ? 'Customer ID'
                : 'Account Number'
            }
            rules={[{ required: true }]}
          >
            <Input />
          </Form.Item>
          <Form.Item name="overrideAmount" label="Override Amount (optional)">
            <InputNumber style={{ width: '100%' }} />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="Reject Approval"
        open={rejectModalOpen}
        onOk={handleReject}
        onCancel={() => { setRejectModalOpen(false); setSelectedApprovalId(null); rejectForm.resetFields(); }}
        okText="Reject"
        okButtonProps={{ danger: true }}
      >
        <Form form={rejectForm} layout="vertical">
          <Form.Item name="reason" label="Reason">
            <Input.TextArea />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}

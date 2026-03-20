import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Card, Tabs, Table, Button, Space, Tag, Modal, Form, Input, message, Row, Col, Popconfirm } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import { ArrowLeftOutlined, PlusOutlined, DeleteOutlined } from '@ant-design/icons';
import { useAppDispatch, useAppSelector } from '@/hooks/useRedux';
import {
  fetchProductById,
  submitForApproval,
  approveProduct,
  rejectProduct,
  activateProduct,
  retireProduct,
  fetchVersionHistory,
  addFeature,
  removeFeature,
  addFeeEntry,
  removeFeeEntry,
  assignSegments,
} from '@/store/slices/productSlice';
import {
  ProductStatus,
  FeeCalculationMethod,
  type ProductVersion,
  type ProductFeature,
  type ProductFeeEntry,
  type ProductFeatureRequest,
  type ProductFeeEntryRequest,
} from '@/types/product.types';

const { TabPane } = Tabs;

export function ProductDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { selectedProduct, versionHistory, features, feeEntries, segments, loading } = useAppSelector(
    (state) => state.products
  );

  const [isFeatureModalOpen, setIsFeatureModalOpen] = useState(false);
  const [isFeeModalOpen, setIsFeeModalOpen] = useState(false);
  const [isRejectModalOpen, setIsRejectModalOpen] = useState(false);
  const [rejectComment, setRejectComment] = useState('');
  const [featureForm] = Form.useForm();
  const [feeForm] = Form.useForm();

  useEffect(() => {
    if (id) {
      dispatch(fetchProductById(parseInt(id, 10)));
      dispatch(fetchVersionHistory(parseInt(id, 10)));
    }
  }, [dispatch, id]);

  const getStatusColor = (status: ProductStatus): string => {
    const colorMap: Record<ProductStatus, string> = {
      [ProductStatus.DRAFT]: 'default',
      [ProductStatus.PENDING_APPROVAL]: 'orange',
      [ProductStatus.APPROVED]: 'blue',
      [ProductStatus.ACTIVE]: 'green',
      [ProductStatus.SUPERSEDED]: 'purple',
      [ProductStatus.RETIRED]: 'red',
    };
    return colorMap[status] || 'default';
  };

  const handleSubmitForApproval = async () => {
    if (selectedProduct) {
      await dispatch(submitForApproval({ productId: selectedProduct.productId, versionId: selectedProduct.id }));
      message.success('Product submitted for approval');
      dispatch(fetchProductById(selectedProduct.productId));
    }
  };

  const handleApprove = async () => {
    if (selectedProduct) {
      await dispatch(approveProduct({ productId: selectedProduct.productId, versionId: selectedProduct.id }));
      message.success('Product approved');
      dispatch(fetchProductById(selectedProduct.productId));
    }
  };

  const handleReject = async () => {
    if (selectedProduct) {
      await dispatch(rejectProduct({ productId: selectedProduct.productId, versionId: selectedProduct.id, comment: rejectComment }));
      message.success('Product rejected');
      setIsRejectModalOpen(false);
      setRejectComment('');
      dispatch(fetchProductById(selectedProduct.productId));
    }
  };

  const handleActivate = async () => {
    if (selectedProduct) {
      await dispatch(activateProduct({ productId: selectedProduct.productId, versionId: selectedProduct.id }));
      message.success('Product activated');
      dispatch(fetchProductById(selectedProduct.productId));
    }
  };

  const handleRetire = async () => {
    if (selectedProduct) {
      await dispatch(retireProduct({ productId: selectedProduct.productId, versionId: selectedProduct.id }));
      message.success('Product retired');
      dispatch(fetchProductById(selectedProduct.productId));
    }
  };

  const handleAddFeature = async (values: ProductFeatureRequest) => {
    if (selectedProduct) {
      await dispatch(addFeature({ productId: selectedProduct.productId, versionId: selectedProduct.id, data: values }));
      message.success('Feature added');
      setIsFeatureModalOpen(false);
      featureForm.resetFields();
      dispatch(fetchProductById(selectedProduct.productId));
    }
  };

  const handleRemoveFeature = async (featureId: number) => {
    if (selectedProduct) {
      await dispatch(removeFeature({ productId: selectedProduct.productId, versionId: selectedProduct.id, featureId }));
      message.success('Feature removed');
      dispatch(fetchProductById(selectedProduct.productId));
    }
  };

  const handleAddFeeEntry = async (values: ProductFeeEntryRequest) => {
    if (selectedProduct) {
      await dispatch(addFeeEntry({ productId: selectedProduct.productId, versionId: selectedProduct.id, data: values }));
      message.success('Fee entry added');
      setIsFeeModalOpen(false);
      feeForm.resetFields();
      dispatch(fetchProductById(selectedProduct.productId));
    }
  };

  const handleRemoveFeeEntry = async (feeId: number) => {
    if (selectedProduct) {
      await dispatch(removeFeeEntry({ productId: selectedProduct.productId, versionId: selectedProduct.id, feeId }));
      message.success('Fee entry removed');
      dispatch(fetchProductById(selectedProduct.productId));
    }
  };

  const handleAssignSegments = async (newSegments: string[]) => {
    if (selectedProduct) {
      await dispatch(assignSegments({ productId: selectedProduct.productId, versionId: selectedProduct.id, segments: newSegments }));
      message.success('Segments assigned');
      dispatch(fetchProductById(selectedProduct.productId));
    }
  };

  const renderLifecycleButtons = () => {
    if (!selectedProduct) return null;
    const status = selectedProduct.status;

    return (
      <Space>
        {status === ProductStatus.DRAFT && (
          <Button type="primary" onClick={handleSubmitForApproval}>
            Submit for Approval
          </Button>
        )}
        {status === ProductStatus.PENDING_APPROVAL && (
          <>
            <Button type="primary" onClick={handleApprove}>
              Approve
            </Button>
            <Button danger onClick={() => setIsRejectModalOpen(true)}>
              Reject
            </Button>
          </>
        )}
        {status === ProductStatus.APPROVED && (
          <Button type="primary" onClick={handleActivate}>
            Activate
          </Button>
        )}
        {status === ProductStatus.ACTIVE && (
          <Popconfirm title="Are you sure you want to retire this product?" onConfirm={handleRetire}>
            <Button danger>Retire</Button>
          </Popconfirm>
        )}
      </Space>
    );
  };

  const featureColumns: ColumnsType<ProductFeature> = [
    { title: 'Key', dataIndex: 'featureKey', key: 'featureKey' },
    { title: 'Value', dataIndex: 'featureValue', key: 'featureValue' },
    {
      title: 'Action',
      key: 'action',
      width: 100,
      render: (_, record) => (
        <Button danger size="small" icon={<DeleteOutlined />} onClick={() => handleRemoveFeature(record.id)}>
          Remove
        </Button>
      ),
    },
  ];

  const feeColumns: ColumnsType<ProductFeeEntry> = [
    { title: 'Fee Type', dataIndex: 'feeType', key: 'feeType' },
    { title: 'Method', dataIndex: 'calculationMethod', key: 'calculationMethod' },
    {
      title: 'Amount',
      key: 'amount',
      render: (_, record) => {
        if (record.calculationMethod === FeeCalculationMethod.FLAT && record.amount) {
          return `${record.currency} ${record.amount}`;
        }
        if (record.calculationMethod === FeeCalculationMethod.PERCENTAGE && record.rate) {
          return `${record.rate}%`;
        }
        if (record.calculationMethod === FeeCalculationMethod.TIERED_VOLUME) {
          return 'Tiered';
        }
        return '-';
      },
    },
    {
      title: 'Action',
      key: 'action',
      width: 100,
      render: (_, record) => (
        <Button danger size="small" icon={<DeleteOutlined />} onClick={() => handleRemoveFeeEntry(record.id)}>
          Remove
        </Button>
      ),
    },
  ];

  const versionColumns: ColumnsType<ProductVersion> = [
    { title: 'Version', dataIndex: 'versionNumber', key: 'versionNumber', width: 100 },
    { title: 'Status', dataIndex: 'status', key: 'status', render: (status: ProductStatus) => <Tag color={getStatusColor(status)}>{status}</Tag> },
    { title: 'Contracts', dataIndex: 'contractCount', key: 'contractCount', width: 100 },
    { title: 'Submitted By', dataIndex: 'submittedBy', key: 'submittedBy' },
    { title: 'Approved By', dataIndex: 'approvedBy', key: 'approvedBy' },
    { title: 'Created', dataIndex: 'createdAt', key: 'createdAt', render: (date: string) => new Date(date).toLocaleDateString() },
  ];

  if (!selectedProduct && !loading) {
    return <Card>Product not found</Card>;
  }

  return (
    <div style={{ padding: 24 }}>
      <Button icon={<ArrowLeftOutlined />} onClick={() => navigate('/products')} style={{ marginBottom: 16 }}>
        Back to Products
      </Button>

      <Card
        title={`Product: ${selectedProduct?.product?.name || ''}`}
        loading={loading}
        extra={renderLifecycleButtons()}
      >
        <Tabs defaultActiveKey="overview">
          <TabPane tab="Overview" key="overview">
            <Row gutter={24}>
              <Col span={12}>
                <Card title="Product Details">
                  <div style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid #f0f0f0' }}>
                    <span style={{ color: '#8c8c8c' }}>Code</span>
                    <span style={{ fontWeight: 500 }}>{selectedProduct?.product?.code}</span>
                  </div>
                  <div style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid #f0f0f0' }}>
                    <span style={{ color: '#8c8c8c' }}>Name</span>
                    <span style={{ fontWeight: 500 }}>{selectedProduct?.product?.name}</span>
                  </div>
                  <div style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid #f0f0f0' }}>
                    <span style={{ color: '#8c8c8c' }}>Family</span>
                    <span style={{ fontWeight: 500 }}>{selectedProduct?.product?.family}</span>
                  </div>
                  <div style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid #f0f0f0' }}>
                    <span style={{ color: '#8c8c8c' }}>Description</span>
                    <span style={{ fontWeight: 500 }}>{selectedProduct?.product?.description || '-'}</span>
                  </div>
                  <div style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0' }}>
                    <span style={{ color: '#8c8c8c' }}>Status</span>
                    <Tag color={getStatusColor(selectedProduct?.status || ProductStatus.DRAFT)}>{selectedProduct?.status}</Tag>
                  </div>
                </Card>
              </Col>
              <Col span={12}>
                <Card title="Current Version">
                  <div style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid #f0f0f0' }}>
                    <span style={{ color: '#8c8c8c' }}>Version</span>
                    <span style={{ fontWeight: 500 }}>{selectedProduct?.versionNumber}</span>
                  </div>
                  <div style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid #f0f0f0' }}>
                    <span style={{ color: '#8c8c8c' }}>Contract Count</span>
                    <span style={{ fontWeight: 500 }}>{selectedProduct?.contractCount}</span>
                  </div>
                  <div style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid #f0f0f0' }}>
                    <span style={{ color: '#8c8c8c' }}>Submitted By</span>
                    <span style={{ fontWeight: 500 }}>{selectedProduct?.submittedBy || '-'}</span>
                  </div>
                  <div style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0' }}>
                    <span style={{ color: '#8c8c8c' }}>Approved By</span>
                    <span style={{ fontWeight: 500 }}>{selectedProduct?.approvedBy || '-'}</span>
                  </div>
                </Card>
              </Col>
            </Row>
            {selectedProduct?.rejectionComment && (
              <Card title="Rejection Comment" style={{ marginTop: 16 }}>
                <p style={{ color: '#cf1322' }}>{selectedProduct.rejectionComment}</p>
              </Card>
            )}
          </TabPane>

          <TabPane tab="Version History" key="versions">
            <Table columns={versionColumns} dataSource={versionHistory} rowKey="id" pagination={false} />
          </TabPane>

          <TabPane
            tab={
              <span>
                Features
                <Button type="primary" size="small" icon={<PlusOutlined />} style={{ marginLeft: 8 }} onClick={() => setIsFeatureModalOpen(true)}>
                  Add
                </Button>
              </span>
            }
            key="features"
          >
            <Table columns={featureColumns} dataSource={features} rowKey="id" pagination={false} />
          </TabPane>

          <TabPane
            tab={
              <span>
                Pricing
                <Button type="primary" size="small" icon={<PlusOutlined />} style={{ marginLeft: 8 }} onClick={() => setIsFeeModalOpen(true)}>
                  Add
                </Button>
              </span>
            }
            key="pricing"
          >
            <Table columns={feeColumns} dataSource={feeEntries} rowKey="id" pagination={false} />
          </TabPane>

          <TabPane tab="Segments" key="segments">
            <Space wrap style={{ marginBottom: 16 }}>
              {segments.map((segment) => (
                <Tag key={segment}>{segment}</Tag>
              ))}
            </Space>
            <Form
              layout="inline"
              onFinish={(values) => handleAssignSegments(values.segments ? values.segments.split(',').map((s: string) => s.trim()) : [])}
            >
              <Form.Item name="segments" style={{ width: 400 }}>
                <Input placeholder="Enter segments separated by commas" />
              </Form.Item>
              <Form.Item>
                <Button type="primary" htmlType="submit">
                  Update Segments
                </Button>
              </Form.Item>
            </Form>
          </TabPane>

          <TabPane tab="Audit" key="audit">
            <div style={{ color: '#8c8c8c' }}>
              <p>Created: {selectedProduct?.createdAt ? new Date(selectedProduct.createdAt).toLocaleString() : '-'}</p>
              <p>Version History shows all previous versions with their approval status.</p>
            </div>
          </TabPane>
        </Tabs>
      </Card>

      <Modal title="Add Feature" open={isFeatureModalOpen} onCancel={() => setIsFeatureModalOpen(false)} onOk={() => featureForm.submit()}>
        <Form form={featureForm} onFinish={handleAddFeature} layout="vertical">
          <Form.Item name="featureKey" label="Feature Key" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="featureValue" label="Feature Value" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
        </Form>
      </Modal>

      <Modal title="Add Fee Entry" open={isFeeModalOpen} onCancel={() => setIsFeeModalOpen(false)} onOk={() => feeForm.submit()}>
        <Form form={feeForm} onFinish={handleAddFeeEntry} layout="vertical">
          <Form.Item name="feeType" label="Fee Type" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="calculationMethod" label="Calculation Method" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="currency" label="Currency" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="amount" label="Amount">
            <Input type="number" />
          </Form.Item>
          <Form.Item name="rate" label="Rate (%)">
            <Input type="number" step="0.01" />
          </Form.Item>
        </Form>
      </Modal>

      <Modal title="Reject Product" open={isRejectModalOpen} onCancel={() => setIsRejectModalOpen(false)} onOk={handleReject}>
        <Form.Item label="Rejection Comment">
          <Input.TextArea rows={4} value={rejectComment} onChange={(e) => setRejectComment(e.target.value)} />
        </Form.Item>
      </Modal>
    </div>
  );
}

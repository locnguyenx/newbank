import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Card, Table, Button, Space, Tag, Modal, Input, Spin, message } from 'antd';
import { CheckCircleOutlined, CloseCircleOutlined, EyeOutlined } from '@ant-design/icons';
import { useAppDispatch, useAppSelector } from '@/hooks/useRedux';
import { fetchPendingReviews, approveKYC, rejectKYC } from '@/store/slices/kycSlice';
import type { KYC, KYCStatus } from '@/types/kyc.types';

export function KYCReviewPage() {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { pendingReviews, loading, error } = useAppSelector((state) => state.kyc);
  const [selectedKYC, setSelectedKYC] = useState<KYC | null>(null);
  const [modalVisible, setModalVisible] = useState(false);
  const [actionType, setActionType] = useState<'approve' | 'reject'>('approve');
  const [notes, setNotes] = useState('');

  useEffect(() => {
    dispatch(fetchPendingReviews());
  }, [dispatch]);

  if (loading && pendingReviews.length === 0) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', padding: 100 }}>
        <Spin size="large" />
      </div>
    );
  }

  if (error) {
    message.error(error);
  }

  const getStatusColor = (status: KYCStatus): string => {
    const colorMap: Record<KYCStatus, string> = {
      PENDING: 'default',
      SUBMITTED: 'processing',
      UNDER_REVIEW: 'warning',
      APPROVED: 'success',
      REJECTED: 'error',
    };
    return colorMap[status];
  };

  const handleAction = (kyc: KYC, action: 'approve' | 'reject') => {
    setSelectedKYC(kyc);
    setActionType(action);
    setNotes('');
    setModalVisible(true);
  };

  const handleConfirmAction = async () => {
    if (!selectedKYC) return;

    try {
      if (actionType === 'approve') {
        await dispatch(approveKYC({ kycId: selectedKYC.id, notes })).unwrap();
        message.success('KYC approved successfully');
      } else {
        await dispatch(rejectKYC({ kycId: selectedKYC.id, notes })).unwrap();
        message.success('KYC rejected');
      }
      setModalVisible(false);
      setSelectedKYC(null);
      setNotes('');
    } catch (err) {
      message.error(`Failed to ${actionType} KYC`);
    }
  };

  const columns = [
    {
      title: 'Customer Number',
      dataIndex: 'customerNumber',
      key: 'customerNumber',
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      render: (status: KYCStatus) => (
        <Tag color={getStatusColor(status)}>{status}</Tag>
      ),
    },
    {
      title: 'Submitted At',
      dataIndex: 'submittedAt',
      key: 'submittedAt',
      render: (date: string) => date ? new Date(date).toLocaleString() : '-',
    },
    {
      title: 'Documents',
      key: 'documents',
      render: (_: unknown, record: KYC) => (
        <span>{record.documents.length} document(s)</span>
      ),
    },
    {
      title: 'Sanctions Status',
      key: 'sanctions',
      render: (_: unknown, record: KYC) => {
        if (!record.sanctionsScreening) return '-';
        const color = record.sanctionsScreening.status === 'CLEAR' ? 'success' :
                     record.sanctionsScreening.status === 'MATCH' ? 'error' : 'warning';
        return <Tag color={color}>{record.sanctionsScreening.status}</Tag>;
      },
    },
    {
      title: 'Actions',
      key: 'actions',
      render: (_: unknown, record: KYC) => (
        <Space>
          <Button
            type="link"
            icon={<EyeOutlined />}
            onClick={() => navigate(`/customers/${record.customerId}/kyc`)}
          >
            View
          </Button>
          <Button
            type="link"
            icon={<CheckCircleOutlined />}
            style={{ color: 'green' }}
            onClick={() => handleAction(record, 'approve')}
          >
            Approve
          </Button>
          <Button
            type="link"
            icon={<CloseCircleOutlined />}
            danger
            onClick={() => handleAction(record, 'reject')}
          >
            Reject
          </Button>
        </Space>
      ),
    },
  ];

  return (
    <div style={{ padding: 24 }}>
      <Card
        title="KYC Pending Reviews"
        extra={
          <Button onClick={() => dispatch(fetchPendingReviews())}>
            Refresh
          </Button>
        }
      >
        <Table
          dataSource={pendingReviews}
          columns={columns}
          rowKey="id"
          loading={loading}
          locale={{ emptyText: 'No pending KYC reviews' }}
        />
      </Card>

      <Modal
        title={actionType === 'approve' ? 'Approve KYC' : 'Reject KYC'}
        open={modalVisible}
        onOk={handleConfirmAction}
        onCancel={() => {
          setModalVisible(false);
          setSelectedKYC(null);
          setNotes('');
        }}
        okText={actionType === 'approve' ? 'Approve' : 'Reject'}
        okButtonProps={{ danger: actionType === 'reject' }}
      >
        <p>
          Are you sure you want to {actionType} KYC for customer{' '}
          <strong>{selectedKYC?.customerNumber}</strong>?
        </p>
        <Input.TextArea
          placeholder="Enter notes (optional)"
          value={notes}
          onChange={(e) => setNotes(e.target.value)}
          rows={4}
          style={{ marginTop: 16 }}
        />
      </Modal>
    </div>
  );
}

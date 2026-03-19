import { useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Card, Descriptions, Timeline, Tag, Button, Space, Spin, message, Table } from 'antd';
import { ArrowLeftOutlined, UploadOutlined, CheckCircleOutlined, ClockCircleOutlined, CloseCircleOutlined } from '@ant-design/icons';
import { useAppDispatch, useAppSelector } from '@/hooks/useRedux';
import { fetchKYC } from '@/store/slices/kycSlice';
import type { KYCStatus } from '@/types/kyc.types';

export function KYCStatusPage() {
  const { customerId } = useParams<{ customerId: string }>();
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { currentKYC: kyc, loading, error } = useAppSelector((state) => state.kyc);

  useEffect(() => {
    if (customerId) {
      dispatch(fetchKYC(parseInt(customerId, 10)));
    }
  }, [dispatch, customerId]);

  if (loading) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', padding: 100 }}>
        <Spin size="large" />
      </div>
    );
  }

  if (error) {
    message.error(error);
    return null;
  }

  if (!kyc) {
    return (
      <div style={{ padding: 24 }}>
        <Card title="KYC Status">
          <p>No KYC record found for this customer.</p>
          <Button type="primary" icon={<UploadOutlined />} onClick={() => navigate(`/customers/${customerId}/kyc/upload`)}>
            Start KYC Process
          </Button>
        </Card>
      </div>
    );
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

  const getStatusIcon = (status: KYCStatus) => {
    const iconMap: Record<KYCStatus, React.ReactNode> = {
      PENDING: <ClockCircleOutlined />,
      SUBMITTED: <ClockCircleOutlined />,
      UNDER_REVIEW: <ClockCircleOutlined />,
      APPROVED: <CheckCircleOutlined />,
      REJECTED: <CloseCircleOutlined />,
    };
    return iconMap[status];
  };

  const documentColumns = [
    {
      title: 'Document Type',
      dataIndex: 'documentType',
      key: 'documentType',
    },
    {
      title: 'File Name',
      dataIndex: 'fileName',
      key: 'fileName',
    },
    {
      title: 'Uploaded At',
      dataIndex: 'uploadedAt',
      key: 'uploadedAt',
      render: (date: string) => new Date(date).toLocaleString(),
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      render: (status: string) => {
        const colorMap: Record<string, string> = {
          UPLOADED: 'default',
          VERIFIED: 'success',
          REJECTED: 'error',
        };
        return <Tag color={colorMap[status]}>{status}</Tag>;
      },
    },
  ];

  const timelineItems = kyc.reviewTimeline.map((item) => ({
    children: (
      <div>
        <p><strong>{item.action}</strong></p>
        <p>By: {item.performedBy} at {new Date(item.performedAt).toLocaleString()}</p>
        {item.notes && <p>Notes: {item.notes}</p>}
      </div>
    ),
  }));

  return (
    <div style={{ padding: 24 }}>
      <Space style={{ marginBottom: 16 }}>
        <Button icon={<ArrowLeftOutlined />} onClick={() => navigate(`/customers/${customerId}`)}>
          Back to Customer
        </Button>
        {(kyc.status === 'PENDING' || kyc.status === 'REJECTED') && (
          <Button type="primary" icon={<UploadOutlined />} onClick={() => navigate(`/customers/${customerId}/kyc/upload`)}>
            Upload Documents
          </Button>
        )}
      </Space>

      <Card title={`KYC Status - ${kyc.customerNumber}`}>
        <Descriptions column={3} style={{ marginBottom: 24 }}>
          <Descriptions.Item label="Status">
            <Tag color={getStatusColor(kyc.status)} icon={getStatusIcon(kyc.status)}>
              {kyc.status}
            </Tag>
          </Descriptions.Item>
          <Descriptions.Item label="Submitted">{kyc.submittedAt ? new Date(kyc.submittedAt).toLocaleString() : 'Not submitted'}</Descriptions.Item>
          <Descriptions.Item label="Reviewed">{kyc.reviewedAt ? new Date(kyc.reviewedAt).toLocaleString() : 'Not reviewed'}</Descriptions.Item>
          {kyc.reviewedBy && <Descriptions.Item label="Reviewed By">{kyc.reviewedBy}</Descriptions.Item>}
          {kyc.rejectionReason && <Descriptions.Item label="Rejection Reason" span={2}>{kyc.rejectionReason}</Descriptions.Item>}
        </Descriptions>

        <Card title="Documents" style={{ marginBottom: 24 }}>
          <Table
            dataSource={kyc.documents}
            columns={documentColumns}
            rowKey="id"
            pagination={false}
            locale={{ emptyText: 'No documents uploaded' }}
          />
        </Card>

        {kyc.sanctionsScreening && (
          <Card title="Sanctions Screening" style={{ marginBottom: 24 }}>
            <Descriptions column={2}>
              <Descriptions.Item label="Status">
                <Tag color={kyc.sanctionsScreening.status === 'CLEAR' ? 'success' : kyc.sanctionsScreening.status === 'MATCH' ? 'error' : 'warning'}>
                  {kyc.sanctionsScreening.status}
                </Tag>
              </Descriptions.Item>
              <Descriptions.Item label="Screening Date">
                {new Date(kyc.sanctionsScreening.screeningDate).toLocaleString()}
              </Descriptions.Item>
              <Descriptions.Item label="Lists Checked" span={2}>
                {kyc.sanctionsScreening.lists.join(', ')}
              </Descriptions.Item>
              {kyc.sanctionsScreening.matchDetails && (
                <Descriptions.Item label="Match Details" span={2}>
                  {kyc.sanctionsScreening.matchDetails}
                </Descriptions.Item>
              )}
            </Descriptions>
          </Card>
        )}

        <Card title="Review Timeline">
          {kyc.reviewTimeline.length > 0 ? (
            <Timeline items={timelineItems} />
          ) : (
            <p style={{ color: '#999' }}>No review activity yet</p>
          )}
        </Card>
      </Card>
    </div>
  );
}

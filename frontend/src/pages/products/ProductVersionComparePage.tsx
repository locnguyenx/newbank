import { useEffect, useState } from 'react';
import { useParams, useSearchParams } from 'react-router-dom';
import { Card, Table, Tag, Button, Row, Col, Spin, Empty } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import { ArrowLeftOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import { productService } from '@/services/productService';
import { ProductStatus, type ProductDetail } from '@/types/product.types';

interface FeatureCompareRow {
  key: string;
  featureKey: string;
  v1Value?: string;
  v2Value?: string;
}

interface FeeCompareRow {
  feeType: string;
  v1Method?: string;
  v1Amount?: string;
  v2Method?: string;
  v2Amount?: string;
}

export function ProductVersionComparePage() {
  const { id } = useParams<{ id: string }>();
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const versionId1 = searchParams.get('v1');
  const versionId2 = searchParams.get('v2');

  const [version1, setVersion1] = useState<ProductDetail | null>(null);
  const [version2, setVersion2] = useState<ProductDetail | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadVersions = async () => {
      if (!versionId1 || !versionId2) {
        setLoading(false);
        return;
      }

      setLoading(true);
      try {
        const [v1, v2] = await Promise.all([
          productService.getProductVersionById(parseInt(versionId1, 10)),
          productService.getProductVersionById(parseInt(versionId2, 10)),
        ]);
        setVersion1(v1);
        setVersion2(v2);
      } catch {
        console.error('Failed to load versions');
      } finally {
        setLoading(false);
      }
    };

    loadVersions();
  }, [versionId1, versionId2]);

  const getStatusColor = (status: string): string => {
    const colorMap: Record<string, string> = {
      [ProductStatus.DRAFT]: 'default',
      [ProductStatus.PENDING_APPROVAL]: 'orange',
      [ProductStatus.APPROVED]: 'blue',
      [ProductStatus.ACTIVE]: 'green',
      [ProductStatus.SUPERSEDED]: 'purple',
      [ProductStatus.RETIRED]: 'red',
    };
    return colorMap[status] || 'default';
  };

  const renderDiff = (oldVal: string | undefined, newVal: string | undefined) => {
    if (oldVal !== newVal) {
      return (
        <span>
          <span style={{ textDecoration: 'line-through', color: '#cf1322' }}>{oldVal || '(none)'}</span>
          {' → '}
          <span style={{ color: '#3f8600' }}>{newVal || '(none)'}</span>
        </span>
      );
    }
    return <span>{oldVal || '(none)'}</span>;
  };

  if (loading) {
    return (
      <div style={{ textAlign: 'center', padding: 100 }}>
        <Spin size="large" />
      </div>
    );
  }

  if (!versionId1 || !versionId2 || !version1 || !version2) {
    return (
      <div style={{ padding: 24 }}>
        <Button icon={<ArrowLeftOutlined />} onClick={() => navigate(`/products/${id}`)} style={{ marginBottom: 16 }}>
          Back
        </Button>
        <Card>
          <Empty description="Please select two versions to compare" />
        </Card>
      </div>
    );
  }

  const allFeatureKeys = [...new Set([...version1.features.map((f) => f.featureKey), ...version2.features.map((f) => f.featureKey)])];
  const featureData: FeatureCompareRow[] = allFeatureKeys.map((key) => ({
    key,
    featureKey: key,
    v1Value: version1.features.find((f) => f.featureKey === key)?.featureValue,
    v2Value: version2.features.find((f) => f.featureKey === key)?.featureValue,
  }));

  const allFeeTypes = [...new Set([...version1.feeEntries.map((f) => f.feeType), ...version2.feeEntries.map((f) => f.feeType)])];
  const feeData: FeeCompareRow[] = allFeeTypes.map((type) => {
    const v1Fee = version1.feeEntries.find((f) => f.feeType === type);
    const v2Fee = version2.feeEntries.find((f) => f.feeType === type);
    return {
      feeType: type,
      v1Method: v1Fee?.calculationMethod,
      v1Amount: v1Fee ? `${v1Fee.currency} ${v1Fee.amount || `${v1Fee.rate}%`}` : undefined,
      v2Method: v2Fee?.calculationMethod,
      v2Amount: v2Fee ? `${v2Fee.currency} ${v2Fee.amount || `${v2Fee.rate}%`}` : undefined,
    };
  });

  const featureColumns: ColumnsType<FeatureCompareRow> = [
    {
      title: 'Feature',
      dataIndex: 'featureKey',
      key: 'featureKey',
      width: 200,
    },
    {
      title: `Version ${version1.versionNumber}`,
      key: 'v1',
      render: (_, record) => renderDiff(record.v1Value, record.v2Value),
    },
    {
      title: `Version ${version2.versionNumber}`,
      key: 'v2',
      render: (_, record) => renderDiff(record.v2Value, record.v1Value),
    },
  ];

  const feeColumns: ColumnsType<FeeCompareRow> = [
    {
      title: 'Fee Type',
      dataIndex: 'feeType',
      key: 'feeType',
      width: 150,
    },
    {
      title: `Version ${version1.versionNumber}`,
      key: 'v1',
      render: (_, record) => record.v1Method ? `${record.v1Method} - ${record.v1Amount}` : '-',
    },
    {
      title: `Version ${version2.versionNumber}`,
      key: 'v2',
      render: (_, record) => record.v2Method ? `${record.v2Method} - ${record.v2Amount}` : '-',
    },
  ];

  return (
    <div style={{ padding: 24 }}>
      <Button icon={<ArrowLeftOutlined />} onClick={() => navigate(`/products/${id}`)} style={{ marginBottom: 16 }}>
        Back to Product
      </Button>

      <Card title={`Version Comparison: ${version1.product?.name}`}>
        <Row gutter={24}>
          <Col span={12}>
            <Card size="small" title={`Version ${version1.versionNumber}`} extra={<Tag color={getStatusColor(version1.status)}>{version1.status}</Tag>}>
              <p>Contracts: {version1.contractCount}</p>
              <p>Created: {new Date(version1.createdAt).toLocaleDateString()}</p>
              <p>Description: {version1.product?.description || '-'}</p>
            </Card>
          </Col>
          <Col span={12}>
            <Card size="small" title={`Version ${version2.versionNumber}`} extra={<Tag color={getStatusColor(version2.status)}>{version2.status}</Tag>}>
              <p>Contracts: {version2.contractCount}</p>
              <p>Created: {new Date(version2.createdAt).toLocaleDateString()}</p>
              <p>Description: {renderDiff(version1.product?.description, version2.product?.description)}</p>
            </Card>
          </Col>
        </Row>

        <h3 style={{ marginTop: 24 }}>Features Comparison</h3>
        <Table columns={featureColumns} dataSource={featureData} rowKey="key" pagination={false} size="small" />

        <h3 style={{ marginTop: 24 }}>Fees Comparison</h3>
        <Table columns={feeColumns} dataSource={feeData} rowKey="feeType" pagination={false} size="small" />
      </Card>
    </div>
  );
}

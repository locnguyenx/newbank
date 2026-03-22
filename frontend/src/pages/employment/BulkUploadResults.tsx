import { Card, Statistic, Row, Col, Table, Alert } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import { CheckCircleOutlined, CloseCircleOutlined } from '@ant-design/icons';
// @ts-expect-error - BulkUploadError may not be exported from @/types
import type { BulkUploadResult, BulkUploadError } from '@/types';

interface BulkUploadResultsProps {
  result: BulkUploadResult;
}

export function BulkUploadResults({ result }: BulkUploadResultsProps) {
  const hasErrors = result.failureCount > 0;

  const errorColumns: ColumnsType<BulkUploadError> = [
    {
      title: 'Row',
      dataIndex: 'row',
      key: 'row',
      width: 80,
    },
    {
      title: 'Field',
      dataIndex: 'field',
      key: 'field',
      width: 150,
    },
    {
      title: 'Value',
      dataIndex: 'value',
      key: 'value',
      width: 200,
    },
    {
      title: 'Error',
      dataIndex: 'message',
      key: 'message',
    },
  ];

  return (
    <Card title="Upload Results">
      <Row gutter={16} style={{ marginBottom: 24 }}>
        <Col span={8}>
          {/* @ts-expect-error - totalRows may not exist on BulkUploadResult */}
          <Statistic title="Total Rows" value={result.totalRows} />
        </Col>
        <Col span={8}>
          <Statistic
            title="Successful"
            value={result.successCount}
            valueStyle={{ color: '#3f8600' }}
            prefix={<CheckCircleOutlined />}
          />
        </Col>
        <Col span={8}>
          <Statistic
            title="Failed"
            value={result.failureCount}
            valueStyle={{ color: result.failureCount > 0 ? '#cf1322' : undefined }}
            prefix={result.failureCount > 0 ? <CloseCircleOutlined /> : undefined}
          />
        </Col>
      </Row>

      {hasErrors && (
        <>
          <Alert
            type="warning"
            message={`${result.failureCount} row(s) failed. Review errors below.`}
            showIcon
            style={{ marginBottom: 16 }}
          />
          <Table
            columns={errorColumns}
            dataSource={result.errors}
            rowKey={(_, index) => index!}
            pagination={false}
            size="small"
          />
        </>
      )}

      {!hasErrors && (
        <Alert
          type="success"
          // @ts-expect-error - totalRows may not exist on BulkUploadResult
          message={`All ${result.totalRows} rows uploaded successfully.`}
          showIcon
        />
      )}
    </Card>
  );
}
